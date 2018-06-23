package com.pmonteiro.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.pmonteiro.dropwizard.api.TaskApi;
import com.pmonteiro.dropwizard.core.Task;
import com.pmonteiro.dropwizard.dao.TaskDAO;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.*;
import static org.eclipse.jetty.http.HttpStatus.*;

@Api("/")
@Path("tasks")
@Produces(APPLICATION_JSON)
public class TasksResource{

    private TaskDAO dao;

    @Inject
    public TasksResource(TaskDAO taskDAO) {
        this.dao = taskDAO;
    }

    @GET
    @Timed
    @ApiOperation(
            value = "Get all the tasks",
            notes = "Returns all the tasks save on the database")
    @ApiResponses(value = { @ApiResponse(code = OK_200, message = "", responseContainer = "List", response = Task.class)})
    public Response getTasks() {
        Optional<List> tasks = dao.find(Task.class)
                .map(this::getTasksWithHypermediaLinks);

        return ok().entity(ImmutableMap.of("tasks", tasks)).build();
    }

    @GET
    @Path("/{taskId}")
    @Timed
    @ApiOperation(
            value = "Get task by id",
            notes = "Returns task by Id. If it does not exist it will return a HTTP 404")
    @ApiResponses(value = {
        @ApiResponse(code = OK_200, message = "", response = Task.class),
        @ApiResponse(code = NOT_FOUND_404, message = "Not Found")})
    public Response getTask(@ApiParam(value = "taskId", example = "1") @PathParam("taskId") Long id) {
        return dao.findById(id)
                .map(task -> ok().entity(task.asEmbedded()).build())
                .orElse(status(NOT_FOUND_404).build());
    }

    @POST
    @Timed
    @Consumes(APPLICATION_JSON)
    @ApiOperation(
            value = "Creates a new task",
            notes = "Creates a new task. Task descriptions are not unique.")
    @ApiResponses(value = {
            @ApiResponse(code = CREATED_201, message = "Created", response = TaskApi.class)})
    public Response create(@ApiParam(value = "payload", required = true) TaskApi taskApi) {
        Task task = new Task(taskApi);
        dao.persist(task);
        return status(CREATED_201).entity(task.asEmbedded()).build();
    }

    @PUT
    @Path("{taskId}")
    @Timed
    @Consumes(APPLICATION_JSON)
    @ApiOperation(
            value = "Updates task by id",
            notes = "Updates a task description if available in the database")
    @ApiResponses(value = {
            @ApiResponse(code = NO_CONTENT_204, message = "Updated"),
            @ApiResponse(code = NOT_FOUND_404, message = "Not Found")})
    public Response update(@ApiParam(value = "taskId", example = "1") @PathParam("taskId") Long id,
                           @ApiParam(value = "payload", required = true) TaskApi task) {
        return dao.update(task, id)
                .filter(updatedRows ->  updatedRows == 1)
                .map(updated -> noContent().build())
                .orElse(status(NOT_FOUND_404).build());
    }

    @DELETE
    @Path("/{taskId}")
    @Timed
    @Consumes(APPLICATION_JSON)
    @Transactional
    @ApiOperation(
            value = "Deletes task by id",
            notes = "Deletes a if available in the database")
    @ApiResponses(value = {
            @ApiResponse(code = NO_CONTENT_204, message = "Deleted"),
            @ApiResponse(code = NOT_FOUND_404, message = "Not Found")})

    public Response delete(@ApiParam(value = "taskId", example = "1") @PathParam("taskId") Long id) {
        return dao.findById(id)
                .map(task -> {
                    dao.remove(task);
                    return noContent().build();
                })
                .orElse(status(NOT_FOUND_404).build());
    }

    private List getTasksWithHypermediaLinks(List<Task> list) {
        return list.stream()
                .map(task -> task.asEmbedded())
                .collect(Collectors.toList());
    }
}

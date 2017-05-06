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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@Api("/")
@Path("tasks")
@Produces(APPLICATION_JSON)
public class TasksResource {

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
    @ApiResponses(value = { @ApiResponse(code = 200, message = "", responseContainer = "List", response = Task.class)})
    public Response getTasks() {
        return ok().entity(ImmutableMap.of("tasks", dao.find(Task.class))).build();
    }

    @GET
    @Path("/{taskId}")
    @Timed
    @ApiOperation(
            value = "Get task by id",
            notes = "Returns task by Id. If it does not exist it will return a HTTP 404")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = Task.class),
        @ApiResponse(code = 404, message = "Not Found")})
    public Response getTask(@ApiParam(value = "taskId", example = "1") @PathParam("taskId") Long id) {
        return dao.findById(id)
                .map(task -> ok().entity(task).build())
                .orElse(status(NOT_FOUND.getStatusCode()).build());
    }

    @POST
    @Timed
    @Consumes(APPLICATION_JSON)
    @ApiOperation(
            value = "Creates a new task",
            notes = "Creates a new task. Task descriptions are not unique.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = TaskApi.class)})
    public Response create(@ApiParam(value = "payload", required = true) TaskApi taskApi) {
        Task task = new Task(taskApi);
        dao.persist(task);
        return status(CREATED).entity(task).build();
    }

    @PUT
    @Path("{taskId}")
    @Timed
    @Consumes(APPLICATION_JSON)
    @ApiOperation(
            value = "Updates task by id",
            notes = "Updates a task description if available in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated"),
            @ApiResponse(code = 404, message = "Not Found")})
    public Response update(@ApiParam(value = "taskId", example = "1") @PathParam("taskId") Long id,
                           @ApiParam(value = "payload", required = true) Task task) {
        return dao.update(task, id)
                .filter(updatedRows ->  updatedRows == 1)
                .map(updated -> ok().build())
                .orElse(status(NOT_FOUND.getStatusCode()).build());
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
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})

    public Response delete(@ApiParam(value = "taskId", example = "1") @PathParam("taskId") Long id) {
        return dao.findById(id)
                .map(task -> {
                    dao.remove(task);
                    return noContent().build();
                })
                .orElse(status(NOT_FOUND.getStatusCode()).build());
    }
}

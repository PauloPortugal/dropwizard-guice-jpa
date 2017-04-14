package com.pmonteiro.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.pmonteiro.dropwizard.core.Task;
import com.pmonteiro.dropwizard.dao.TaskDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@Path("tasks")
@Produces(APPLICATION_JSON)
public class TasksResource {

    final Logger logger = LoggerFactory.getLogger(TasksResource.class);
    private TaskDAO dao;

    @Inject
    public TasksResource(TaskDAO taskDAO) {
        this.dao = taskDAO;
    }

    @GET
    @Timed
    public Response getTasks() {
        return ok().entity(ImmutableMap.of("tasks", dao.find(Task.class))).build();
    }

    @GET
    @Path("/{taskId}")
    @Timed
    public Response getTask(@PathParam("taskId") Long id) {
        return dao.findById(id)
                .map(task -> ok().entity(task).build())
                .orElse(status(NOT_FOUND.getStatusCode()).build());
    }

    @POST
    @Timed
    @Consumes(APPLICATION_JSON)
    public Response create(Task task) {
        dao.persist(task);
        return status(CREATED).entity(task).build();
    }

    @PUT
    @Path("{taskId}")
    @Timed
    @Consumes(APPLICATION_JSON)
    public Response update(@PathParam("taskId") Long id, Task task) {
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
    public Response delete(@PathParam("taskId") Long id) {
        return dao.findById(id)
                .map(task -> {
                    dao.remove(task);
                    return noContent().build();
                })
                .orElse(status(NOT_FOUND.getStatusCode()).build());
    }
}

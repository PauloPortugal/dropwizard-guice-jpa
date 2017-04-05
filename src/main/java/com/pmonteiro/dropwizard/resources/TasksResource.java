package com.pmonteiro.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.pmonteiro.dropwizard.core.Task;
import com.pmonteiro.dropwizard.dao.TaskDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("tasks")
@Produces(MediaType.APPLICATION_JSON)
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
        return Response.ok().entity(dao.find(Task.class)).build();
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Task task) {
        dao.persist(task);
        return Response.status(CREATED).build();
    }

}

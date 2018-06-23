package com.pmonteiro.dropwizard.resources;

import com.pmonteiro.dropwizard.DropwizardGuiceApplication;
import com.pmonteiro.dropwizard.DropwizardGuiceConfiguration;
import com.pmonteiro.dropwizard.api.TaskApi;
import com.pmonteiro.dropwizard.core.Task;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.junit.DAOTestRule;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

public class TasksResourceTest {

    @ClassRule
    public static DropwizardAppRule<DropwizardGuiceConfiguration>
            rule = new DropwizardAppRule<>(DropwizardGuiceApplication.class,  resourceFilePath("test-config.yml"));

    @Rule
    public DAOTestRule database = DAOTestRule.newBuilder().addEntityClass(Task.class).build();
    private DBI jdbi;


    @Before
    public void setUp() {
        jdbi = new DBIFactory().build(rule.getEnvironment(), rule.getConfiguration().getDataSourceFactory(), "postgres");
        jdbi.withHandle(handle -> handle.update("DELETE FROM tasks"));
        jdbi.withHandle(handle -> handle.update("ALTER TABLE tasks ALTER COLUMN id RESTART WITH 1"));
    }

    @Test
    public void getTasks_whenNoTasksExist_ShouldReturnEmptyArray() {
        when()
                .get("/tasks")
        .then()
                .statusCode(OK_200)
                .body("tasks", is(empty()));
    }

    @Test
    public void getTasks_whenTasksExist_ShouldReturnTasks() {
        Task task = insertTask(new Task("some-description"));
        when()
                .get("/tasks")
        .then()
                .statusCode(OK_200)
                    .body("tasks[0].description", is(task.getDescription()))
                    .body("tasks[0].id", is(1))
                    .body("tasks[0]._links.self.href", is("/tasks/1"));
    }

    @Test
    public void getTask_whenTaskDoesNotExist_ShouldReturn404() {
        when()
                .get("/task/1")
        .then()
                .statusCode(NOT_FOUND_404);
    }

    @Test
    public void getTask_whenIdExists_ShouldReturnTask() {
        insertTask(new Task("description-1"));
        Task task2 = insertTask(new Task("description-2"));
        when()
                .get("/tasks/2")
                .then()
                .statusCode(OK_200)
                .body("description", is(task2.getDescription()))
                .body("id", is(2))
                .body("_links.self.href", is("/tasks/2"));
    }

    @Test
    public void create() {
        TaskApi task = new TaskApi("description");
        given()
                .accept(JSON)
                .contentType(JSON)
                .body(task)
        .when()
                .post("/tasks")
        .then()
                .statusCode(CREATED_201)
                .body("description", is(task.getDescription()))
                .body("id", is(1))
                .body("_links.self.href", is("/tasks/1"));
    }

    @Test
    public void update_whenTaskDoesNotExist_ShouldReturn404() {
        given()
                .accept(JSON)
                .contentType(JSON)
                .body(new TaskApi("description"))
        .when()
                .put("/tasks/1")
        .then()
                .statusCode(NOT_FOUND_404);
    }

    @Test
    public void update_whenTaskExists_ShouldUpdateTask() {
        insertTask(new Task("description"));
        given()
                .accept(JSON)
                .contentType(JSON)
                .body(new TaskApi("new-description"))
        .when()
                .put("/tasks/1")
        .then()
                .statusCode(NO_CONTENT_204);
    }

    @Test
    public void delete_whenTaskExists_ShouldDeleteAndReturn204() {
        insertTask(new Task("description"));
        given()
                .accept(JSON)
                .contentType(JSON)
        .when()
                .delete("/tasks/1")
        .then()
                .statusCode(NO_CONTENT_204);
    }

    @Test
    public void delete_whenTaskDoesNotExist_ShouldReturn404() {
        given()
                .accept(JSON)
                .contentType(JSON)
        .when()
                .delete("/tasks/1")
        .then()
                .statusCode(NOT_FOUND_404);
    }

    private Task insertTask(Task task) {
        jdbi.withHandle(handle -> handle
                .createStatement("insert into tasks(description) VALUES (:description)")
                .bind("description", task.getDescription())
                .execute());
        return task;
    }
}
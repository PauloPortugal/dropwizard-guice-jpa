package com.pmonteiro.dropwizard.resources;

import com.pmonteiro.dropwizard.DropwizardGuiceApplication;
import com.pmonteiro.dropwizard.DropwizardGuiceConfiguration;
import com.pmonteiro.dropwizard.core.Task;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.junit.DAOTestRule;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.skife.jdbi.v2.DBI;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

public class TasksResourceTest {

    @ClassRule
    public static DropwizardAppRule<DropwizardGuiceConfiguration>
            rule = new DropwizardAppRule<>(DropwizardGuiceApplication.class,  resourceFilePath("test-config.yml"));

    @Rule
    public DAOTestRule database = DAOTestRule.newBuilder().addEntityClass(Task.class).build();
    private final DBIFactory factory = new DBIFactory();
    private DBI jdbi;


    @Before
    public void setup() {
        jdbi = factory.build(rule.getEnvironment(), rule.getConfiguration().getDataSourceFactory(), "postgres");
        jdbi.withHandle(handle -> handle.update("DELETE FROM tasks"));
        jdbi.withHandle(handle -> handle.update("ALTER TABLE tasks ALTER COLUMN id RESTART WITH 1"));
    }

    @Test
    public void getTasks_whenNoTasksExist_ShouldReturnEmptyArray() throws Exception {
        when()
                .get("/tasks")
        .then()
                .statusCode(OK.getStatusCode())
                .body("tasks", is(empty()));
    }

    @Test
    public void getTasks_whenTasksExist_ShouldReturnTasks() throws Exception {
        Task task = insertTask(new Task("some-description"));
        when()
                .get("/tasks")
        .then()
                .statusCode(OK.getStatusCode())
                    .body("tasks[0].description", is(task.getDescription()))
                    .body("tasks[0].id", is(1));
    }

    @Test
    public void getTask_whenTaskDoesNotExist_ShouldReturn404() throws Exception {
        when()
                .get("/task/1")
        .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void getTask_whenIdExists_ShouldReturnTask() throws Exception {
        insertTask(new Task("description-1"));
        Task task2 = insertTask(new Task("description-2"));
        when()
                .get("/tasks/2")
                .then()
                .statusCode(OK.getStatusCode())
                .body("description", is(task2.getDescription()))
                .body("id", is(2));
    }

    @Test
    public void create() throws Exception {
        Task task = new Task("description");
        given()
                .accept(JSON)
                .contentType(JSON)
                .body(task)
        .when()
                .post("/tasks")
        .then()
                .statusCode(CREATED.getStatusCode())
                .body("description", is(task.getDescription()))
                .body("id", is(1));
    }

    @Test @Ignore
    public void update_whenTaskDoesNotExist_ShouldReturn404() throws Exception {
        Task task = new Task("description");
        given()
                .accept(JSON)
                .contentType(JSON)
                .body(task)
        .when()
                .put("/tasks/1")
        .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void update_whenTaskExists_ShouldUpdateTask() throws Exception {
        insertTask(new Task("description"));
        Task changedTaskDescription = new Task("new-description");
        given()
                .accept(JSON)
                .contentType(JSON)
                .body(changedTaskDescription)
        .when()
                .put("/tasks/1")
        .then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    public void delete_whenTaskExists_ShouldDeleteAndReturn204() throws Exception {
        insertTask(new Task("description"));
        given()
                .accept(JSON)
                .contentType(JSON)
        .when()
                .delete("/tasks/1")
        .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    @Test
    public void delete_whenTaskDoesNotExist_ShouldReturn404() throws Exception {
        given()
                .accept(JSON)
                .contentType(JSON)
        .when()
                .delete("/tasks/1")
        .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    private Task insertTask(Task task) {
        jdbi.withHandle(handle -> handle
                .createStatement("insert into tasks(description) VALUES (:description)")
                .bind("description", task.getDescription())
                .execute());
        return task;
    }
}
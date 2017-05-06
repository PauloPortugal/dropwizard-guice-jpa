[![Build Status](https://travis-ci.org/PauloPortugal/dropwizard-guice-jpa.png)](https://travis-ci.org/PauloPortugal/dropwizard-guice-jpa.svg?branch=master)     [![Codacy Badge](https://api.codacy.com/project/badge/Grade/82869bd865d74a09bee564cc761c831a)](https://www.codacy.com/app/PauloPortugal/dropwizard-guice-jpa?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PauloPortugal/dropwizard-guice-jpa&amp;utm_campaign=Badge_Grade) [![Coverage Status](https://coveralls.io/repos/github/PauloPortugal/dropwizard-guice-jpa/badge.svg?branch=master)](https://coveralls.io/github/PauloPortugal/dropwizard-guice-jpa?branch=master)

# Dropwizard Guice and JPA application

This a dockerized RESTful Dropwizard application, to provide an example on how to integrate JPA in Dropwizard, using dependency injection.
The example is a simple RESTful interface to easily manage a task planner.

- using [Dropwizard](https://dropwizard.github.io/dropwizard/) v1.1.0 framework
- dependency injection achieve through [Google Guice](https://code.google.com/p/google-guice/)
- JPA implementation through [Hibernate](http://hibernate.org/)
- [H2](http://http://www.h2database.com/) as an in-memory database
- [rest-assured](https://code.google.com/p/rest-assured/) for the integration tests
- API documentation provided by [swagger.io](http://swagger.io//) and [swagger UI](http://swagger.io/swagger-ui/)
- Database migrations provided by [FlywayDB](https://flywaydb.org/)
- [Docker](https://www.docker.com/) as the containerisation solution

How to start the DropwizardGuice application
---

1. Run `mvn clean package` to build your application
1. Start application with `java -jar target/dropwizard-guice-1.0-SNAPSHOT-uber.jar server target/config.yml`
1. To check that the application is running enter url `http://localhost:8080`
1. To interact with the application using Swagger UI endpoints enter url `http://localhost:8080/swagger`
1. To run the application inside a docker container, `./docker-run.sh`

<a name="paths"></a>
## API Paths

<a name="create"></a>
### Creates a new task
```
POST /tasks
```


#### Description
Creates a new task. Task descriptions are not unique.


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**body**  <br>*required*|payload|[Task Entity](#task-entity)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[Task Entity](#task-entity)|


#### Consumes

* `application/json`


#### Produces

* `application/json`


<a name="gettasks"></a>
### Get all the tasks
```
GET /tasks
```


#### Description
Returns all the tasks save on the database


#### Responses

|HTTP Code|Schema|
|---|---|
|**200**|< [Task Entity](#task-entity) > array|


#### Produces

* `application/json`


<a name="gettask"></a>
### Get task by id
```
GET /tasks/{taskId}
```


#### Description
Returns task by Id. If it does not exist it will return a HTTP 404


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**taskId**  <br>*required*|taskId|integer(int64)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**||[Task Entity](#task-entity)|
|**404**|Not Found|No Content|


#### Produces

* `application/json`


<a name="update"></a>
### Updates task by id
```
PUT /tasks/{taskId}
```


#### Description
Updates a task description if available in the database


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**taskId**  <br>*required*|taskId|integer(int64)|
|**Body**|**body**  <br>*required*|payload|[Task Entity](#task-entity)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Updated|No Content|
|**404**|Not Found|No Content|


#### Consumes

* `application/json`


#### Produces

* `application/json`


<a name="delete"></a>
### Deletes task by id
```
DELETE /tasks/{taskId}
```


#### Description
Deletes a if available in the database


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**taskId**  <br>*required*|taskId|integer(int64)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**404**|Not Found|No Content|


#### Consumes

* `application/json`


#### Produces

* `application/json`




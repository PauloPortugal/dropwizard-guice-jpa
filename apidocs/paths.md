
<a name="paths"></a>
## Paths

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
|**Body**|**body**  <br>*required*|payload|[A new task](#a-new-task)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[A new task](#a-new-task)|


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




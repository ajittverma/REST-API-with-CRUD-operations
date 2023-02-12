# REST-API-with-CRUD-operations


In this implementation, the following dependencies are used:

    MongoDB Java Driver
    Spark Java
    Gson
    
  
This updated implementation includes the following CRUD operations:

    GET /tasks - Retrieves all tasks
    GET /tasks/:id - Retrieves a specific task by its id
    POST /tasks - Creates a new task
    PUT /tasks/:id - Updates a specific task by its id
    DELETE /tasks/:id - Deletes a specific task by its id
    
    
 
 
 This updated implementation includes pagination in the GET /tasks API.
 The page size is set to 10 and the page number is passed as a query 
 parameter page. The skip and limit methods of the MongoDB Java.

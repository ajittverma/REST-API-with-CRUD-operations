import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Sorts.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import spark.Spark;

import static spark.Spark.*;

public class TodoApi {
    private static final int PAGE_SIZE = 10;
    private static final String DB_NAME = "todos";
    private static final String COLLECTION_NAME = "tasks";

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        // Get all tasks
        get("/tasks", (request, response) -> {
            response.type("application/json");
            int page = Integer.parseInt(request.queryParams("page"));
            return collection.find().sort(ascending("end_date"))
                    .skip(PAGE_SIZE * (page - 1)).limit(PAGE_SIZE).into(new Document());
        }, new JsonTransformer());

        // Get a specific task
        get("/tasks/:id", (request, response) -> {
            response.type("application/json");
            return collection.find(Filters.eq("_id", new ObjectId(request.params(":id")))).first();
        }, new JsonTransformer());

        // Add a new task
        post("/tasks", (request, response) -> {
            response.type("application/json");
            Document task = Document.parse(request.body());
            collection.insertOne(task);
            return task;
        }, new JsonTransformer());

        // Update a task
        put("/tasks/:id", (request, response) -> {
            response.type("application/json");
            Document task = Document.parse(request.body());
            task.put("_id", new ObjectId(request.params(":id")));
            collection.replaceOne(Filters.eq("_id", task.getObjectId("_id")), task);
            return task;
        }, new JsonTransformer());

        // Delete a task
        delete("/tasks/:id", (request, response) -> {
            collection.deleteOne(Filters.eq("_id", new ObjectId(request.params(":id"))));
            response.status(204);
            return response;
        });
    }
}
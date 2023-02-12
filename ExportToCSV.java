import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import spark.Spark;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

public class TodoApi {
    private static final String DB_NAME = "todos";
    private static final String COLLECTION_NAME = "tasks";

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        // Get all tasks
        get("/tasks", (request, response) -> {
            response.type("application/json");
            return collection.find().into(new ArrayList<>());
        }, new JsonTransformer());

        // Export tasks to CSV
        get("/tasks/csv", (request, response) -> {
            response.type("text/csv");
            response.header("Content-Disposition", "attachment; filename=tasks.csv");

            List<Document> tasks = collection.find().into(new ArrayList<>());
            StringWriter writer = new StringWriter();
            writer.append("task,is_completed,end_date\n");
            for (Document task : tasks) {
                writer.append(task.getString("task")).append(",")
                        .append(task.getBoolean("is_completed").toString()).append(",")
                        .append(task.getDate("end_date").toString()).append("\n");
            }

            return writer.toString();
        });
    }
}
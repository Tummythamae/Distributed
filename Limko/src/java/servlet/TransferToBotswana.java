package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.MongoClientSettings; 
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TransferToBotswana", urlPatterns = {"/TransferToBotswana"})
public class TransferToBotswana extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // MongoDB connection string
        String connectionString = "mongodb+srv://reitumetsethamae229:LydiahThamae02@distributed.a5hlsdp.mongodb.net/";

        // MongoDB database name
        String dbName = "limkokwing_university";

        // Get the username parameter from the request
        String username = request.getParameter("username");

        try {
            // Set up MongoDB connection
            ConnectionString connString = new ConnectionString(connectionString);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);

            // Accessing the database
            MongoDatabase database = mongoClient.getDatabase(dbName);

            // Retrieving the collection
            MongoCollection<Document> collection = database.getCollection("student_details");

            // Update operation to change campus to Botswana
            UpdateResult result = collection.updateOne(Filters.eq("username", username),
                    new Document("$set", new Document("campus", "Botswana")));

            // Check if the update was successful
            if (result.getModifiedCount() > 0) {
                response.getWriter().println("<script>alert('Student transferred to Botswana successfully.'); window.location.href = document.referrer;</script>");
            } else {
                response.getWriter().println("<script>alert('Failed to transfer student to Botswana. Student not found or already transferred.'); window.location.href = document.referrer;</script>");
            }

            mongoClient.close();
        } catch (Exception e) {
            response.getWriter().println("<script>alert('An error occurred while transferring student to Botswana: " + e.getMessage() + "'); window.location.href = document.referrer;</script>");
        }
    }
    }


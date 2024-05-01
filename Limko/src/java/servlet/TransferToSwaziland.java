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

@WebServlet(name = "TransferToSwaziland", urlPatterns = {"/TransferToSwaziland"})
public class TransferToSwaziland extends HttpServlet {

    private static final String CONNECTION_STRING = "mongodb+srv://reitumetsethamae229:LydiahThamae02@distributed.a5hlsdp.mongodb.net/";
    private static final String DATABASE_NAME = "limkokwing_university";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        try {
            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("student_details");

            UpdateResult result = collection.updateOne(Filters.eq("username", username),
                    new Document("$set", new Document("campus", "Swaziland")));

          if (result.getModifiedCount() > 0) {
                response.getWriter().println("<script>alert('Student transferred to Swaziland successfully.'); window.location.href = document.referrer;</script>");
            } else {
                response.getWriter().println("<script>alert('Failed to transfer student to Swaziland. Student not found or already transferred.'); window.location.href = document.referrer;</script>");
            }

            mongoClient.close();
        } catch (Exception e) {
            response.getWriter().println("<script>alert('An error occurred while transferring student to Swaziland: " + e.getMessage() + "'); window.location.href = document.referrer;</script>");
        }
    }
}

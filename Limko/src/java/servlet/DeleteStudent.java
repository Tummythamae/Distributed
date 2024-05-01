package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.MongoClientSettings; 
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteStudent", urlPatterns = {"/DeleteStudent"})
public class DeleteStudent extends HttpServlet {

    private static final String CONNECTION_STRING = "mongodb+srv://reitumetsethamae229:LydiahThamae02@distributed.a5hlsdp.mongodb.net/";
    private static final String DATABASE_NAME = "limkokwing_university";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        try {
            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("student_details");

            DeleteResult result = collection.deleteOne(Filters.eq("username", username));

            if (result.getDeletedCount() > 0) {
                response.getWriter().println("<script>alert('Student record deleted successfully.'); window.location.href = document.referrer;</script>");
            } else {
                response.getWriter().println("<script>alert('Failed to delete student record. Student not found.'); window.location.href = document.referrer;</script>");
            }

            mongoClient.close();
        } catch (Exception e) {
            response.getWriter().println("<script>alert('An error occurred while deleting student record: " + e.getMessage() + "'); window.location.href = document.referrer;</script>");
        }
    }
}

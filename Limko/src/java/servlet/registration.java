package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TSEPO DESPO
 */
@WebServlet(name = "registration", urlPatterns = {"/registration"})
public class registration extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
     
    }


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // MongoDB connection string
        String connectionString = "mongodb+srv://reitumetsethamae229:LydiahThamae02@distributed.a5hlsdp.mongodb.net/";

        // MongoDB database name
        String dbName = "limkokwing_university";

        try {
            // Set up MongoDB connection
            ConnectionString connString = new ConnectionString(connectionString);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);

            // Accessing the database
            MongoDatabase database = mongoClient.getDatabase(dbName);

            // Creating the collection (table) if it doesn't exist
            if (!database.listCollectionNames().into(new ArrayList<>()).contains("student_details")) {
                database.createCollection("student_details");
            }

            // Getting form data
            String username = request.getParameter("username");
            String surname = request.getParameter("surname");
         
            String gender = request.getParameter("gender");
            String campus = request.getParameter("campus");
            String degree_level = request.getParameter("degree");
            String behavior = request.getParameter("behavior");

            // Creating a new document to insert into the database
            Document document = new Document("username", username)
                    .append("surname", surname)
                    
                    .append("gender", gender)
                    .append("campus", campus)
                    .append("degree", degree_level)
                    .append("behavior", behavior);

            // Inserting the document into the collection (table)
            database.getCollection("student_details").insertOne(document);

            // Success message
           String successMessage = "Data saved successfully!";
           String alertScript = "<script>alert('" + successMessage + "'); window.location.href = 'register.html';</script>";
            
            response.getWriter().println(alertScript);

            // Closing MongoDB connection
            mongoClient.close();
        } catch (IOException e) {
             // Error handling
            String successMessage = "Data saved successfully!";
            String alertScript = "<script>alert('" + successMessage + "'); window.location.href = 'register.html';</script>";
    
            // Send the alert script back to the client
            response.getWriter().println(alertScript);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
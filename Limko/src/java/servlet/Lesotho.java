package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.MongoClientSettings; 
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "Lesotho", urlPatterns = {"/Lesotho"})
public class Lesotho extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // MongoDB connection string
        String connectionString = "mongodb+srv://reitumetsethamae229:LydiahThamae02@distributed.a5hlsdp.mongodb.net/";

        // MongoDB database name
        String dbName = "limkokwing_university";

        // Filter parameter
        String campusFilter = "Lesotho";

        // Search parameter
        String searchQuery = request.getParameter("search");

        try {
            // Set up MongoDB connection
            ConnectionString connString = new ConnectionString(connectionString);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);

            // Accessing the database
            MongoDatabase database = mongoClient.getDatabase(dbName);

            // Retrieving students from Swaziland
            MongoCollection<Document> collection = database.getCollection("student_details");
            Document query = new Document("campus", campusFilter);

            // Apply search filter if provided
            if (searchQuery != null && !searchQuery.isEmpty()) {
                Pattern regexPattern = Pattern.compile(searchQuery, Pattern.CASE_INSENSITIVE);
                List<Document> regexPatternDocuments = Arrays.asList(
                    new Document("username", regexPattern),
                    new Document("surname", regexPattern),
                    new Document("gender", regexPattern)
                );
                query.append("$or", regexPatternDocuments);
            }

            FindIterable<Document> result = collection.find(query);
            
             // Check if any results were found
            boolean foundResults = false;
            for (Document doc : result) {
                foundResults = true;
                break;
            }

            // HTML table for displaying student data
            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<html>");
            htmlTable.append("<head>");
            htmlTable.append("<style>");
            htmlTable.append("table { border-collapse: collapse; width: 100%; }");
            htmlTable.append("th, td { text-align: left; padding: 8px; }");
            htmlTable.append("th { background-color: #f2f2f2; }");
            htmlTable.append("tr:nth-child(even) { background-color: #f2f2f2; }");
            htmlTable.append(".action-buttons { display: flex; }"); // Style for the button container
            htmlTable.append(".action-buttons form { margin-right: 10px; }"); // Style for the button form
            htmlTable.append(".action-buttons button { padding: 5px 10px; background-color: #007bff; color: #fff; border: none; border-radius: 3px; cursor: pointer; }"); // Style for the buttons
            htmlTable.append(".action-buttons button:hover { background-color: #0056b3; }"); // Style for button hover effect
            htmlTable.append("input[type=text] { padding: 8px; margin-right: 5px; border-radius: 3px; border: 1px solid #ccc; }"); // Style for search input field
            htmlTable.append("button[type=submit] { padding: 8px 12px; background-color: #007bff; color: #fff; border: none; border-radius: 3px; cursor: pointer; }"); // Style for search button
            htmlTable.append("</style>");
            htmlTable.append("</head>");
            htmlTable.append("<body>");
            htmlTable.append("<h2>Lesotho Students</h2>");

            // Search form
            htmlTable.append("<form method=\"get\">");
            htmlTable.append("<input type=\"text\" name=\"search\" placeholder=\"Search by name, surname, or gender\">");
            htmlTable.append("<button type=\"submit\">Search</button>");
            htmlTable.append("</form>");
            
            if (!foundResults) {
             // Display alert if no results were found and return to the table
             String alertScript = "<script>alert('No results found for the search query.'); window.location.href = 'Lesotho';</script>";
             response.getWriter().println(alertScript);
             return;
           }


            htmlTable.append("<table>");
            htmlTable.append("<tr>");
            htmlTable.append("<th>Username</th>");
            htmlTable.append("<th>Surname</th>");
            htmlTable.append("<th>Gender</th>");
            htmlTable.append("<th>Degree Level</th>");
            htmlTable.append("<th>Behavior</th>");
            htmlTable.append("<th>Action</th>"); // Add column for action buttons
            htmlTable.append("</tr>");

            // Extracting student data from the query result and adding rows to the table
            try (MongoCursor<Document> cursor = result.iterator()) {
                while (cursor.hasNext()) {
                    Document student = cursor.next();
                    htmlTable.append("<tr>");
                    htmlTable.append("<td>").append(student.getString("username")).append("</td>");
                    htmlTable.append("<td>").append(student.getString("surname")).append("</td>");
                    htmlTable.append("<td>").append(student.getString("gender")).append("</td>");
                    htmlTable.append("<td>").append(student.getString("degree")).append("</td>");
                    htmlTable.append("<td>").append(student.getString("behavior")).append("</td>");

                    // Add buttons for transferring to different campuses
                    htmlTable.append("<td class=\"action-buttons\">");
                    htmlTable.append("<form action=\"TransferToSwaziland\" method=\"get\">");
                    htmlTable.append("<input type=\"hidden\" name=\"username\" value=\"" + student.getString("username") + "\">");
                    htmlTable.append("<button type=\"submit\">Transfer to Swaziland</button>");
                    htmlTable.append("</form>");
                    htmlTable.append("<form action=\"TransferToBotswana\" method=\"get\">");
                    htmlTable.append("<input type=\"hidden\" name=\"username\" value=\"" + student.getString("username") + "\">");
                    htmlTable.append("<button type=\"submit\">Transfer to Botswana</button>");
                    htmlTable.append("</form>");
                    // Delete button
                    htmlTable.append("<form action=\"DeleteStudent\">");
                    htmlTable.append("<input type=\"hidden\" name=\"username\" value=\"" + student.getString("username") + "\">");
                    htmlTable.append("<button type=\"submit\" style=\"background-color: #dc3545;\">Delete</button>");
                    htmlTable.append("</form>");
                    htmlTable.append("</td>");

                    htmlTable.append("</tr>");
                }
            }

            // Close table tags
            htmlTable.append("</table>");
            htmlTable.append("</body>");
            htmlTable.append("</html>");

            // Send the HTML response back to the client
            response.setContentType("text/html");
            response.getWriter().println(htmlTable);

            // Closing MongoDB connection
            mongoClient.close();
        } catch (Exception e) {
            // Error handling
            response.getWriter().println("An error occurred while retrieving Lesotho students: " + e.getMessage());
        }
    }
}

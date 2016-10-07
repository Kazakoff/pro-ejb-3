package examples.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import examples.stateless.EmployeeService;

public class EmployeeServiceServlet extends HttpServlet {

    private final String TITLE = 
        "Chapter 9: Using SqlResultSetMapping with a Compound Primary Key Example";
    
    private final String DESCRIPTION = 
        "This example demonstates the basics of using SqlResultSetMappings with " +
        "compound primary keys.";

    @EJB EmployeeService service;
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        printHtmlHeader(out);
        
        // process request
        String action = request.getParameter("action");
        if (action == null) {
            // do nothing if no action requested
        } else if (action.equals("Find")) {
            List emps = service.findEmployeeWithManager();
            out.println("Found Employees with Manager: <br/>");
            printCollection(emps, out);
        }
        
        printHtmlFooter(out);
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void printCollection(Collection c, PrintWriter out) {
        for (Object o : c) {
            if (o instanceof Object[]) {
                for (Object o2 : (Object[])o) {
                    out.println(o2 + " : ");
                }
                out.println("<br/>");
            } else {
                out.println(o + "<br/>");
            }
        }
    }

    private void printHtmlHeader(PrintWriter out) throws IOException {
        out.println("<body>");
        out.println("<html>");
        out.println("<head><title>" + TITLE + "</title></head>");
        out.println("<center><h1>" + TITLE + "</h1></center>");
        out.println("<p>" + DESCRIPTION + "</p>");
        out.println("<hr/>");
        out.println("<form action=\"EmployeeServiceServlet\" method=\"POST\">");
        // form to create and Employee 
        out.println("<h3>Find all Employees with Manager </h3>");
        out.println("<table><tbody>");
        out.println("<tr><td><input name=\"action\" type=\"submit\" value=\"Find\"/></td></tr>");
        out.println("</tbody></table>");
        out.println("<hr/>");
    }
    
    
    private void printHtmlFooter(PrintWriter out) throws IOException {
        out.println("</html>");
        out.println("</body>");
        out.close();
    }
}

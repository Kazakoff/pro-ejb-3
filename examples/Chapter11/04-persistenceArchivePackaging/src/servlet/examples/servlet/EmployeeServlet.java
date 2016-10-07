package examples.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.ejb.EJB;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import examples.model.Employee;
import examples.stateless.EmployeeService;

public class EmployeeServlet extends HttpServlet {

    private final String TITLE = 
        "Chapter 11: Entities Packaged in a Persistence Archive";
    
    private final String DESCRIPTION = 
        "This example demonstrates how to package entities in a peristence archive and " +
        "access it from different components.";

    
    @EJB EmployeeService empService;

    @PersistenceContext(unitName="EmployeeService")
    protected EntityManager em;
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        printHtmlHeader(out);
        
        // process request
        String action = request.getParameter("action");
        if (action == null) {
            // do nothing if no action requested
        } else {
            Collection<Employee> emps = null;
            if (action.equals("FindAllJar")) {
                emps = empService.findAllEmployees();
            } else if (action.equals("FindAllWar")) {
                Query query = em.createNamedQuery("Employee.findAll");
                emps = (Collection<Employee>) query.getResultList();
            }

            if (emps.isEmpty()) {
                out.println("No Employees found ");
            } else {
                out.println("Found Employees: </br>");
                for (Employee emp : emps) {
                    out.println(emp + "<br/>");
                    out.println("&nbsp;&nbsp;&nbsp;&nbsp;" + emp.getAddress() + "<br/>");
                    out.println("&nbsp;&nbsp;&nbsp;&nbsp;" + emp.getDepartment() + "<br/>");
                    out.println("&nbsp;&nbsp;&nbsp;&nbsp;Phones: " + emp.getPhones() + "<br/>");
                    out.println("<br/>");
                }
            }
        }
        
        printHtmlFooter(out);
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void printHtmlHeader(PrintWriter out) throws IOException {
        out.println("<body>");
        out.println("<html>");
        out.println("<head><title>" + TITLE + "</title></head>");
        out.println("<center><h1>" + TITLE + "</h1></center>");
        out.println("<p>" + DESCRIPTION + "</p>");
        out.println("<hr/>");
        out.println("<form action=\"EmployeeServlet\" method=\"POST\">");
        // form to find all
        out.println("<h3>Find all</h3>");
        out.println("<input name=\"action\" type=\"submit\" value=\"FindAllJar\"/>");
        out.println("<input name=\"action\" type=\"submit\" value=\"FindAllWar\"/>");
        out.println("</form>");
        out.println("<hr/>");
    }
    
    
    private void printHtmlFooter(PrintWriter out) throws IOException {
        out.println("</html>");
        out.println("</body>");
        out.close();
    }
}

package src.main.java.controller;

import model.Staff;
import src.main.java.dao.StaffDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ListStaffServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ListStaffServlet.class.getName());
    private StaffDAO staffDAO;

    @Override
    public void init() throws javax.servlet.ServletException {
        try {
            staffDAO = new StaffDAO();
        } catch (ClassNotFoundException | SQLException e) {
            throw new javax.servlet.ServletException("Failed to initialize StaffDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String query = request.getParameter("query");
        String searchType = request.getParameter("searchType");

        List<Staff> staffList = null;
        String forwardPath = "listStaff.jsp"; // Default forward path

        try {
            if (query != null && !query.trim().isEmpty() && searchType != null && !searchType.trim().isEmpty()) {
                // Perform search if query and searchType are provided
                staffList = staffDAO.findStaff(query.trim(), searchType.trim());
            } else {
                // If no search query, fetch all staff
                staffList = staffDAO.getAllStaff(); // Call the new getAllStaff method
            }

            request.setAttribute("staffList", staffList);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while listing/searching staff.", e);
            session.setAttribute("errorMessage", "Database error: Could not retrieve staff list.");
            forwardPath = "error.jsp"; // Or another appropriate error page
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while listing/searching staff.", e);
            session.setAttribute("errorMessage", "An unexpected error occurred.");
            forwardPath = "error.jsp"; // Or another appropriate error page
        }

        // Forward to the JSP page
        request.getRequestDispatcher(forwardPath).forward(request, response);
    }

    @Override
    public void destroy() {
        if (staffDAO != null) {
            try {
                staffDAO.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Failed to close database connection.", ex);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unexpected error closing database connection.", ex);
            }
        }
    }
    
    // Note: For the findStaff method in StaffDAO to return all staff when query/type are empty,
    // you might need to adjust its logic or add a dedicated getAllStaff() method.
    // The current findStaff(query, searchType) with empty inputs will return an empty list
    // based on the implementation I provided earlier.
} 
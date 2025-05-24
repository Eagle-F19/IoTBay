package src.main.java.controller;

import src.main.java.dao.StaffDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteStaffServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteStaffServlet.class.getName());
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

        HttpSession session = request.getSession();
        String staffIDParam = request.getParameter("id");
        int staffID = -1; // Default invalid ID

        try {
            staffID = Integer.parseInt(staffIDParam);
            staffDAO.deleteStaff(staffID);

            session.setAttribute("successMessage", "Staff member deleted successfully!");

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid Staff ID format for deletion.", e);
            session.setAttribute("errorMessage", "Invalid staff ID provided for deletion.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during staff deletion.", e);
            session.setAttribute("errorMessage", "Database error: Could not delete staff member.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during staff deletion.", e);
            session.setAttribute("errorMessage", "An unexpected error occurred during deletion.");
        } finally {
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

        // Always redirect back to the staff list page
        response.sendRedirect("listStaff");
    }
} 
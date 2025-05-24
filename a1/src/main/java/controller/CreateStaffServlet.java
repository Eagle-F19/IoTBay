package src.main.java.controller;

import model.Staff;
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

public class CreateStaffServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CreateStaffServlet.class.getName());
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String position = request.getParameter("position");
        String address = request.getParameter("address");

        String errorMessage = "";

        // Basic validation (can be expanded)
        if (name == null || name.trim().isEmpty()) {
            errorMessage += "Name cannot be empty. ";
        }
        if (email == null || email.trim().isEmpty()) {
            errorMessage += "Email cannot be empty. ";
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
             errorMessage += "Invalid email format. ";
        }
         if (position == null || position.trim().isEmpty()) {
            errorMessage += "Position cannot be empty. ";
        }
         if (address == null || address.trim().isEmpty()) {
            errorMessage += "Address cannot be empty. ";
        }

        if (!errorMessage.isEmpty()) {
            session.setAttribute("errorMessage", errorMessage.trim());
            response.sendRedirect("createStaff.jsp"); // Redirect back to the form
            return;
        }

        try {
            // Check if staff with this email already exists (optional but recommended)
            // List<Staff> existingStaff = staffDAO.findStaff(email, "email");
            // if (!existingStaff.isEmpty()) {
            //     session.setAttribute("errorMessage", "Staff with this email already exists.");
            //     response.sendRedirect("createStaff.jsp");
            //     return;
            // }

            Staff newStaff = new Staff(name, email, position, address);
            staffDAO.addStaff(newStaff);

            session.setAttribute("successMessage", "Staff member created successfully!");
            response.sendRedirect("createStaff.jsp"); // Redirect to show success message (or redirect to staff list)

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during staff creation.", e);
            session.setAttribute("errorMessage", "Database error: Could not create staff member.");
            response.sendRedirect("createStaff.jsp");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during staff creation.", e);
            session.setAttribute("errorMessage", "An unexpected error occurred.");
            response.sendRedirect("createStaff.jsp");
        }
    }

    @Override
    public void destroy() {
        if (staffDAO != null) {
            try {
                staffDAO.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Failed to close database connection.", ex);
            }
        }
    }
} 
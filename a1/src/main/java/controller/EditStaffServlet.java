package src.main.java.controller;

import model.Staff;
import src.main.java.dao.StaffDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EditStaffServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EditStaffServlet.class.getName());
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
        String staffIDParam = request.getParameter("id");
        int staffID = -1; // Default invalid ID

        String forwardPath = "editStaff.jsp";

        try {
            staffID = Integer.parseInt(staffIDParam);
            Staff staff = staffDAO.findStaff(staffID);

            if (staff != null) {
                request.setAttribute("staff", staff); // Set staff object as request attribute
            } else {
                session.setAttribute("errorMessage", "Staff member not found.");
                forwardPath = "listStaff"; // Redirect to list if staff not found
            }

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid Staff ID format.", e);
            session.setAttribute("errorMessage", "Invalid staff ID provided.");
            forwardPath = "listStaff"; // Redirect to list on invalid ID
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while fetching staff for edit.", e);
            session.setAttribute("errorMessage", "Database error: Could not load staff details.");
            forwardPath = "error.jsp"; // Or another appropriate error page
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while fetching staff for edit.", e);
            session.setAttribute("errorMessage", "An unexpected error occurred.");
            forwardPath = "error.jsp"; // Or another appropriate error page
        }

        if ("listStaff".equals(forwardPath) || "error.jsp".equals(forwardPath)) {
             // If redirecting, send redirect response
             response.sendRedirect(forwardPath);
        } else {
             // If forwarding to JSP, use RequestDispatcher
             RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
             dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String staffIDParam = request.getParameter("staffID");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String position = request.getParameter("position");
        String address = request.getParameter("address");
        String status = request.getParameter("status");

        int staffID = -1;
        String errorMessage = "";

        try {
            staffID = Integer.parseInt(staffIDParam);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid Staff ID format for update.", e);
            errorMessage += "Invalid staff ID for update. ";
        }

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
            // Redirect back to edit page with messages - Need to include staffID
            response.sendRedirect("editStaff.jsp?id=" + staffIDParam);
            return;
        }

        try {
            Staff updatedStaff = new Staff(staffID, name, email, position, address, status);
            staffDAO.updateStaff(updatedStaff);

            session.setAttribute("successMessage", "Staff member updated successfully!");
            response.sendRedirect("listStaff"); // Redirect to staff list after successful update

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during staff update.", e);
            session.setAttribute("errorMessage", "Database error: Could not update staff member.");
            response.sendRedirect("editStaff.jsp?id=" + staffIDParam);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during staff update.", e);
            session.setAttribute("errorMessage", "An unexpected error occurred.");
            response.sendRedirect("editStaff.jsp?id=" + staffIDParam);
        }
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
} 
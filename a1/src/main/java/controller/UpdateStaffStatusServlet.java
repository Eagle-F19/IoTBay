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

public class UpdateStaffStatusServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UpdateStaffStatusServlet.class.getName());
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

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String staffIDParam = request.getParameter("staffID");
        String newStatus = request.getParameter("status");

        try {
            int staffID = Integer.parseInt(staffIDParam);
            Staff staff = staffDAO.findStaff(staffID);
            
            if (staff != null) {
                staff.setStatus(newStatus);
                staffDAO.updateStaff(staff);
                session.setAttribute("successMessage", "Staff status updated successfully");
            } else {
                session.setAttribute("errorMessage", "Staff member not found");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid Staff ID format.", e);
            session.setAttribute("errorMessage", "Invalid staff ID");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while updating staff status.", e);
            session.setAttribute("errorMessage", "Database error: Could not update staff status");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while updating staff status.", e);
            session.setAttribute("errorMessage", "An unexpected error occurred");
        }

        response.sendRedirect("listStaff");
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
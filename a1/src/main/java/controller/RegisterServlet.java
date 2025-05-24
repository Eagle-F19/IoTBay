package src.main.java.controller;

import model.Customer;
import src.main.java.dao.UserDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
// Removed: import javax.servlet.annotation.WebServlet; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// @WebServlet("/register") // Annotation removed for web.xml configuration
public class RegisterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String name = request.getParameter("fullName");
        String username = request.getParameter("email"); // 使用邮箱作为用户名
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        
        String errorMessage = "";
        if (name == null || name.trim().isEmpty()) {
            errorMessage += "Full Name cannot be empty. ";
        }
        if (email == null || email.trim().isEmpty()) {
            errorMessage += "Email cannot be empty. ";
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            errorMessage += "Invalid email format. ";
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage += "Password cannot be empty. ";
        }

        if (!errorMessage.isEmpty()) {
            session.setAttribute("errorMessage", errorMessage.trim());
            response.sendRedirect("register.jsp"); 
            return;
        }

        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO(); 
            
            Customer customer = new Customer(name, username, email, password, phone, "CUSTOMER");
            userDAO.addUser(customer);
            
            session.setAttribute("successMessage", "Registration successful! Please login.");
            response.sendRedirect("login.jsp"); // Assuming you have login.jsp

        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Database driver class not found.", e);
            session.setAttribute("errorMessage", "Server error: Database driver not found. Please contact support.");
            response.sendRedirect("register.jsp");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during registration.", e);
            if ("23505".equals(e.getSQLState())) { // Derby's unique constraint violation SQLState
                session.setAttribute("errorMessage", "This email is already registered. Please use a different email or login.");
            } else {
                session.setAttribute("errorMessage", "Database error: Could not register user. Please try again later.");
            }
            response.sendRedirect("register.jsp");
        } catch (Exception e) { 
            logger.log(Level.SEVERE, "Unexpected error during registration.", e);
            session.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            response.sendRedirect("register.jsp");
        } finally {
            if (userDAO != null) {
                try {
                    userDAO.close(); 
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Failed to close database connection.", ex);
                }
            }
        }
    }
} 
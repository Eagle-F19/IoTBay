package src.main.java.dao;

import src.main.java.StaffDB.DBConnector;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
// import java.sql.ResultSet; // Temporarily commented out, as addUser doesn't use it directly

public class UserDAO {

    private DBConnector dbConnector;
    private Connection conn; // Holds the connection obtained from DBConnector

    public UserDAO() throws ClassNotFoundException, SQLException {
        this.dbConnector = new DBConnector();
        this.conn = dbConnector.openConnection(); // Obtain connection when UserDAO is instantiated
    }

    /**
     * Adds a new customer to the USERS table.
     *
     * @param customer The customer object containing user details.
     * @throws SQLException if a database access error occurs.
     */
    public void addUser(Customer customer) throws SQLException {
        PreparedStatement pstmt = null;
        // SQL statement uses the USERS table name and columns defined in our DDL
        String sql = "INSERT INTO USERS (FULL_NAME, EMAIL, PASSWORD, PHONE, USER_TYPE) VALUES (?, ?, ?, ?, ?)";

        try {
            // conn was obtained in the constructor
            if (this.conn == null || this.conn.isClosed()) {
                // If connection is invalid or closed, attempt to reopen or throw an exception
                // Simple handling: throw an exception, prompting external code to re-instantiate DAO or handle connection issue
                throw new SQLException("Database connection is closed or invalid.");
            }

            pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPassword()); // Reminder: Password should be stored encrypted
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getUserType()); // e.g., "CUSTOMER"

            pstmt.executeUpdate();

        } finally {
            // Close only the PreparedStatement; Connection closure is managed by the UserDAO's caller via the close() method
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Or log the exception
                }
            }
        }
    }

    // Other DAO methods (findUserByEmail, checkUser, updateUser, deleteUser, etc.) will be added here

    /**
     * Closes the database connection held by this DAO instance.
     * Should be called when the DAO is no longer needed, typically by the service layer or servlet.
     */
    public void close() throws SQLException {
        if (this.dbConnector != null) {
            this.dbConnector.closeConnection(); // This will close the connection created in DBConnector
        }
        // Alternatively, if conn were obtained directly via DriverManager, then conn.close() directly
        // Current design is that dbConnector is responsible for closing the connection it created
    }
} 

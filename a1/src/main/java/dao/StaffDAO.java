package src.main.java.dao;

import src.main.java.StaffDB.DBConnector;
import model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    private DBConnector dbConnector;
    private Connection conn;

    // Default constructor for application use
    public StaffDAO() throws ClassNotFoundException, SQLException {
        this.dbConnector = new DBConnector();
        this.conn = dbConnector.openConnection();
        System.out.println(System.getProperty("user.dir"));
    }

    // Constructor for testing, accepts an existing connection
    public StaffDAO(Connection conn) {
        this.conn = conn;
        this.dbConnector = null; // Not needed when connection is provided
    }

    // Add a new staff member to the database
    public void addStaff(Staff staff) throws SQLException {
        // SQL INSERT statement - added STATUS
        String sql = "INSERT INTO STAFF (NAME, EMAIL, POSITION, ADDRESS, STATUS) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getEmail());
            pstmt.setString(3, staff.getPosition());
            pstmt.setString(4, staff.getAddress());
            pstmt.setString(5, staff.getStatus()); // Set status
            pstmt.executeUpdate();
        }
    }

    // Find a staff member by ID
    public Staff findStaff(int staffID) throws SQLException {
        // SQL SELECT statement - added STATUS
        String sql = "SELECT * FROM STAFF WHERE STAFFID = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("NAME");
                    String email = rs.getString("EMAIL");
                    String position = rs.getString("POSITION");
                    String address = rs.getString("ADDRESS");
                    String status = rs.getString("STATUS"); // Get status
                    return new Staff(staffID, name, email, position, address, status); // Use constructor with status
                }
            }
        }
        return null; // Staff not found
    }

    // Find staff members by name, position, email, or status
    public List<Staff> findStaff(String query, String searchType) throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        // SQL SELECT statement - added STATUS
        String sql = "SELECT * FROM STAFF WHERE ";
        if ("name".equalsIgnoreCase(searchType)) {
            sql += "NAME LIKE ?";
        } else if ("position".equalsIgnoreCase(searchType)) {
            sql += "POSITION LIKE ?";
        } else if ("email".equalsIgnoreCase(searchType)) {
            sql += "EMAIL LIKE ?";
        } else if ("status".equalsIgnoreCase(searchType)) { // Added search by status
            sql += "STATUS LIKE ?";
        } else {
            // Default or invalid search type, return empty list
            return staffList;
        }

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            // Use % for LIKE query to find partial matches (except for status)
            if ("status".equalsIgnoreCase(searchType)) {
                 pstmt.setString(1, query); // Exact match for status
            } else {
                 pstmt.setString(1, "%" + query + "%"); // Partial match for others
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int staffID = rs.getInt("STAFFID");
                    String name = rs.getString("NAME");
                    String email = rs.getString("EMAIL");
                    String position = rs.getString("POSITION");
                    String address = rs.getString("ADDRESS");
                    String status = rs.getString("STATUS"); // Get status
                    staffList.add(new Staff(staffID, name, email, position, address, status)); // Use constructor with status
                }
            }
        }
        return staffList;
    }
    
    // Update staff member details
    public void updateStaff(Staff staff) throws SQLException {
        // SQL UPDATE statement - added STATUS
        String sql = "UPDATE STAFF SET NAME = ?, EMAIL = ?, POSITION = ?, ADDRESS = ?, STATUS = ? WHERE STAFFID = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getEmail());
            pstmt.setString(3, staff.getPosition());
            pstmt.setString(4, staff.getAddress());
            pstmt.setString(5, staff.getStatus()); // Set status
            pstmt.setInt(6, staff.getStaffID());
            pstmt.executeUpdate();
        }
    }

    // Delete a staff member by ID
    public void deleteStaff(int staffID) throws SQLException {
        String sql = "DELETE FROM STAFF WHERE STAFFID = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffID);
            pstmt.executeUpdate();
        }
    }

    // Get all staff members
    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        // SQL SELECT statement - added STATUS
        String sql = "SELECT * FROM STAFF";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int staffID = rs.getInt("STAFFID");
                    String name = rs.getString("NAME");
                    String email = rs.getString("EMAIL");
                    String position = rs.getString("POSITION");
                    String address = rs.getString("ADDRESS");
                    String status = rs.getString("STATUS"); // Get status
                    staffList.add(new Staff(staffID, name, email, position, address, status)); // Use constructor with status
                }
            }
        }
        return staffList;
    }

    // Close the database connection
    public void close() throws SQLException {
        if (this.dbConnector != null) {
            this.dbConnector.closeConnection();
        }
    }

    // Helper method to get a connection (primarily for application use, less for this test setup style)
    public Connection getConn() {
        return conn;
    }
}
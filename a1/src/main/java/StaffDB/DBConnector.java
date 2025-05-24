package src.main.java.staffdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class DBConnector {

    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:IoTBayDB.db";
    private static final String DB_USER = ""; // SQLite doesn't use username/password
    private static final String DB_PASSWORD = "";

    private Connection conn = null;

    public DBConnector() throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        checkAndInitializeDatabase();
    }

    private void checkAndInitializeDatabase() throws SQLException {
        try {
            // Check if the USERS table exists
            boolean usersTableExists = false;
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "USERS", null)) {
                usersTableExists = rs.next();
            }

            if (!usersTableExists) {
                // Create User Table
                String createTableSQL = 
                    "CREATE TABLE USERS (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "FULL_NAME VARCHAR(100) NOT NULL, " +
                    "EMAIL VARCHAR(100) NOT NULL UNIQUE, " +
                    "PASSWORD VARCHAR(100) NOT NULL, " +
                    "PHONE VARCHAR(20), " +
                    "USER_TYPE VARCHAR(20) NOT NULL, " +
                    "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSQL);
                    System.out.println("Users table created successfully");
                    
                    // Create index
                    String createIndexSQL = "CREATE INDEX IDX_USERS_EMAIL ON USERS(EMAIL)";
                    stmt.execute(createIndexSQL);
                    System.out.println("Email index created successfully");
                }
            }

            // Check if the STAFF table exists
            boolean staffTableExists = false;
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "STAFF", null)) {
                staffTableExists = rs.next();
            }

            if (!staffTableExists) {
                // Create the staff table
                String createStaffTableSQL = 
                    "CREATE TABLE STAFF (" +
                    "STAFFID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME VARCHAR(255) NOT NULL, " +
                    "EMAIL VARCHAR(255) NOT NULL UNIQUE, " +
                    "POSITION VARCHAR(255), " +
                    "ADDRESS VARCHAR(255), " +
                    "STATUS VARCHAR(50) DEFAULT 'active')";
                
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createStaffTableSQL);
                    System.out.println("Staff table created successfully");
                    
                    // Create Index
                    String createStaffIndexSQL = "CREATE INDEX IDX_STAFF_EMAIL ON STAFF(EMAIL)";
                    stmt.execute(createStaffIndexSQL);
                    System.out.println("Staff email index created successfully");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to initialize database", e);
        }
    }

    public Connection openConnection() {
        return this.conn;
    }

    public void closeConnection() throws SQLException {
        if (this.conn != null && !this.conn.isClosed()) {
            this.conn.close();
        }
    }

    // Helper method to close statement
    public static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to close result set
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to close connection, statement, and result set
    public static void close(Connection con, Statement st, ResultSet rs) {
        close(rs);
        close(st);
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 
package src.main.java.staffdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DBInitializer {
    public static void main(String[] args) {
        try {
            // Loading the SQLite driver
            Class.forName("org.sqlite.JDBC");
            
            // Create database connection
            String dbURL = "jdbc:sqlite:IoTBayDB.db";
            Connection conn = DriverManager.getConnection(dbURL);
            
            // Create user table
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
                
                // Create Index
                String createIndexSQL = "CREATE INDEX IDX_USERS_EMAIL ON USERS(EMAIL)";
                stmt.execute(createIndexSQL);
                System.out.println("Email index created successfully");
            }
            
            // Close connection
            conn.close();
            System.out.println("Database initialization completed successfully");
            
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
} 
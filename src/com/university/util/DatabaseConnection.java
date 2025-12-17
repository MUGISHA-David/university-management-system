package com.university.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Utility using Singleton Pattern.
 * Manages PostgreSQL database connections.
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/university_management_system_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    
    private static Connection connection = null;
    
    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Gets a connection to the database.
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

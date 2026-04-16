package com.moviebooking.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.moviebooking.exception.DatabaseConnectionException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/movie_booking_db"
                                    + "?autoReconnect=true&useSSL=false";
    private static final String USER     = "root";
    private static final String PASSWORD = "Rudra@2006"; // <-- your password here
    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() throws DatabaseConnectionException {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                //System.out.println("[DB] Connected to MySQL successfully.");
            }
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException(
                "MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                "Failed to connect: " + e.getMessage(), e);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("[DB] Error closing: " + e.getMessage());
            }
        }
    }
}
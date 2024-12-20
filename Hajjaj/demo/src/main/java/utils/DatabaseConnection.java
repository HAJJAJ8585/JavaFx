package utils;

import models.User;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bb3"; // Update with your database URL
    private static final String USER = "root"; // Replace with your username
    private static final String PASSWORD = ""; // Replace with your password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Registered!");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
        return null;
    }

    public static User getUserByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM user_account WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (connection == null) {
                throw new SQLException("Failed to establish a database connection.");
            }

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User(username, password);
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by username and password: " + e.getMessage());
        }
        return null;
    }
}

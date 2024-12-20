package database_access;

import models.Car;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarAdd {

    // Method to fetch all cars from the database
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars"; // Ensure table name matches your schema

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setMake(rs.getString("Make"));
                car.setModel(rs.getString("Model"));
                car.setYear(rs.getInt("Year"));
                car.setPrice(rs.getDouble("Price"));
                car.setStock(rs.getInt("Stock"));
                car.setVin(rs.getString("VIN"));
                cars.add(car);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching cars: " + e.getMessage());
        }
        return cars;
    }

    // Method to add a new car to the database
    public void addCar(Car car) {
        String query = "INSERT INTO cars (Make, Model, Year, Price, Stock, VIN) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, car.getMake());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setDouble(4, car.getPrice());
            stmt.setInt(5, car.getStock());
            stmt.setString(6, car.getVin());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding car: " + e.getMessage());
        }
    }

    // Method to search for cars by make or model
    public List<Car> searchCars(String searchTerm) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars WHERE Make LIKE ? OR Model LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String likeSearchTerm = "%" + searchTerm + "%";
            stmt.setString(1, likeSearchTerm);
            stmt.setString(2, likeSearchTerm);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setMake(rs.getString("Make"));
                car.setModel(rs.getString("Model"));
                car.setYear(rs.getInt("Year"));
                car.setPrice(rs.getDouble("Price"));
                car.setStock(rs.getInt("Stock"));
                car.setVin(rs.getString("VIN"));
                cars.add(car);
            }
        } catch (SQLException e) {
            System.err.println("Error searching cars: " + e.getMessage());
        }
        return cars;
    }
}

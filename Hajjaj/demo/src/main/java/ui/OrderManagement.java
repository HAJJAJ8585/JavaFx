package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Order;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManagement {

    // Method to set the background image as cover
    public static void setBackgroundImage(StackPane root) {
        // Path to your background image (cover image)
        String imagePath = "file:/C:/Users/Admin/Desktop/Dbase/Hajjaj/demo/src/main/java/ui/resources/images/loginbg.jpg";

        // Load the image
        Image image = new Image(imagePath);

        // Create a BackgroundImage with the desired settings
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false)
        );

        // Set the background for the root pane
        root.setBackground(new Background(backgroundImage));
    }

    // Method to refresh the order table
    public static void refreshOrder(TableView<Order> orderTable) {
        orderTable.getItems().clear();
        String query = "SELECT * FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            ObservableList<Order> orderList = FXCollections.observableArrayList();
            while (rs.next()) {
                orderList.add(new Order(
                        rs.getInt("OrderID"),
                        rs.getString("OrderDate"),
                        rs.getInt("CarID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("Quantity"),
                        rs.getDouble("TotalPrice")
                ));
            }
            orderTable.setItems(orderList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to apply filters to the order table
    public static void applyFilters(TableView<Order> orderTable, ObservableList<Order> orderList, TextField... filters) {
        for (TextField filter : filters) {
            filter.textProperty().addListener((observable, oldValue, newValue) ->
                    filterOrders(orderTable, orderList, filters));
        }
    }

    // Method to filter orders based on user input
    public static void filterOrders(TableView<Order> orderTable, ObservableList<Order> orderList, TextField[] filters) {
        String orderidFilter = filters[0].getText();
        String orderdateFilter = filters[1].getText();
        String carIdFilter = filters[2].getText();
        String customerIdFilter = filters[3].getText();
        String employeeidFilter = filters[4].getText();
        String quantityFilter = filters[5].getText();
        String totalPriceFilter = filters[6].getText();

        List<Order> filteredOrder = new ArrayList<>();
        for (Order order : orderList) {
            if (matchesFilters(order, orderidFilter, orderdateFilter, carIdFilter, customerIdFilter, employeeidFilter, quantityFilter, totalPriceFilter)) {
                filteredOrder.add(order);
            }
        }

        orderTable.setItems(FXCollections.observableArrayList(filteredOrder));
    }

    // Method to check if an order matches the filters
    private static boolean matchesFilters(Order order, String orderId, String orderDate, String carId, String customerId, String employeeId, String quantity, String totalPrice) {
        return (orderId.isEmpty() || Integer.toString(order.getOrderID()).contains(orderId)) &&
                (orderDate.isEmpty() || order.getOrderDate().contains(orderDate)) &&
                (carId.isEmpty() || Integer.toString(order.getCarID()).contains(carId)) &&
                (customerId.isEmpty() || Integer.toString(order.getCustomerID()).contains(customerId)) &&
                (employeeId.isEmpty() || Integer.toString(order.getEmployeeID()).contains(employeeId)) &&
                (quantity.isEmpty() || Integer.toString(order.getQuantity()).contains(quantity)) &&
                (totalPrice.isEmpty() || Double.toString(order.getTotalPrice()).contains(totalPrice));
    }

    // Method to add an order
    public void addOrder(int orderID, String orderDate, int carID, int customerID, int employeeID, int quantity, double totalPrice) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO orders (OrderID, OrderDate, CarID, CustomerID, EmployeeID, Quantity, TotalPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, orderID);
            pstmt.setString(2, orderDate);
            pstmt.setInt(3, carID);
            pstmt.setInt(4, customerID);
            pstmt.setInt(5, employeeID);
            pstmt.setInt(6, quantity);
            pstmt.setDouble(7, totalPrice);
            pstmt.executeUpdate();
            System.out.println("Order added successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to update an order
    public void updateOrder(int orderID, String orderDate, int carID, int customerID, int employeeID, int quantity, double totalPrice) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE orders SET OrderDate = ?, CarID = ?, CustomerID = ?, EmployeeID = ?, Quantity = ?, TotalPrice = ? WHERE OrderID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, orderDate);
            pstmt.setInt(2, carID);
            pstmt.setInt(3, customerID);
            pstmt.setInt(4, employeeID);
            pstmt.setInt(5, quantity);
            pstmt.setDouble(6, totalPrice);
            pstmt.setInt(7, orderID);
            pstmt.executeUpdate();
            System.out.println("Order updated successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to delete an order
    public void deleteOrder(int orderID) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM orders WHERE OrderID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, orderID);
            pstmt.executeUpdate();
            System.out.println("Order deleted successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to clear the text fields
    public void clearTextFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    // JavaFX main application class
    public static class OrderManagementApp extends javafx.application.Application {

        @Override
        public void start(Stage primaryStage) {
            // Create a StackPane or any other container
            StackPane root = new StackPane();
            
            // Set background image
            setBackgroundImage(root);

            // Create TableView for orders
            TableView<Order> orderTable = new TableView<>();
            refreshOrder(orderTable);

            // Other UI components can be added here

            // Create the scene and set it on the stage
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Order Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
}

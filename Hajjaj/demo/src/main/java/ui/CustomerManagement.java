package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Customer;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerManagement {

    // Refresh the customer table with data from the database
    public static void refreshCustomerTable(TableView<Customer> customerTable) {
        customerTable.getItems().clear();

        String query = "SELECT * FROM customers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ObservableList<Customer> customers = FXCollections.observableArrayList();

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("State"),
                        rs.getString("ZipCode")
                );
                customers.add(customer);
            }
            customerTable.setItems(customers);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Update customer in the database
    public void updateCustomer(int customerID, String firstName, String lastName, String email, String phone, String address, String city, String state, String zipCode) {
        String updateQuery = "UPDATE customers SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, Address = ?, City = ?, State = ?, ZipCode = ? WHERE CustomerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, city);
            pstmt.setString(7, state);
            pstmt.setString(8, zipCode);
            pstmt.setInt(9, customerID);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Apply filters to the customer table
    public static void applyFilters(TableView<Customer> customerTable, ObservableList<Customer> customerList, TextField... filters) {
        for (TextField filter : filters) {
            filter.textProperty().addListener((observable, oldValue, newValue) ->
                    filterCustomers(customerTable, customerList, filters));
        }
    }

    public static void filterCustomers(TableView<Customer> customerTable, ObservableList<Customer> customerList, TextField[] filters) {
        String idFilter = filters[0].getText();
        String firstNameFilter = filters[1].getText();
        String lastNameFilter = filters[2].getText();
        String emailFilter = filters[3].getText();
        String phoneFilter = filters[4].getText();

        List<Customer> filteredCustomers = new ArrayList<>();
        for (Customer customer : customerList) {
            if (matchesFilters(customer, idFilter, firstNameFilter, lastNameFilter, emailFilter, phoneFilter)) {
                filteredCustomers.add(customer);
            }
        }

        customerTable.setItems(FXCollections.observableArrayList(filteredCustomers));
    }

    private static boolean matchesFilters(Customer customer, String id, String firstName, String lastName, String email, String phone) {
        return (id.isEmpty() || Integer.toString(customer.getCustomerID()).contains(id)) &&
                (firstName.isEmpty() || customer.getFirstName().toLowerCase().contains(firstName.toLowerCase())) &&
                (lastName.isEmpty() || customer.getLastName().toLowerCase().contains(lastName.toLowerCase())) &&
                (email.isEmpty() || customer.getEmail().toLowerCase().contains(email.toLowerCase())) &&
                (phone.isEmpty() || customer.getPhone().contains(phone));
    }

    // Delete customer from the database
    public void deleteCustomer(int customerID) {
        String deleteQuery = "DELETE FROM customers WHERE CustomerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, customerID);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Clear text fields
    public void clearTextFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
}

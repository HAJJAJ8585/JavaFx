package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Service;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceManagement {

    public static void refreshServiceTable(TableView<Service> serviceTable) {
        serviceTable.getItems().clear();
        String query = "SELECT * FROM services"; 
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            ObservableList<Service> services = FXCollections.observableArrayList();
            while (rs.next()) {
                Service service = new Service(
                        rs.getInt("ServiceID"),
                        rs.getInt("CarID"),
                        rs.getInt("CustomerID"),
                        rs.getString("ServiceDate"),
                        rs.getString("ServiceDescription"),
                        rs.getDouble("Cost")
                );
                services.add(service);
            }
            serviceTable.setItems(services);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void applyFilters(TableView<Service> serviceTable, ObservableList<Service> serviceList, TextField... filters) {
        for (TextField filter : filters) {
            filter.textProperty().addListener((observable, oldValue, newValue) ->
                    filterServices(serviceTable, serviceList, filters));
        }
    }

    public static void filterServices(TableView<Service> serviceTable, ObservableList<Service> serviceList, TextField[] filters) {
        String serviceIdFilter = filters[0].getText();
        String carIdFilter = filters[1].getText();
        String customerIdFilter = filters[2].getText();
        String serviceDateFilter = filters[3].getText();
        String descriptionFilter = filters[4].getText();
        String costFilter = filters[5].getText();

        List<Service> filteredServices = new ArrayList<>();
        for (Service service : serviceList) {
            if (matchesFilters(service, serviceIdFilter, carIdFilter, customerIdFilter, serviceDateFilter, descriptionFilter, costFilter)) {
                filteredServices.add(service);
            }
        }

        serviceTable.setItems(FXCollections.observableArrayList(filteredServices));
    }

    private static boolean matchesFilters(Service service, String serviceId, String carId, String customerId, String serviceDate, String description, String cost) {
        return (serviceId.isEmpty() || Integer.toString(service.getServiceID()).contains(serviceId)) &&
                (carId.isEmpty() || Integer.toString(service.getCarID()).contains(carId)) &&
                (customerId.isEmpty() || Integer.toString(service.getCustomerID()).contains(customerId)) &&
                (serviceDate.isEmpty() || service.getServiceDate().contains(serviceDate)) &&
                (description.isEmpty() || service.getServiceDescription().contains(description)) &&
                (cost.isEmpty() || Double.toString(service.getCost()).contains(cost));
    }

    public void updateService(int serviceID, int carID, int customerID, String serviceDate, String serviceDescription, double cost) {
        String updateQuery = "UPDATE services SET CarID = ?, CustomerID = ?, ServiceDate = ?, ServiceDescription = ?, Cost = ? WHERE ServiceID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setInt(1, carID);
            pstmt.setInt(2, customerID);
            pstmt.setString(3, serviceDate);
            pstmt.setString(4, serviceDescription);
            pstmt.setDouble(5, cost);
            pstmt.setInt(6, serviceID);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteService(int serviceID) {
        String deleteQuery = "DELETE FROM services WHERE ServiceID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, serviceID);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void clearTextFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
}

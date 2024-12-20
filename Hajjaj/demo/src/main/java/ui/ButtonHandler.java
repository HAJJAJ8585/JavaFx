package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Car;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ButtonHandler {

    public static void setupCarButton(TableView<Car> carTable, ObservableList<Car> carList) {
        // Define columns
        TableColumn<Car, Integer> colCarID = new TableColumn<>("Car ID");
        colCarID.setCellValueFactory(new PropertyValueFactory<>("carId"));

        TableColumn<Car, String> colMake = new TableColumn<>("Make");
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));

        TableColumn<Car, String> colModel = new TableColumn<>("Model");
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Car, Integer> colYear = new TableColumn<>("Year");
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Car, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Car, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Car, String> colVIN = new TableColumn<>("VIN");
        colVIN.setCellValueFactory(new PropertyValueFactory<>("vin"));

        carTable.getColumns().addAll(colCarID, colMake, colModel, colYear, colPrice, colStock, colVIN);
        carTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        carTable.setPrefSize(750, 400);

        carTable.setRowFactory(tv -> {
            TableRow<Car> row = new TableRow<>();
            row.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1); -fx-border-color: white; -fx-text-fill: white;");
            return row;
        });

        // Load data from the database
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cars")) {

            while (rs.next()) {
                carList.add(new Car(
                        rs.getInt("CarID"),
                        rs.getString("Make"),
                        rs.getString("Model"),
                        rs.getInt("Year"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("VIN")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        carTable.setItems(carList);
    }

    public static HBox createInputFields(TextField... fields) {
        for (TextField field : fields) {
            field.setPadding(new Insets(5));
        }
        HBox inputFields = new HBox(10, fields);
        inputFields.setPadding(new Insets(10));
        return inputFields;
    }

    public static HBox createActionButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-padding: 5 10;");
        }
        HBox actionButtons = new HBox(10, buttons);
        actionButtons.setPadding(new Insets(10));
        return actionButtons;
    }

    public static void clearTextFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    public static void refreshCarTable(TableView<Car> carTable) {
        carTable.refresh();
    }
}

package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.DatabaseConnection;
import models.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarManagement {
    // Method to refresh the car table from the database
    public static void refreshCarTable(TableView<Car> carTable) {
        ObservableList<Car> carList = FXCollections.observableArrayList();
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

    public static void updateCar(int carID, String make, String model, int year, double price, int stock, String vin) {
        String updateQuery = "UPDATE cars SET Make = ?, Model = ?, Year = ?, Price = ?, Stock = ?, VIN = ? WHERE CarID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, make);
            pstmt.setString(2, model);
            pstmt.setInt(3, year);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, stock);
            pstmt.setString(6, vin);
            pstmt.setInt(7, carID);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void applyFilters(TableView<Car> carTable, ObservableList<Car> carList, TextField... filters) {
        filters[0].textProperty().addListener((observable, oldValue, newValue) -> filterCars(carTable, carList, filters));
        filters[1].textProperty().addListener((observable, oldValue, newValue) -> filterCars(carTable, carList, filters));
        filters[2].textProperty().addListener((observable, oldValue, newValue) -> filterCars(carTable, carList, filters));
        filters[3].textProperty().addListener((observable, oldValue, newValue) -> filterCars(carTable, carList, filters));
        filters[4].textProperty().addListener((observable, oldValue, newValue) -> filterCars(carTable, carList, filters));
        filters[5].textProperty().addListener((observable, oldValue, newValue) -> filterCars(carTable, carList, filters));
        filters[6].textProperty().addListener((observable, oldValue, newValue) -> filterCars(carTable, carList, filters));
    }

    private static void filterCars(TableView<Car> carTable, ObservableList<Car> carList, TextField[] filters) {
        String carIdFilter = filters[0].getText();
        String makeFilter = filters[1].getText();
        String modelFilter = filters[2].getText();
        String yearFilter = filters[3].getText();
        String priceFilter = filters[4].getText();
        String stockFilter = filters[5].getText();
        String vinFilter = filters[6].getText();

        List<Car> filteredCars = new ArrayList<>();
        for (Car car : carList) {
            if (matchesFilters(car, carIdFilter, makeFilter, modelFilter, yearFilter, priceFilter, stockFilter, vinFilter)) {
                filteredCars.add(car);
            }
        }

        carTable.setItems(FXCollections.observableArrayList(filteredCars));
    }

    private static boolean matchesFilters(Car car, String carId, String make, String model, String year, String price, String stock, String vin) {
        return (carId.isEmpty() || Integer.toString(car.getcarId()).contains(carId)) &&
                (make.isEmpty() || car.getMake().toLowerCase().contains(make.toLowerCase())) &&
                (model.isEmpty() || car.getModel().toLowerCase().contains(model.toLowerCase())) &&
                (year.isEmpty() || Integer.toString(car.getYear()).contains(year)) &&
                (price.isEmpty() || Double.toString(car.getPrice()).contains(price)) &&
                (stock.isEmpty() || Integer.toString(car.getStock()).contains(stock)) &&
                (vin.isEmpty() || car.getVin().toLowerCase().contains(vin.toLowerCase()));
    }

    private static void clearTextFields(TextField txtCarId, TextField txtMake, TextField txtModel,
                                        TextField txtYear, TextField txtPrice, TextField txtStock, TextField txtVIN) {
        txtCarId.clear();
        txtMake.clear();
        txtModel.clear();
        txtYear.clear();
        txtPrice.clear();
        txtStock.clear();
        txtVIN.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // This method can be used to create the layout and apply the background image
    public static void applyBackgroundToLayout(BorderPane layout) {
        String imagePath = "file:/C:/Users/Admin/Desktop/Dbase/Hajjaj/demo/src/main/java/ui/resources/images/dashboard1bg.jpg"; // Image path
        Image backgroundImage = new Image(imagePath);

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );
        layout.setBackground(new Background(bgImage));
    }
}

package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.beans.property.*;

import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Displays search results for a given query.
 * Make sure that the resources (images, CSS) are adjusted as needed.
 */
public class SearchResultPage {
    private String query;

    public SearchResultPage(String query) {
        this.query = query;
    }

    public void showNewWindow() {
        Stage stage = new Stage();
        stage.setTitle("Search Results");

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            homepage homePage = new homepage();
            homePage.start(new Stage());
            stage.close();
        });

        Label headerLabel = new Label("Search Results");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter your search query...");
        searchBar.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String newQuery = searchBar.getText().trim();
            if (!newQuery.isEmpty()) {
                stage.close();
                SearchResultPage resultPage = new SearchResultPage(newQuery);
                resultPage.showNewWindow();
            }
        });

        HBox leftHeaderBox = new HBox(10, backButton, headerLabel);
        leftHeaderBox.setAlignment(Pos.CENTER_LEFT);

        HBox rightHeaderBox = new HBox(10, searchBar, searchButton);
        rightHeaderBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane header = new BorderPane();
        header.setLeft(leftHeaderBox);
        header.setRight(rightHeaderBox);
        header.setPadding(new Insets(10));
        header.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.5);");

        TableView<Car> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Car, Integer> carIdCol = new TableColumn<>("Car ID");
        carIdCol.setCellValueFactory(cellData -> cellData.getValue().carIdProperty().asObject());

        TableColumn<Car, String> makeCol = new TableColumn<>("Make");
        makeCol.setCellValueFactory(cellData -> cellData.getValue().makeProperty());

        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(cellData -> cellData.getValue().modelProperty());

        TableColumn<Car, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());

        TableColumn<Car, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        table.getColumns().addAll(carIdCol, makeCol, modelCol, yearCol, priceCol);

        ObservableList<Car> cars = searchDatabase(query);
        table.setItems(cars);

        VBox resultsArea = new VBox(20, table);
        resultsArea.setAlignment(Pos.CENTER);
        resultsArea.setPadding(new Insets(20));

        if (cars.isEmpty()) {
            Label noResultsLabel = new Label("No results found");
            noResultsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
            resultsArea.getChildren().add(noResultsLabel);
        }

        BorderPane layout = new BorderPane();
        layout.setTop(header);
        layout.setCenter(resultsArea);

        // Apply the background image as a cover
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

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private ObservableList<Car> searchDatabase(String query) {
        ObservableList<Car> cars = FXCollections.observableArrayList();
        if (query == null || query.trim().isEmpty()) {
            return cars;
        }

        String sql = "SELECT * FROM cars WHERE Make LIKE ? OR Model LIKE ? OR Year LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("CarID"),
                        rs.getString("Make"),
                        rs.getString("Model"),
                        rs.getInt("Year"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("VIN")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public static class Car {
        private final IntegerProperty carId;
        private final StringProperty make;
        private final StringProperty model;
        private final IntegerProperty year;
        private final DoubleProperty price;

        public Car(int carId, String make, String model, int year, double price, int stock, String vin) {
            this.carId = new SimpleIntegerProperty(carId);
            this.make = new SimpleStringProperty(make);
            this.model = new SimpleStringProperty(model);
            this.year = new SimpleIntegerProperty(year);
            this.price = new SimpleDoubleProperty(price);
        }

        public IntegerProperty carIdProperty() { return carId; }
        public StringProperty makeProperty() { return make; }
        public StringProperty modelProperty() { return model; }
        public IntegerProperty yearProperty() { return year; }
        public DoubleProperty priceProperty() { return price; }
    }
}

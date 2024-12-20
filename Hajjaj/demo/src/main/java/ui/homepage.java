package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The homepage of the Car Shop Management System.
 * Displays a header, a login/dashboard button, and a search feature.
 */
public class homepage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Header
        Label header = new Label("Car Shop Management System");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10; -fx-text-fill: white; -fx-background-color: black;");
        header.setMaxWidth(Double.MAX_VALUE);

        Button btnLogin = new Button(SessionManager.isLoggedIn() ? "Dashboard" : "Login");
        String buttonStyle = "-fx-font-size: 14px; -fx-padding: 5; -fx-background-color: black; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-color: white;";
        btnLogin.setStyle(buttonStyle);
        btnLogin.setOnAction(e -> {
            if (SessionManager.isLoggedIn()) {
                // If logged in, go to Dashboard
                dashboardpage dashboardPage = new dashboardpage();
                try {
                    dashboardPage.start(new Stage());
                    primaryStage.hide();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // If not logged in, go to Login Page
                Loginpage loginPage = new Loginpage();
                try {
                    loginPage.start(new Stage());
                    primaryStage.hide();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        HBox headerBox = new HBox(10, header, btnLogin);
        headerBox.setStyle("-fx-background-color: black;");
        headerBox.setPadding(new Insets(10));
        HBox.setHgrow(header, Priority.ALWAYS);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Search Content
        VBox contentArea = new VBox(20);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(10));

        Label searchLabel = new Label("Search");
        searchLabel.setStyle("-fx-font-size: 20px;-fx-font-weight: bold; -fx-text-fill: white;");
        searchLabel.setPadding(new Insets(50, 0, 0, 0));

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter your search query...");
        searchBar.setStyle(
                "-fx-font-size: 15px; " +
                "-fx-padding: 5; " +
                "-fx-border-radius: 20; " +
                "-fx-background-radius: 20; " +
                "-fx-border-color: white; " +
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                "-fx-text-fill: white; " +
                "-fx-prompt-text-fill: gray;"
        );
        searchBar.setMaxWidth(450);
        searchBar.setPadding(new Insets(10, 0, 0, 0));

        Button searchButton = new Button("Search");
        searchButton.setStyle(
                "-fx-font-size: 12px; " +
                "-fx-padding: 4; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: white; " +
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                "-fx-text-fill: white; "
        );
        searchButton.setOnAction(e -> {
            String query = searchBar.getText().trim();
            if (!query.isEmpty()) {
                SearchResultPage resultPage = new SearchResultPage(query);
                resultPage.showNewWindow();
                primaryStage.close();
            }
        });

        searchBox.getChildren().addAll(searchBar, searchButton);
        contentArea.getChildren().addAll(searchLabel, searchBox);

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(contentArea);

        // Set the background image as a cover
        String imagePath = "file:/C:/Users/Admin/Desktop/Dbase/Hajjaj/demo/src/main/java/ui/resources/images/loginbg.jpg"; // Image path
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

        Scene scene = new Scene(layout, 1000, 600);
        primaryStage.setTitle("Car Shop Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

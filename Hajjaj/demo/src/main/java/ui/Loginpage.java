package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.User;
import utils.DatabaseConnection;

public class Loginpage extends Application {

    @Override
    public void start(Stage primaryStage) {
        String buttonStyle = "-fx-font-size: 14px; -fx-padding: 5; -fx-border-color: white; -fx-background-color: rgba(0, 0, 0, 0.6);"
                + "-fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-color: white;";

        Button backButton = new Button("Back");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> {
            homepage homePage = new homepage();
            homePage.start(new Stage());
            primaryStage.close();
        });

        Label headerLabel = new Label("Login Page");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox leftHeaderBox = new HBox(10, headerLabel);
        leftHeaderBox.setAlignment(Pos.CENTER_LEFT);

        HBox rightHeaderBox = new HBox(10, backButton);
        rightHeaderBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane header = new BorderPane();
        header.setLeft(leftHeaderBox);
        header.setRight(rightHeaderBox);
        header.setPadding(new Insets(10));

        // Set Image as Background
        String imagePath = "file:C:/Users/Admin/Desktop/Dbase/Hajjaj/demo/src/main/java/ui/resources/images/bg.jpg";
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(false);

        BorderPane layout = new BorderPane();
        layout.getChildren().add(imageView);
        layout.setTop(header);

        // Rectangle with Shadow
        Rectangle rectangle = new Rectangle(400, 400);
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        // Adding DropShadow Effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setOffsetX(4);
        dropShadow.setOffsetY(4);
        dropShadow.setRadius(10);
        rectangle.setEffect(dropShadow);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        String labelStyle = "-fx-font-size: 15px;-fx-font-weight: bold; -fx-text-fill: white;";
        String textfieldStyle = "-fx-font-size: 15px; -fx-padding: 5; -fx-border-radius: 20; -fx-background-radius: 20; "
                + "-fx-border-color: white; -fx-background-color: rgba(0, 0, 0, 0.8); -fx-text-fill: white; "
                + "-fx-prompt-text-fill: gray;";

        Label lblUsername = new Label("Username:");
        lblUsername.setStyle(labelStyle);
        TextField txtUsername = new TextField();
        txtUsername.setStyle(textfieldStyle);
        txtUsername.setPromptText("Enter your username");

        Label lblPassword = new Label("Password:");
        lblPassword.setStyle(labelStyle);
        PasswordField txtPassword = new PasswordField();
        txtPassword.setStyle(textfieldStyle);
        txtPassword.setPromptText("Enter your password");

        Button btnLogin = new Button("Login");
        btnLogin.setStyle(buttonStyle);
        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            User user = DatabaseConnection.getUserByUsernameAndPassword(username, password);

            if (user != null) {
                // Show progress bar
                Label welcomeLabel = new Label("Login successful! Welcome, " + user.getUsername());
                welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

                ProgressBar progressBar = new ProgressBar(0);
                progressBar.setPrefWidth(300);

                VBox vbox = new VBox(10, welcomeLabel, progressBar);
                vbox.setAlignment(Pos.CENTER);

                Scene progressScene = new Scene(vbox, 400, 200);
                Stage progressStage = new Stage();
                progressStage.setTitle("Welcome");
                progressStage.setScene(progressScene);

                progressStage.show();

                new Thread(() -> {
                    for (double progress = 0; progress <= 1; progress += 0.1) {
                        double currentProgress = progress;
                        try {
                            Thread.sleep(500);
                            Platform.runLater(() -> progressBar.setProgress(currentProgress));
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    Platform.runLater(() -> {
                        dashboardpage dashboardPage = new dashboardpage();
                        try {
                            dashboardPage.start(new Stage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        primaryStage.close();
                        progressStage.close();
                    });
                }).start();
            } else {
                showAlert("Invalid username or password. Please try again.");
            }
        });

        gridPane.add(lblUsername, 0, 0);
        gridPane.add(txtUsername, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(txtPassword, 1, 1);
        gridPane.add(btnLogin, 1, 2);

        StackPane centerPane = new StackPane(rectangle, gridPane);
        centerPane.setAlignment(Pos.CENTER);

        layout.setCenter(centerPane);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String message) {
        Stage alertStage = new Stage();
        alertStage.setTitle("Message");
        Rectangle rectangle = new Rectangle(400, 150);
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> alertStage.close());
        okButton.setStyle("-fx-text-fill: white; -fx-border-color: white; -fx-background-color: rgba(0,0,0,0.6);");

        VBox vbox = new VBox(10, messageLabel, okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        StackPane stackPane = new StackPane(rectangle, vbox);
        stackPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(stackPane, 420, 180);
        alertStage.setScene(scene);
        alertStage.showAndWait();
    }
}

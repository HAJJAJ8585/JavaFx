package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Car;
import models.Customer;
import models.Employee;
import models.Order;
import models.Payment;
import models.Service;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * dashboardpage.java
 * 
 * This class provides a Dashboard UI with CRUD (Create, Read, Update, Delete)
 * and filtering functionalities for the following entities:
 * - Cars
 * - Services
 * - Orders
 * - Customers
 * - Payment
 * - Employees
 * 
 * Additionally, it provides a Reports section where the user can select one of
 * the given report types, enter parameters, and generate a result table.
 * 
 * Note:
 * - The SQL queries are placeholders. Adjust them to match your actual database schema.
 * - Ensure that the images and resources referenced exist in 
 *   src/main/resources/com/example/resources/images/
 * - If you don't have these resources, comment out or remove the relevant lines.
 * - The code depends on existing "models" classes and a "utils.DatabaseConnection" class
 *   similar to previous instructions.
 * - This code is provided as an example/template. Further refinement, error handling,
 *   and input validation may be necessary.
 */
public class dashboardpage extends Application {
    private StackPane mainContent = new StackPane();

    @Override
    public void start(Stage primaryStage) {
        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                homepage homePage = new homepage();
                homePage.start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    
        // Header Label
        Label headerLabel = new Label("Dashboard");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
    
        HBox leftHeaderBox = new HBox(10, backButton, headerLabel);
        leftHeaderBox.setAlignment(Pos.CENTER_LEFT);
    
        BorderPane header = new BorderPane();
        header.setLeft(leftHeaderBox);
        header.setPadding(new Insets(10));
    
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1050, 576);
    
        // Background image
        try {
            Image bgImage = new Image("file:///C:/Users/Admin/Desktop/Dbase/Hajjaj/demo/src/main/java/ui/resources/images/dashboard2bg.jpg");
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    backgroundSize
            );
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    
        root.getChildren().add(header);
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
    
        VBox sideNav = new VBox();
        sideNav.setPrefWidth(256);
        sideNav.setPrefSize(256, 693);
sideNav.setStyle("-fx-background-color: rgb(39, 39, 39);");        sideNav.setPadding(new Insets(80, 10, 10, 10));
        
        // Make the VBox fill the available height
        VBox.setVgrow(sideNav, Priority.ALWAYS);  // Ensures VBox grows to fill the height
        
        // Create navigation buttons
        Button btnCars = createNavButton("Cars", "Hajjaj\\demo\\src\\main\\java\\ui\\car_icon.png");
        Button btnServices = createNavButton("Services", "/resources/images/services_icon.png");
        Button btnOrders = createNavButton("Orders", "/resources/images/order_icon.png");
        Button btnCustomers = createNavButton("Customers", "/resources/images/customer_icon.png");
        Button btnPayment = createNavButton("Payment", "/resources/images/payment_icon.png");
        Button btnEmployees = createNavButton("Employees", "/resources/images/employees_icon.png");
        Button btnReports = createNavButton("Reports", "/resources/images/report_icon.png");
        Button btnSignout = createNavButton("Signout", "/resources/images/signout_icon.png");
            
        sideNav.getChildren().addAll(btnCars, btnServices, btnOrders, btnCustomers, btnPayment, btnEmployees, btnReports, btnSignout);
        
        mainContent = new StackPane();
        mainContent.setLayoutX(258);
        mainContent.setPrefSize(793, 576);
        
        root.getChildren().addAll(sideNav, mainContent);
        AnchorPane.setTopAnchor(sideNav, 0.0);
        AnchorPane.setLeftAnchor(sideNav, 0.0);
        AnchorPane.setTopAnchor(mainContent, 0.0);
        AnchorPane.setLeftAnchor(mainContent, 258.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);
        AnchorPane.setBottomAnchor(mainContent, 0.0);
        sideNav.toBack();
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard");
        primaryStage.centerOnScreen();
        primaryStage.show();
        
    
        // Button Actions
        btnCars.setOnAction(e -> showCarsSection());
        btnServices.setOnAction(e -> showServicesSection());
        btnOrders.setOnAction(e -> showOrdersSection());
        btnCustomers.setOnAction(e -> showCustomersSection());
        btnPayment.setOnAction(e -> showPaymentSection());
        btnEmployees.setOnAction(e -> showEmployeesSection());
        btnReports.setOnAction(e -> showReportsMenu());
        btnSignout.setOnAction(e -> handleSignOut(primaryStage));
    }
    
    // The rest of your methods remain unchanged...
    
   

    private Button createNavButton(String text, String iconPath) {
        Button button = new Button(text);
        button.setStyle("-fx-text-fill: #e7e5e5; -fx-background-color: transparent; -fx-font-size: 14px;");
        button.setPrefSize(259, 42);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(10, 10, 10, 20));
        try {
            Image icon = new Image(getClass().getResource(iconPath).toExternalForm());
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(23);
            imageView.setFitWidth(27);
            button.setGraphic(imageView);
        } catch (Exception e) {
            System.out.println("Icon not found for: " + text);
        }
        return button;
    }

    // ============================= CARS SECTION =============================
    private void showCarsSection() {
        mainContent.getChildren().clear();
        TableView<Car> table = createCarTableView();
        ObservableList<Car> data = loadCarData();
        FilteredList<Car> filteredData = new FilteredList<>(data, p -> true);

        TextField txtCarID = new TextField(); txtCarID.setPromptText("CarID");
        TextField txtMake = new TextField(); txtMake.setPromptText("Make");
        TextField txtModel = new TextField(); txtModel.setPromptText("Model");
        TextField txtYear = new TextField(); txtYear.setPromptText("Year");
        TextField txtPrice = new TextField(); txtPrice.setPromptText("Price");
        TextField txtStock = new TextField(); txtStock.setPromptText("Stock");
        TextField txtVIN = new TextField(); txtVIN.setPromptText("VIN");

        txtCarID.textProperty().addListener((obs, oldVal, newVal) -> applyCarFilter(filteredData, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN));
        txtMake.textProperty().addListener((obs, oldVal, newVal) -> applyCarFilter(filteredData, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN));
        txtModel.textProperty().addListener((obs, oldVal, newVal) -> applyCarFilter(filteredData, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN));
        txtYear.textProperty().addListener((obs, oldVal, newVal) -> applyCarFilter(filteredData, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN));
        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> applyCarFilter(filteredData, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN));
        txtStock.textProperty().addListener((obs, oldVal, newVal) -> applyCarFilter(filteredData, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN));
        txtVIN.textProperty().addListener((obs, oldVal, newVal) -> applyCarFilter(filteredData, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN));

        table.setItems(filteredData);

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> addCar(txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN, data));

        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(e -> updateCar(table, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN, data));

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteCar(table, txtCarID, data));

        HBox inputFields = new HBox(10, txtCarID, txtMake, txtModel, txtYear, txtPrice, txtStock, txtVIN);
        inputFields.setPadding(new Insets(10));
        HBox actionButtons = new HBox(10, btnAdd, btnUpdate, btnDelete);
        actionButtons.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, inputFields, actionButtons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        mainContent.getChildren().add(layout);
    }

    private TableView<Car> createCarTableView() {
        TableView<Car> table = new TableView<>();
        table.setPrefSize(750, 400);

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

        table.getColumns().addAll(colCarID, colMake, colModel, colYear, colPrice, colStock, colVIN);
        return table;
    }

    // ============================= SERVICES SECTION =============================
    private void showServicesSection() {
        mainContent.getChildren().clear();

        TableView<Service> table = createServiceTableView();
        ObservableList<Service> data = loadServiceData();
        FilteredList<Service> filteredData = new FilteredList<>(data, p -> true);

        TextField txtServiceID = new TextField(); txtServiceID.setPromptText("ServiceID");
        TextField txtCarID = new TextField(); txtCarID.setPromptText("CarID");
        TextField txtCustomerID = new TextField(); txtCustomerID.setPromptText("CustomerID");
        TextField txtServiceDate = new TextField(); txtServiceDate.setPromptText("ServiceDate");
        TextField txtDescription = new TextField(); txtDescription.setPromptText("Description");
        TextField txtCost = new TextField(); txtCost.setPromptText("Cost");

        txtServiceID.textProperty().addListener((obs, oldVal, newVal) -> applyServiceFilter(filteredData, txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost));
        txtCarID.textProperty().addListener((obs, oldVal, newVal) -> applyServiceFilter(filteredData, txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost));
        txtCustomerID.textProperty().addListener((obs, oldVal, newVal) -> applyServiceFilter(filteredData, txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost));
        txtServiceDate.textProperty().addListener((obs, oldVal, newVal) -> applyServiceFilter(filteredData, txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost));
        txtDescription.textProperty().addListener((obs, oldVal, newVal) -> applyServiceFilter(filteredData, txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost));
        txtCost.textProperty().addListener((obs, oldVal, newVal) -> applyServiceFilter(filteredData, txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost));

        table.setItems(filteredData);

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> addService(txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost, data));
        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(e -> updateService(table, txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost, data));
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteService(table, txtServiceID, data));

        HBox inputFields = new HBox(10, txtServiceID, txtCarID, txtCustomerID, txtServiceDate, txtDescription, txtCost);
        inputFields.setPadding(new Insets(10));
        HBox actionButtons = new HBox(10, btnAdd, btnUpdate, btnDelete);
        actionButtons.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, inputFields, actionButtons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        mainContent.getChildren().add(layout);
    }

    private TableView<Service> createServiceTableView() {
        TableView<Service> table = new TableView<>();
        table.setPrefSize(750, 400);

        TableColumn<Service, Integer> colServiceID = new TableColumn<>("ServiceID");
        colServiceID.setCellValueFactory(new PropertyValueFactory<>("serviceID"));

        TableColumn<Service, Integer> colCarID = new TableColumn<>("CarID");
        colCarID.setCellValueFactory(new PropertyValueFactory<>("carID"));

        TableColumn<Service, Integer> colCustomerID = new TableColumn<>("CustomerID");
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        TableColumn<Service, String> colServiceDate = new TableColumn<>("ServiceDate");
        colServiceDate.setCellValueFactory(new PropertyValueFactory<>("serviceDate"));

        TableColumn<Service, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("serviceDescription"));

        TableColumn<Service, Double> colCost = new TableColumn<>("Cost");
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        table.getColumns().addAll(colServiceID, colCarID, colCustomerID, colServiceDate, colDescription, colCost);
        return table;
    }

    // ============================= ORDERS SECTION =============================
    private void showOrdersSection() {
        mainContent.getChildren().clear();
        TableView<Order> table = createOrderTableView();
        ObservableList<Order> data = loadOrderData();
        FilteredList<Order> filteredData = new FilteredList<>(data, p -> true);

        TextField txtOrderID = new TextField(); txtOrderID.setPromptText("OrderID");
        TextField txtOrderDate = new TextField(); txtOrderDate.setPromptText("OrderDate");
        TextField txtCarID = new TextField(); txtCarID.setPromptText("CarID");
        TextField txtCustomerID = new TextField(); txtCustomerID.setPromptText("CustomerID");
        TextField txtEmployeeID = new TextField(); txtEmployeeID.setPromptText("EmployeeID");
        TextField txtQuantity = new TextField(); txtQuantity.setPromptText("Quantity");
        TextField txtTotalPrice = new TextField(); txtTotalPrice.setPromptText("TotalPrice");

        txtOrderID.textProperty().addListener((obs, oldVal, newVal) -> applyOrderFilter(filteredData, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice));
        txtOrderDate.textProperty().addListener((obs, oldVal, newVal) -> applyOrderFilter(filteredData, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice));
        txtCarID.textProperty().addListener((obs, oldVal, newVal) -> applyOrderFilter(filteredData, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice));
        txtCustomerID.textProperty().addListener((obs, oldVal, newVal) -> applyOrderFilter(filteredData, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice));
        txtEmployeeID.textProperty().addListener((obs, oldVal, newVal) -> applyOrderFilter(filteredData, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice));
        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> applyOrderFilter(filteredData, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice));
        txtTotalPrice.textProperty().addListener((obs, oldVal, newVal) -> applyOrderFilter(filteredData, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice));

        table.setItems(filteredData);

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> addOrder(txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice, data));
        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(e -> updateOrder(table, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice, data));
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteOrder(table, txtOrderID, data));

        HBox inputFields = new HBox(10, txtOrderID, txtOrderDate, txtCarID, txtCustomerID, txtEmployeeID, txtQuantity, txtTotalPrice);
        inputFields.setPadding(new Insets(10));
        HBox actionButtons = new HBox(10, btnAdd, btnUpdate, btnDelete);
        actionButtons.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, inputFields, actionButtons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        mainContent.getChildren().add(layout);
    }

    private TableView<Order> createOrderTableView() {
        TableView<Order> table = new TableView<>();
        table.setPrefSize(750, 400);

        TableColumn<Order, Integer> colOrderID = new TableColumn<>("OrderID");
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));

        TableColumn<Order, String> colOrderDate = new TableColumn<>("OrderDate");
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        TableColumn<Order, Integer> colCarID = new TableColumn<>("CarID");
        colCarID.setCellValueFactory(new PropertyValueFactory<>("carID"));

        TableColumn<Order, Integer> colCustomerID = new TableColumn<>("CustomerID");
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        TableColumn<Order, Integer> colEmployeeID = new TableColumn<>("EmployeeID");
        colEmployeeID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

        TableColumn<Order, Integer> colQuantity = new TableColumn<>("Quantity");
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Order, Double> colTotalPrice = new TableColumn<>("TotalPrice");
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        table.getColumns().addAll(colOrderID, colOrderDate, colCarID, colCustomerID, colEmployeeID, colQuantity, colTotalPrice);
        return table;
    }

    // ============================= CUSTOMERS SECTION =============================
    private void showCustomersSection() {
        mainContent.getChildren().clear();
        TableView<Customer> table = createCustomerTableView();
        ObservableList<Customer> data = loadCustomerData();
        FilteredList<Customer> filteredData = new FilteredList<>(data, p -> true);

        TextField txtCustomerID = new TextField(); txtCustomerID.setPromptText("CustomerID");
        TextField txtFirstName = new TextField(); txtFirstName.setPromptText("FirstName");
        TextField txtLastName = new TextField(); txtLastName.setPromptText("LastName");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Email");
        TextField txtPhone = new TextField(); txtPhone.setPromptText("Phone");
        TextField txtAddress = new TextField(); txtAddress.setPromptText("Address");
        TextField txtCity = new TextField(); txtCity.setPromptText("City");
        TextField txtState = new TextField(); txtState.setPromptText("State");
        TextField txtZip = new TextField(); txtZip.setPromptText("ZipCode");

        txtCustomerID.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));
        txtFirstName.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));
        txtLastName.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));
        txtEmail.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));
        txtPhone.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));
        txtAddress.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));
        txtCity.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));
        txtState.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));
        txtZip.textProperty().addListener((obs, oldVal, newVal) -> applyCustomerFilter(filteredData, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip));

        table.setItems(filteredData);

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> addCustomer(txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip, data));
        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(e -> updateCustomer(table, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip, data));
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteCustomer(table, txtCustomerID, data));

        HBox inputFields = new HBox(10, txtCustomerID, txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtCity, txtState, txtZip);
        inputFields.setPadding(new Insets(10));
        HBox actionButtons = new HBox(10, btnAdd, btnUpdate, btnDelete);
        actionButtons.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, inputFields, actionButtons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        mainContent.getChildren().add(layout);
    }

    private TableView<Customer> createCustomerTableView() {
        TableView<Customer> table = new TableView<>();
        table.setPrefSize(750, 400);

        TableColumn<Customer, Integer> colCustomerID = new TableColumn<>("CustomerID");
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        TableColumn<Customer, String> colFirstName = new TableColumn<>("FirstName");
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Customer, String> colLastName = new TableColumn<>("LastName");
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Customer, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Customer, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Customer, String> colAddress = new TableColumn<>("Address");
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customer, String> colCity = new TableColumn<>("City");
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));

        TableColumn<Customer, String> colState = new TableColumn<>("State");
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));

        TableColumn<Customer, String> colZip = new TableColumn<>("ZipCode");
        colZip.setCellValueFactory(new PropertyValueFactory<>("zipCode"));

        table.getColumns().addAll(colCustomerID, colFirstName, colLastName, colEmail, colPhone, colAddress, colCity, colState, colZip);
        return table;
    }

    // ============================= PAYMENT SECTION =============================
    private void showPaymentSection() {
        mainContent.getChildren().clear();
        TableView<Payment> table = createPaymentTableView();
        ObservableList<Payment> data = loadPaymentData();
        FilteredList<Payment> filteredData = new FilteredList<>(data, p -> true);

        TextField txtPaymentID = new TextField(); txtPaymentID.setPromptText("PaymentID");
        TextField txtOrderID = new TextField(); txtOrderID.setPromptText("OrderID");
        TextField txtPaymentDate = new TextField(); txtPaymentDate.setPromptText("PaymentDate");
        TextField txtPaymentMethod = new TextField(); txtPaymentMethod.setPromptText("PaymentMethod");
        TextField txtAmount = new TextField(); txtAmount.setPromptText("Amount");

        txtPaymentID.textProperty().addListener((obs, oldVal, newVal) -> applyPaymentFilter(filteredData, txtPaymentID, txtOrderID, txtPaymentDate, txtPaymentMethod, txtAmount));
        txtOrderID.textProperty().addListener((obs, oldVal, newVal) -> applyPaymentFilter(filteredData, txtPaymentID, txtOrderID, txtPaymentDate, txtPaymentMethod, txtAmount));
        txtPaymentDate.textProperty().addListener((obs, oldVal, newVal) -> applyPaymentFilter(filteredData, txtPaymentID, txtOrderID, txtPaymentDate, txtPaymentMethod, txtAmount));
        txtPaymentMethod.textProperty().addListener((obs, oldVal, newVal) -> applyPaymentFilter(filteredData, txtPaymentID, txtOrderID, txtPaymentDate, txtPaymentMethod, txtAmount));
        txtAmount.textProperty().addListener((obs, oldVal, newVal) -> applyPaymentFilter(filteredData, txtPaymentID, txtOrderID, txtPaymentDate, txtPaymentMethod, txtAmount));

        table.setItems(filteredData);

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> addPayment(txtPaymentID, txtOrderID, txtPaymentDate, txtPaymentMethod, txtAmount, data));
        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(e -> updatePayment(table, txtPaymentID, txtOrderID, txtPaymentDate, txtPaymentMethod, txtAmount, data));
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deletePayment(table, txtPaymentID, data));

        HBox inputFields = new HBox(10, txtPaymentID, txtOrderID, txtPaymentDate, txtPaymentMethod, txtAmount);
        inputFields.setPadding(new Insets(10));
        HBox actionButtons = new HBox(10, btnAdd, btnUpdate, btnDelete);
        actionButtons.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, inputFields, actionButtons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        mainContent.getChildren().add(layout);
    }

    private TableView<Payment> createPaymentTableView() {
        TableView<Payment> table = new TableView<>();
        table.setPrefSize(750, 400);

        TableColumn<Payment, Integer> colPaymentID = new TableColumn<>("PaymentID");
        colPaymentID.setCellValueFactory(new PropertyValueFactory<>("paymentID"));

        TableColumn<Payment, Integer> colOrderID = new TableColumn<>("OrderID");
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));

        TableColumn<Payment, String> colPaymentDate = new TableColumn<>("PaymentDate");
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        TableColumn<Payment, String> colPaymentMethod = new TableColumn<>("PaymentMethod");
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Payment, Double> colAmount = new TableColumn<>("Amount");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.getColumns().addAll(colPaymentID, colOrderID, colPaymentDate, colPaymentMethod, colAmount);
        return table;
    }

    // ============================= EMPLOYEES SECTION =============================
    private void showEmployeesSection() {
        mainContent.getChildren().clear();
        TableView<Employee> table = createEmployeeTableView();
        ObservableList<Employee> data = loadEmployeeData();
        FilteredList<Employee> filteredData = new FilteredList<>(data, p -> true);

        TextField txtEmployeeID = new TextField(); txtEmployeeID.setPromptText("EmployeeID");
        TextField txtFirstName = new TextField(); txtFirstName.setPromptText("FirstName");
        TextField txtLastName = new TextField(); txtLastName.setPromptText("LastName");
        TextField txtPosition = new TextField(); txtPosition.setPromptText("Position");
        TextField txtSalary = new TextField(); txtSalary.setPromptText("Salary");
        TextField txtHireDate = new TextField(); txtHireDate.setPromptText("HireDate");

        txtEmployeeID.textProperty().addListener((obs, oldVal, newVal) -> applyEmployeeFilter(filteredData, txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate));
        txtFirstName.textProperty().addListener((obs, oldVal, newVal) -> applyEmployeeFilter(filteredData, txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate));
        txtLastName.textProperty().addListener((obs, oldVal, newVal) -> applyEmployeeFilter(filteredData, txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate));
        txtPosition.textProperty().addListener((obs, oldVal, newVal) -> applyEmployeeFilter(filteredData, txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate));
        txtSalary.textProperty().addListener((obs, oldVal, newVal) -> applyEmployeeFilter(filteredData, txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate));
        txtHireDate.textProperty().addListener((obs, oldVal, newVal) -> applyEmployeeFilter(filteredData, txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate));

        table.setItems(filteredData);

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> addEmployee(txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate, data));
        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(e -> updateEmployee(table, txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate, data));
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteEmployee(table, txtEmployeeID, data));

        HBox inputFields = new HBox(10, txtEmployeeID, txtFirstName, txtLastName, txtPosition, txtSalary, txtHireDate);
        inputFields.setPadding(new Insets(10));
        HBox actionButtons = new HBox(10, btnAdd, btnUpdate, btnDelete);
        actionButtons.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, inputFields, actionButtons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        mainContent.getChildren().add(layout);
    }

    private TableView<Employee> createEmployeeTableView() {
        TableView<Employee> table = new TableView<>();
        table.setPrefSize(750, 400);

        TableColumn<Employee, Integer> colEmployeeID = new TableColumn<>("EmployeeID");
        colEmployeeID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

        TableColumn<Employee, String> colFirstName = new TableColumn<>("FirstName");
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Employee, String> colLastName = new TableColumn<>("LastName");
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Employee, String> colPosition = new TableColumn<>("Position");
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<Employee, Double> colSalary = new TableColumn<>("Salary");
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        TableColumn<Employee, String> colHireDate = new TableColumn<>("HireDate");
        colHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        table.getColumns().addAll(colEmployeeID, colFirstName, colLastName, colPosition, colSalary, colHireDate);
        return table;
    }

    // ============================= REPORTS SECTION =============================
    private void showReportsMenu() {
        mainContent.getChildren().clear();

        VBox reportsBox = new VBox(20);
        reportsBox.setPadding(new Insets(20));
        reportsBox.setAlignment(Pos.CENTER);
        reportsBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); "
                + "-fx-border-color: #ccc; "
                + "-fx-border-width: 1; "
                + "-fx-border-radius: 10; "
                + "-fx-background-radius: 10;");

        Label lblTitle = new Label("Generate Report");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label lblReportType = new Label("Select Report Type:");
        lblReportType.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        ComboBox<String> cmbReports = new ComboBox<>();
        cmbReports.getItems().addAll(
                "Services performed on a specific car/customer",
                "Sales completed by a particular employee",
                "Payment amounts and methods by a customer",
                "Monthly/quarterly revenue by service type",
                "Frequency of services by car model/customer category",
                "Detailed history of service costs for specific vehicles"
        );
        cmbReports.setStyle("-fx-font-size: 14px; -fx-background-color: rgba(0, 0, 0, 0.8); -fx-text-fill: white;");

        Label lblParam1 = new Label("Enter Parameter 1 (Optional):");
        lblParam1.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        TextField txtParam1 = new TextField();
        txtParam1.setStyle("-fx-font-size: 14px;");

        Label lblParam2 = new Label("Enter Parameter 2 (Optional):");
        lblParam2.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        TextField txtParam2 = new TextField();
        txtParam2.setStyle("-fx-font-size: 14px;");

        cmbReports.setOnAction(e -> {
            String selectedReport = cmbReports.getValue();
            if (selectedReport != null) {
                switch (selectedReport) {
                    case "Services performed on a specific car/customer":
                        lblParam1.setText("Car/Customer ID:");
                        lblParam2.setText("Service Date (Optional):");
                        break;
                    case "Sales completed by a particular employee":
                        lblParam1.setText("Employee ID:");
                        lblParam2.setText("Date Range (Optional):");
                        break;
                    case "Payment amounts and methods by a customer":
                        lblParam1.setText("Customer Name:");
                        lblParam2.setText("Payment Method (Optional):");
                        break;
                    case "Monthly/quarterly revenue by service type":
                        lblParam1.setText("Service Type:");
                        lblParam2.setText("Date Range (Optional):");
                        break;
                    case "Frequency of services by car model/customer category":
                        lblParam1.setText("Car Model/Customer Category:");
                        lblParam2.setText("Time Period (Optional):");
                        break;
                    case "Detailed history of service costs for specific vehicles":
                        lblParam1.setText("Vehicle ID:");
                        lblParam2.setText("Date Range (Optional):");
                        break;
                    default:
                        lblParam1.setText("Enter Parameter 1 (Optional):");
                        lblParam2.setText("Enter Parameter 2 (Optional):");
                        break;
                }
            }
        });

        Button btnGenerate = new Button("Generate Report");
        btnGenerate.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        btnGenerate.setOnAction(e -> {
            String reportType = cmbReports.getValue();
            if (reportType == null || reportType.isEmpty()) {
                showAlert("Please select a report type!");
                return;
            }

            String param1 = txtParam1.getText().trim();
            String param2 = txtParam2.getText().trim();

            TableView<ObservableList<String>> reportTable = executeReport(reportType, param1, param2);
            if (reportTable == null || reportTable.getItems().isEmpty()) {
                showAlert("No results found or error running report.");
            } else {
                mainContent.getChildren().clear();
                mainContent.getChildren().add(reportTable);
            }
        });

        reportsBox.getChildren().addAll(lblTitle, lblReportType, cmbReports, lblParam1, txtParam1, lblParam2, txtParam2, btnGenerate);
        mainContent.getChildren().add(reportsBox);
    }

    private TableView<ObservableList<String>> executeReport(String reportType, String param1, String param2) {
        String query = null;

        // Placeholder queries, adjust for your schema:
        switch (reportType) {
            case "Services performed on a specific car/customer":
                query = "SELECT * FROM services WHERE (CarID = ? OR CustomerID = ?) " + (param2.isEmpty() ? "" : "AND ServiceDate = ?");
                break;
            case "Sales completed by a particular employee":
                query = "SELECT * FROM orders WHERE EmployeeID = ?" + (param2.isEmpty() ? "" : " AND OrderDate <= ?");
                break;
            case "Payment amounts and methods by a customer":
                query = "SELECT p.PaymentDate, p.Amount, p.PaymentMethod FROM payments p JOIN customers c ON p.OrderID = c.CustomerID WHERE c.FirstName = ?" + (param2.isEmpty() ? "" : " AND p.PaymentMethod = ?");
                break;
            case "Monthly/quarterly revenue by service type":
                query = "SELECT ServiceDescription as ServiceType, SUM(Cost) as Revenue FROM services WHERE ServiceDescription LIKE ?" + (param2.isEmpty() ? "" : " AND ServiceDate <= ?") + " GROUP BY ServiceDescription";
                break;
            case "Frequency of services by car model/customer category":
                query = "SELECT c.Model, COUNT(*) as Frequency FROM services s JOIN cars c ON s.CarID = c.CarID WHERE c.Model = ?" + (param2.isEmpty() ? "" : " AND s.ServiceDate <= ?") + " GROUP BY c.Model";
                break;
            case "Detailed history of service costs for specific vehicles":
                query = "SELECT * FROM services WHERE CarID = ?" + (param2.isEmpty() ? "" : " AND ServiceDate <= ?") + " ORDER BY ServiceDate ASC";
                break;
            default:
                break;
        }

        if (query == null) return null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (!param1.isEmpty()) {
                pstmt.setString(1, param1);
            }
            if (!param2.isEmpty()) {
                pstmt.setString(2, param2);
            } else if (!param1.isEmpty() && query.contains("CarID = ? OR CustomerID = ?")) {
                // For the first report, we need to set both CarID and CustomerID if param2 is empty:
                pstmt.setString(2, param1);
            }

            ResultSet rs = pstmt.executeQuery();
            return buildDynamicTable(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private TableView<ObservableList<String>> buildDynamicTable(ResultSet rs) throws SQLException {
        TableView<ObservableList<String>> table = new TableView<>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(md.getColumnLabel(i));
            col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex - 1)));
            table.getColumns().add(col);
        }

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getString(i) == null ? "" : rs.getString(i));
            }
            data.add(row);
        }
        table.setItems(data);
        return table;
    }

    private void handleSignOut(Stage primaryStage) {
        Stage signOutStage = new Stage();
        signOutStage.initModality(Modality.APPLICATION_MODAL);

        VBox signOutLayout = new VBox(10);
        signOutLayout.setAlignment(Pos.CENTER);
        signOutLayout.setPadding(new Insets(20));

        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(-1.0);
        Label lblMessage = new Label("Signing out...");

        signOutLayout.getChildren().addAll(lblMessage, progressBar);

        Scene signOutScene = new Scene(signOutLayout, 300, 150);
        signOutStage.setScene(signOutScene);
        signOutStage.setTitle("Sign Out");
        signOutStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    signOutStage.close();
                    Loginpage loginPage = new Loginpage();
                    try {
                        loginPage.start(new Stage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    primaryStage.close();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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
        okButton.setStyle("-fx-text-fill: white; -fx-border-color: white; -fx-background-color: rgba(0,0,0,0.6);");
        okButton.setOnAction(e -> alertStage.close());

        VBox vbox = new VBox(10, messageLabel, okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        StackPane stackPane = new StackPane(rectangle, vbox);
        stackPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(stackPane, 420, 180);
        alertStage.setScene(scene);
        alertStage.showAndWait();
    }

    private boolean matchFilter(String value, String filter) {
        if (filter == null || filter.isEmpty()) return true;
        return value.toLowerCase().contains(filter.toLowerCase());
    }

    // Filters for each entity:
    private void applyCarFilter(FilteredList<Car> filteredData, TextField txtCarID, TextField txtMake, TextField txtModel,
                                TextField txtYear, TextField txtPrice, TextField txtStock, TextField txtVIN) {
        filteredData.setPredicate(car -> {
            if (!matchFilter(Integer.toString(car.getcarId()), txtCarID.getText())) return false;
            if (!matchFilter(car.getMake(), txtMake.getText())) return false;
            if (!matchFilter(car.getModel(), txtModel.getText())) return false;
            if (!matchFilter(Integer.toString(car.getYear()), txtYear.getText())) return false;
            if (!matchFilter(Double.toString(car.getPrice()), txtPrice.getText())) return false;
            if (!matchFilter(Integer.toString(car.getStock()), txtStock.getText())) return false;
            if (!matchFilter(car.getVin(), txtVIN.getText())) return false;
            return true;
        });
    }

    private void applyServiceFilter(FilteredList<Service> filteredData, TextField txtServiceID, TextField txtCarID, TextField txtCustomerID,
                                    TextField txtServiceDate, TextField txtDescription, TextField txtCost) {
        filteredData.setPredicate(service -> {
            if (!matchFilter(Integer.toString(service.getServiceID()), txtServiceID.getText())) return false;
            if (!matchFilter(Integer.toString(service.getCarID()), txtCarID.getText())) return false;
            if (!matchFilter(Integer.toString(service.getCustomerID()), txtCustomerID.getText())) return false;
            if (!matchFilter(service.getServiceDate(), txtServiceDate.getText())) return false;
            if (!matchFilter(service.getServiceDescription(), txtDescription.getText())) return false;
            if (!matchFilter(Double.toString(service.getCost()), txtCost.getText())) return false;
            return true;
        });
    }

    private void applyOrderFilter(FilteredList<Order> filteredData, TextField txtOrderID, TextField txtOrderDate, TextField txtCarID, TextField txtCustomerID, TextField txtEmployeeID, TextField txtQuantity, TextField txtTotalPrice) {
        filteredData.setPredicate(order -> {
            if (!matchFilter(Integer.toString(order.getOrderID()), txtOrderID.getText())) return false;
            if (!matchFilter(order.getOrderDate(), txtOrderDate.getText())) return false;
            if (!matchFilter(Integer.toString(order.getCarID()), txtCarID.getText())) return false;
            if (!matchFilter(Integer.toString(order.getCustomerID()), txtCustomerID.getText())) return false;
            if (!matchFilter(Integer.toString(order.getEmployeeID()), txtEmployeeID.getText())) return false;
            if (!matchFilter(Integer.toString(order.getQuantity()), txtQuantity.getText())) return false;
            if (!matchFilter(Double.toString(order.getTotalPrice()), txtTotalPrice.getText())) return false;
            return true;
        });
    }

    private void applyCustomerFilter(FilteredList<Customer> filteredData, TextField txtCustomerID, TextField txtFirstName, TextField txtLastName,
                                     TextField txtEmail, TextField txtPhone, TextField txtAddress, TextField txtCity, TextField txtState, TextField txtZip) {
        filteredData.setPredicate(customer -> {
            if (!matchFilter(Integer.toString(customer.getCustomerID()), txtCustomerID.getText())) return false;
            if (!matchFilter(customer.getFirstName(), txtFirstName.getText())) return false;
            if (!matchFilter(customer.getLastName(), txtLastName.getText())) return false;
            if (!matchFilter(customer.getEmail(), txtEmail.getText())) return false;
            if (!matchFilter(customer.getPhone(), txtPhone.getText())) return false;
            if (!matchFilter(customer.getAddress(), txtAddress.getText())) return false;
            if (!matchFilter(customer.getCity(), txtCity.getText())) return false;
            if (!matchFilter(customer.getState(), txtState.getText())) return false;
            if (!matchFilter(customer.getZipCode(), txtZip.getText())) return false;
            return true;
        });
    }

    private void applyPaymentFilter(FilteredList<Payment> filteredData, TextField txtPaymentID, TextField txtOrderID, TextField txtPaymentDate,
                                    TextField txtPaymentMethod, TextField txtAmount) {
        filteredData.setPredicate(payment -> {
            if (!matchFilter(Integer.toString(payment.getPaymentID()), txtPaymentID.getText())) return false;
            if (!matchFilter(Integer.toString(payment.getOrderID()), txtOrderID.getText())) return false;
            if (!matchFilter(payment.getPaymentDate(), txtPaymentDate.getText())) return false;
            if (!matchFilter(payment.getPaymentMethod(), txtPaymentMethod.getText())) return false;
            if (!matchFilter(Double.toString(payment.getAmount()), txtAmount.getText())) return false;
            return true;
        });
    }

    private void applyEmployeeFilter(FilteredList<Employee> filteredData, TextField txtEmployeeID, TextField txtFirstName, TextField txtLastName,
                                     TextField txtPosition, TextField txtSalary, TextField txtHireDate) {
        filteredData.setPredicate(employee -> {
            if (!matchFilter(Integer.toString(employee.getEmployeeID()), txtEmployeeID.getText())) return false;
            if (!matchFilter(employee.getFirstName(), txtFirstName.getText())) return false;
            if (!matchFilter(employee.getLastName(), txtLastName.getText())) return false;
            if (!matchFilter(employee.getPosition(), txtPosition.getText())) return false;
            if (!matchFilter(Double.toString(employee.getSalary()), txtSalary.getText())) return false;
            if (!matchFilter(employee.getHireDate(), txtHireDate.getText())) return false;
            return true;
        });
    }

    // Load data methods for Orders, Customers, Payment, Employees:

    private ObservableList<Order> loadOrderData() {
        ObservableList<Order> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {
            while (rs.next()) {
                data.add(new Order(
                        rs.getInt("OrderID"),
                        rs.getString("OrderDate"),
                        rs.getInt("CarID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("Quantity"),
                        rs.getDouble("TotalPrice")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    private ObservableList<Customer> loadCustomerData() {
        ObservableList<Customer> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {
            while (rs.next()) {
                data.add(new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("State"),
                        rs.getString("ZipCode")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    private ObservableList<Payment> loadPaymentData() {
        ObservableList<Payment> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM payments")) {
            while (rs.next()) {
                data.add(new Payment(
                        rs.getInt("PaymentID"),
                        rs.getInt("OrderID"),
                        rs.getString("PaymentDate"),
                        rs.getString("PaymentMethod"),
                        rs.getDouble("Amount")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    private ObservableList<Employee> loadEmployeeData() {
        ObservableList<Employee> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {
            while (rs.next()) {
                data.add(new Employee(
                        rs.getInt("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Position"),
                        rs.getDouble("Salary"),
                        rs.getString("HireDate")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    // CRUD for Orders:
    private void addOrder(TextField txtOrderID, TextField txtOrderDate, TextField txtCarID, TextField txtCustomerID, TextField txtEmployeeID, TextField txtQuantity, TextField txtTotalPrice, ObservableList<Order> data) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (txtOrderID.getText().isEmpty() || txtOrderDate.getText().isEmpty() || txtCarID.getText().isEmpty() ||
                    txtCustomerID.getText().isEmpty() || txtEmployeeID.getText().isEmpty() ||
                    txtQuantity.getText().isEmpty() || txtTotalPrice.getText().isEmpty()) {
                showAlert("All fields are required for adding an order.");
                return;
            }
            String sql = "INSERT INTO orders (OrderID, OrderDate, CarID, CustomerID, EmployeeID, Quantity, TotalPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(txtOrderID.getText()));
            pstmt.setString(2, txtOrderDate.getText());
            pstmt.setInt(3, Integer.parseInt(txtCarID.getText()));
            pstmt.setInt(4, Integer.parseInt(txtCustomerID.getText()));
            pstmt.setInt(5, Integer.parseInt(txtEmployeeID.getText()));
            pstmt.setInt(6, Integer.parseInt(txtQuantity.getText()));
            pstmt.setDouble(7, Double.parseDouble(txtTotalPrice.getText()));
            pstmt.executeUpdate();
            data.add(new Order(Integer.parseInt(txtOrderID.getText()), txtOrderDate.getText(), Integer.parseInt(txtCarID.getText()),
                    Integer.parseInt(txtCustomerID.getText()), Integer.parseInt(txtEmployeeID.getText()),
                    Integer.parseInt(txtQuantity.getText()), Double.parseDouble(txtTotalPrice.getText())));
            showAlert("Order added successfully.");
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error adding order.");
        }
    }

    private void updateOrder(TableView<Order> table, TextField txtOrderID, TextField txtOrderDate, TextField txtCarID, TextField txtCustomerID, TextField txtEmployeeID, TextField txtQuantity, TextField txtTotalPrice, ObservableList<Order> data) {
        if (txtOrderID.getText().isEmpty()) {
            showAlert("OrderID is required for update.");
            return;
        }
        int orderID = Integer.parseInt(txtOrderID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("UPDATE orders SET ");
            List<Object> params = new ArrayList<>();

            if (!txtOrderDate.getText().isEmpty()) { sql.append("OrderDate = ?, "); params.add(txtOrderDate.getText()); }
            if (!txtCarID.getText().isEmpty()) { sql.append("CarID = ?, "); params.add(Integer.parseInt(txtCarID.getText())); }
            if (!txtCustomerID.getText().isEmpty()) { sql.append("CustomerID = ?, "); params.add(Integer.parseInt(txtCustomerID.getText())); }
            if (!txtEmployeeID.getText().isEmpty()) { sql.append("EmployeeID = ?, "); params.add(Integer.parseInt(txtEmployeeID.getText())); }
            if (!txtQuantity.getText().isEmpty()) { sql.append("Quantity = ?, "); params.add(Integer.parseInt(txtQuantity.getText())); }
            if (!txtTotalPrice.getText().isEmpty()) { sql.append("TotalPrice = ?, "); params.add(Double.parseDouble(txtTotalPrice.getText())); }

            if (params.isEmpty()) {
                showAlert("No fields to update.");
                return;
            }

            sql.setLength(sql.length() - 2);
            sql.append(" WHERE OrderID = ?");
            params.add(orderID);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i+1, params.get(i));
            }

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                for (Order o : data) {
                    if (o.getOrderID() == orderID) {
                        if (!txtOrderDate.getText().isEmpty()) o.setOrderDate(txtOrderDate.getText());
                        if (!txtCarID.getText().isEmpty()) o.setCarID(Integer.parseInt(txtCarID.getText()));
                        if (!txtCustomerID.getText().isEmpty()) o.setCustomerID(Integer.parseInt(txtCustomerID.getText()));
                        if (!txtEmployeeID.getText().isEmpty()) o.setEmployeeID(Integer.parseInt(txtEmployeeID.getText()));
                        if (!txtQuantity.getText().isEmpty()) o.setQuantity(Integer.parseInt(txtQuantity.getText()));
                        if (!txtTotalPrice.getText().isEmpty()) o.setTotalPrice(Double.parseDouble(txtTotalPrice.getText()));
                        table.refresh();
                        break;
                    }
                }
                showAlert("Order updated successfully.");
            } else {
                showAlert("No order found with that ID.");
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error updating order.");
        }
    }

    private void deleteOrder(TableView<Order> table, TextField txtOrderID, ObservableList<Order> data) {
        if (txtOrderID.getText().isEmpty()) {
            showAlert("OrderID is required for delete.");
            return;
        }
        int orderID = Integer.parseInt(txtOrderID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM orders WHERE OrderID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderID);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                data.removeIf(o -> o.getOrderID() == orderID);
                showAlert("Order deleted successfully.");
            } else {
                showAlert("No order found with that ID.");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error deleting order.");
        }
    }

    // CRUD for Customers:
    private void addCustomer(TextField txtCustomerID, TextField txtFirstName, TextField txtLastName, TextField txtEmail, TextField txtPhone,
                             TextField txtAddress, TextField txtCity, TextField txtState, TextField txtZip, ObservableList<Customer> data) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (txtCustomerID.getText().isEmpty() || txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() ||
                    txtEmail.getText().isEmpty() || txtPhone.getText().isEmpty() ||
                    txtAddress.getText().isEmpty() || txtCity.getText().isEmpty() ||
                    txtState.getText().isEmpty() || txtZip.getText().isEmpty()) {
                showAlert("All fields are required to add a customer.");
                return;
            }
            String sql = "INSERT INTO customers (CustomerID, FirstName, LastName, Email, Phone, Address, City, State, ZipCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(txtCustomerID.getText()));
            pstmt.setString(2, txtFirstName.getText());
            pstmt.setString(3, txtLastName.getText());
            pstmt.setString(4, txtEmail.getText());
            pstmt.setString(5, txtPhone.getText());
            pstmt.setString(6, txtAddress.getText());
            pstmt.setString(7, txtCity.getText());
            pstmt.setString(8, txtState.getText());
            pstmt.setString(9, txtZip.getText());
            pstmt.executeUpdate();
            data.add(new Customer(Integer.parseInt(txtCustomerID.getText()), txtFirstName.getText(), txtLastName.getText(),
                    txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), txtCity.getText(), txtState.getText(), txtZip.getText()));
            showAlert("Customer added successfully.");
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error adding customer.");
        }
    }

    private void updateCustomer(TableView<Customer> table, TextField txtCustomerID, TextField txtFirstName, TextField txtLastName, TextField txtEmail, TextField txtPhone,
                                TextField txtAddress, TextField txtCity, TextField txtState, TextField txtZip, ObservableList<Customer> data) {
        if (txtCustomerID.getText().isEmpty()) {
            showAlert("CustomerID is required for update.");
            return;
        }
        int customerID = Integer.parseInt(txtCustomerID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("UPDATE customers SET ");
            List<Object> params = new ArrayList<>();
            if (!txtFirstName.getText().isEmpty()) { sql.append("FirstName = ?, "); params.add(txtFirstName.getText()); }
            if (!txtLastName.getText().isEmpty()) { sql.append("LastName = ?, "); params.add(txtLastName.getText()); }
            if (!txtEmail.getText().isEmpty()) { sql.append("Email = ?, "); params.add(txtEmail.getText()); }
            if (!txtPhone.getText().isEmpty()) { sql.append("Phone = ?, "); params.add(txtPhone.getText()); }
            if (!txtAddress.getText().isEmpty()) { sql.append("Address = ?, "); params.add(txtAddress.getText()); }
            if (!txtCity.getText().isEmpty()) { sql.append("City = ?, "); params.add(txtCity.getText()); }
            if (!txtState.getText().isEmpty()) { sql.append("State = ?, "); params.add(txtState.getText()); }
            if (!txtZip.getText().isEmpty()) { sql.append("ZipCode = ?, "); params.add(txtZip.getText()); }

            if (params.isEmpty()) {
                showAlert("No fields to update.");
                return;
            }

            sql.setLength(sql.length() - 2);
            sql.append(" WHERE CustomerID = ?");
            params.add(customerID);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i+1, params.get(i));
            }

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                for (Customer c : data) {
                    if (c.getCustomerID() == customerID) {
                        if (!txtFirstName.getText().isEmpty()) c.setFirstName(txtFirstName.getText());
                        if (!txtLastName.getText().isEmpty()) c.setLastName(txtLastName.getText());
                        if (!txtEmail.getText().isEmpty()) c.setEmail(txtEmail.getText());
                        if (!txtPhone.getText().isEmpty()) c.setPhone(txtPhone.getText());
                        if (!txtAddress.getText().isEmpty()) c.setAddress(txtAddress.getText());
                        if (!txtCity.getText().isEmpty()) c.setCity(txtCity.getText());
                        if (!txtState.getText().isEmpty()) c.setState(txtState.getText());
                        if (!txtZip.getText().isEmpty()) c.setZipCode(txtZip.getText());
                        table.refresh();
                        break;
                    }
                }
                showAlert("Customer updated successfully.");
            } else {
                showAlert("No customer found with that ID.");
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error updating customer.");
        }
    }

    private void deleteCustomer(TableView<Customer> table, TextField txtCustomerID, ObservableList<Customer> data) {
        if (txtCustomerID.getText().isEmpty()) {
            showAlert("CustomerID is required for delete.");
            return;
        }
        int customerID = Integer.parseInt(txtCustomerID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM customers WHERE CustomerID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerID);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                data.removeIf(c -> c.getCustomerID() == customerID);
                showAlert("Customer deleted successfully.");
            } else {
                showAlert("No customer found with that ID.");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error deleting customer.");
        }
    }

    // CRUD for Payment:
    private void addPayment(TextField txtPaymentID, TextField txtOrderID, TextField txtPaymentDate, TextField txtPaymentMethod, TextField txtAmount, ObservableList<Payment> data) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (txtPaymentID.getText().isEmpty() || txtOrderID.getText().isEmpty() || txtPaymentDate.getText().isEmpty() ||
                    txtPaymentMethod.getText().isEmpty() || txtAmount.getText().isEmpty()) {
                showAlert("All fields are required to add a payment.");
                return;
            }
            String sql = "INSERT INTO payments (PaymentID, OrderID, PaymentDate, PaymentMethod, Amount) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(txtPaymentID.getText()));
            pstmt.setInt(2, Integer.parseInt(txtOrderID.getText()));
            pstmt.setString(3, txtPaymentDate.getText());
            pstmt.setString(4, txtPaymentMethod.getText());
            pstmt.setDouble(5, Double.parseDouble(txtAmount.getText()));
            pstmt.executeUpdate();
            data.add(new Payment(Integer.parseInt(txtPaymentID.getText()), Integer.parseInt(txtOrderID.getText()),
                    txtPaymentDate.getText(), txtPaymentMethod.getText(), Double.parseDouble(txtAmount.getText())));
            showAlert("Payment added successfully.");
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error adding payment.");
        }
    }

    private void updatePayment(TableView<Payment> table, TextField txtPaymentID, TextField txtOrderID, TextField txtPaymentDate, TextField txtPaymentMethod, TextField txtAmount, ObservableList<Payment> data) {
        if (txtPaymentID.getText().isEmpty()) {
            showAlert("PaymentID is required for update.");
            return;
        }
        int paymentID = Integer.parseInt(txtPaymentID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("UPDATE payments SET ");
            List<Object> params = new ArrayList<>();

            if (!txtOrderID.getText().isEmpty()) { sql.append("OrderID = ?, "); params.add(Integer.parseInt(txtOrderID.getText())); }
            if (!txtPaymentDate.getText().isEmpty()) { sql.append("PaymentDate = ?, "); params.add(txtPaymentDate.getText()); }
            if (!txtPaymentMethod.getText().isEmpty()) { sql.append("PaymentMethod = ?, "); params.add(txtPaymentMethod.getText()); }
            if (!txtAmount.getText().isEmpty()) { sql.append("Amount = ?, "); params.add(Double.parseDouble(txtAmount.getText())); }

            if (params.isEmpty()) {
                showAlert("No fields to update.");
                return;
            }

            sql.setLength(sql.length() - 2);
            sql.append(" WHERE PaymentID = ?");
            params.add(paymentID);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i+1, params.get(i));
            }

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                for (Payment p : data) {
                    if (p.getPaymentID() == paymentID) {
                        if (!txtOrderID.getText().isEmpty()) p.setOrderID(Integer.parseInt(txtOrderID.getText()));
                        if (!txtPaymentDate.getText().isEmpty()) p.setPaymentDate(txtPaymentDate.getText());
                        if (!txtPaymentMethod.getText().isEmpty()) p.setPaymentMethod(txtPaymentMethod.getText());
                        if (!txtAmount.getText().isEmpty()) p.setAmount(Double.parseDouble(txtAmount.getText()));
                        table.refresh();
                        break;
                    }
                }
                showAlert("Payment updated successfully.");
            } else {
                showAlert("No payment found with that ID.");
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error updating payment.");
        }
    }

    private void deletePayment(TableView<Payment> table, TextField txtPaymentID, ObservableList<Payment> data) {
        if (txtPaymentID.getText().isEmpty()) {
            showAlert("PaymentID is required for delete.");
            return;
        }
        int paymentID = Integer.parseInt(txtPaymentID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM payments WHERE PaymentID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, paymentID);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                data.removeIf(p -> p.getPaymentID() == paymentID);
                showAlert("Payment deleted successfully.");
            } else {
                showAlert("No payment found with that ID.");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error deleting payment.");
        }
    }

    // CRUD for Employees:
    private void addEmployee(TextField txtEmployeeID, TextField txtFirstName, TextField txtLastName, TextField txtPosition, TextField txtSalary, TextField txtHireDate, ObservableList<Employee> data) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (txtEmployeeID.getText().isEmpty() || txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() ||
                    txtPosition.getText().isEmpty() || txtSalary.getText().isEmpty() || txtHireDate.getText().isEmpty()) {
                showAlert("All fields are required to add an employee.");
                return;
            }
            String sql = "INSERT INTO employees (EmployeeID, FirstName, LastName, Position, Salary, HireDate) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(txtEmployeeID.getText()));
            pstmt.setString(2, txtFirstName.getText());
            pstmt.setString(3, txtLastName.getText());
            pstmt.setString(4, txtPosition.getText());
            pstmt.setDouble(5, Double.parseDouble(txtSalary.getText()));
            pstmt.setString(6, txtHireDate.getText());
            pstmt.executeUpdate();
            data.add(new Employee(Integer.parseInt(txtEmployeeID.getText()), txtFirstName.getText(), txtLastName.getText(),
                    txtPosition.getText(), Double.parseDouble(txtSalary.getText()), txtHireDate.getText()));
            showAlert("Employee added successfully.");
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error adding employee.");
        }
    }

    private void updateEmployee(TableView<Employee> table, TextField txtEmployeeID, TextField txtFirstName, TextField txtLastName, TextField txtPosition, TextField txtSalary, TextField txtHireDate, ObservableList<Employee> data) {
        if (txtEmployeeID.getText().isEmpty()) {
            showAlert("EmployeeID is required for update.");
            return;
        }
        int employeeID = Integer.parseInt(txtEmployeeID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("UPDATE employees SET ");
            List<Object> params = new ArrayList<>();

            if (!txtFirstName.getText().isEmpty()) { sql.append("FirstName = ?, "); params.add(txtFirstName.getText()); }
            if (!txtLastName.getText().isEmpty()) { sql.append("LastName = ?, "); params.add(txtLastName.getText()); }
            if (!txtPosition.getText().isEmpty()) { sql.append("Position = ?, "); params.add(txtPosition.getText()); }
            if (!txtSalary.getText().isEmpty()) { sql.append("Salary = ?, "); params.add(Double.parseDouble(txtSalary.getText())); }
            if (!txtHireDate.getText().isEmpty()) { sql.append("HireDate = ?, "); params.add(txtHireDate.getText()); }

            if (params.isEmpty()) {
                showAlert("No fields to update.");
                return;
            }

            sql.setLength(sql.length() - 2);
            sql.append(" WHERE EmployeeID = ?");
            params.add(employeeID);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i+1, params.get(i));
            }

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                for (Employee emp : data) {
                    if (emp.getEmployeeID() == employeeID) {
                        if (!txtFirstName.getText().isEmpty()) emp.setFirstName(txtFirstName.getText());
                        if (!txtLastName.getText().isEmpty()) emp.setLastName(txtLastName.getText());
                        if (!txtPosition.getText().isEmpty()) emp.setPosition(txtPosition.getText());
                        if (!txtSalary.getText().isEmpty()) emp.setSalary(Double.parseDouble(txtSalary.getText()));
                        if (!txtHireDate.getText().isEmpty()) emp.setHireDate(txtHireDate.getText());
                        table.refresh();
                        break;
                    }
                }
                showAlert("Employee updated successfully.");
            } else {
                showAlert("No employee found with that ID.");
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error updating employee.");
        }
    }
     // Load data from DB for Cars and Services (others similar)
     private ObservableList<Car> loadCarData() {
        ObservableList<Car> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cars")) {
            while (rs.next()) {
                data.add(new Car(
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
        return data;
    }

    private ObservableList<Service> loadServiceData() {
        ObservableList<Service> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM services")) {
            while (rs.next()) {
                data.add(new Service(
                        rs.getInt("ServiceID"),
                        rs.getInt("CarID"),
                        rs.getInt("CustomerID"),
                        rs.getString("ServiceDate"),
                        rs.getString("ServiceDescription"),
                        rs.getDouble("Cost")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    // Car CRUD
    private void addCar(TextField txtCarID, TextField txtMake, TextField txtModel, TextField txtYear, TextField txtPrice, TextField txtStock, TextField txtVIN, ObservableList<Car> data) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (txtCarID.getText().isEmpty() || txtMake.getText().isEmpty() || txtModel.getText().isEmpty() ||
                    txtYear.getText().isEmpty() || txtPrice.getText().isEmpty() || txtStock.getText().isEmpty() || txtVIN.getText().isEmpty()) {
                showAlert("All fields are required.");
                return;
            }
            String insertSQL = "INSERT INTO cars (CarID, Make, Model, Year, Price, Stock, VIN) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setInt(1, Integer.parseInt(txtCarID.getText()));
            pstmt.setString(2, txtMake.getText());
            pstmt.setString(3, txtModel.getText());
            pstmt.setInt(4, Integer.parseInt(txtYear.getText()));
            pstmt.setDouble(5, Double.parseDouble(txtPrice.getText()));
            pstmt.setInt(6, Integer.parseInt(txtStock.getText()));
            pstmt.setString(7, txtVIN.getText());
            pstmt.executeUpdate();
            data.add(new Car(Integer.parseInt(txtCarID.getText()), txtMake.getText(), txtModel.getText(),
                    Integer.parseInt(txtYear.getText()), Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtStock.getText()), txtVIN.getText()));
            showAlert("Car added successfully.");
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error adding car.");
        }
    }

    private void updateCar(TableView<Car> table, TextField txtCarID, TextField txtMake, TextField txtModel, TextField txtYear, TextField txtPrice, TextField txtStock, TextField txtVIN, ObservableList<Car> data) {
        if (txtCarID.getText().isEmpty()) {
            showAlert("CarID is required for update.");
            return;
        }
        int carID = Integer.parseInt(txtCarID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("UPDATE cars SET ");
            List<Object> params = new ArrayList<>();
            if (!txtMake.getText().isEmpty()) { sql.append("Make = ?, "); params.add(txtMake.getText()); }
            if (!txtModel.getText().isEmpty()) { sql.append("Model = ?, "); params.add(txtModel.getText()); }
            if (!txtYear.getText().isEmpty()) { sql.append("Year = ?, "); params.add(Integer.parseInt(txtYear.getText())); }
            if (!txtPrice.getText().isEmpty()) { sql.append("Price = ?, "); params.add(Double.parseDouble(txtPrice.getText())); }
            if (!txtStock.getText().isEmpty()) { sql.append("Stock = ?, "); params.add(Integer.parseInt(txtStock.getText())); }
            if (!txtVIN.getText().isEmpty()) { sql.append("VIN = ?, "); params.add(txtVIN.getText()); }

            if (params.isEmpty()) {
                showAlert("No fields to update.");
                return;
            }

            sql.setLength(sql.length() - 2); 
            sql.append(" WHERE CarID = ?");
            params.add(carID);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i+1, params.get(i));
            }

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                for (Car c : data) {
                    if (c.getcarId() == carID) {
                        if (!txtMake.getText().isEmpty()) c.setMake(txtMake.getText());
                        if (!txtModel.getText().isEmpty()) c.setModel(txtModel.getText());
                        if (!txtYear.getText().isEmpty()) c.setYear(Integer.parseInt(txtYear.getText()));
                        if (!txtPrice.getText().isEmpty()) c.setPrice(Double.parseDouble(txtPrice.getText()));
                        if (!txtStock.getText().isEmpty()) c.setStock(Integer.parseInt(txtStock.getText()));
                        if (!txtVIN.getText().isEmpty()) c.setVin(txtVIN.getText());
                        table.refresh();
                        break;
                    }
                }
                showAlert("Car updated successfully.");
            } else {
                showAlert("No car found with that ID.");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error updating car.");
        }
    }

    private void deleteCar(TableView<Car> table, TextField txtCarID, ObservableList<Car> data) {
        if (txtCarID.getText().isEmpty()) {
            showAlert("CarID is required for delete.");
            return;
        }
        int carID = Integer.parseInt(txtCarID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM cars WHERE CarID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, carID);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                data.removeIf(car -> car.getcarId() == carID);
                showAlert("Car deleted successfully.");
            } else {
                showAlert("No car found with that ID.");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error deleting car.");
        }
    }

    // Service CRUD methods (already shown partially)
    private void addService(TextField txtServiceID, TextField txtCarID, TextField txtCustomerID, TextField txtServiceDate, TextField txtDescription, TextField txtCost, ObservableList<Service> data) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (txtServiceID.getText().isEmpty() || txtCarID.getText().isEmpty() || txtCustomerID.getText().isEmpty() ||
                    txtServiceDate.getText().isEmpty() || txtDescription.getText().isEmpty() || txtCost.getText().isEmpty()) {
                showAlert("All fields are required for adding service.");
                return;
            }
            String sql = "INSERT INTO services (ServiceID, CarID, CustomerID, ServiceDate, ServiceDescription, Cost) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(txtServiceID.getText()));
            pstmt.setInt(2, Integer.parseInt(txtCarID.getText()));
            pstmt.setInt(3, Integer.parseInt(txtCustomerID.getText()));
            pstmt.setString(4, txtServiceDate.getText());
            pstmt.setString(5, txtDescription.getText());
            pstmt.setDouble(6, Double.parseDouble(txtCost.getText()));
            pstmt.executeUpdate();
            data.add(new Service(Integer.parseInt(txtServiceID.getText()), Integer.parseInt(txtCarID.getText()),
                    Integer.parseInt(txtCustomerID.getText()), txtServiceDate.getText(), txtDescription.getText(),
                    Double.parseDouble(txtCost.getText())));
            showAlert("Service added successfully.");
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error adding service.");
        }
    }

    private void updateService(TableView<Service> table, TextField txtServiceID, TextField txtCarID, TextField txtCustomerID, TextField txtServiceDate, TextField txtDescription, TextField txtCost, ObservableList<Service> data) {
        if (txtServiceID.getText().isEmpty()) {
            showAlert("ServiceID is required for update.");
            return;
        }
        int serviceID = Integer.parseInt(txtServiceID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("UPDATE services SET ");
            List<Object> params = new ArrayList<>();

            if (!txtCarID.getText().isEmpty()) { sql.append("CarID = ?, "); params.add(Integer.parseInt(txtCarID.getText())); }
            if (!txtCustomerID.getText().isEmpty()) { sql.append("CustomerID = ?, "); params.add(Integer.parseInt(txtCustomerID.getText())); }
            if (!txtServiceDate.getText().isEmpty()) { sql.append("ServiceDate = ?, "); params.add(txtServiceDate.getText()); }
            if (!txtDescription.getText().isEmpty()) { sql.append("ServiceDescription = ?, "); params.add(txtDescription.getText()); }
            if (!txtCost.getText().isEmpty()) { sql.append("Cost = ?, "); params.add(Double.parseDouble(txtCost.getText())); }

            if (params.isEmpty()) {
                showAlert("No fields to update.");
                return;
            }

            sql.setLength(sql.length() - 2);
            sql.append(" WHERE ServiceID = ?");
            params.add(serviceID);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i+1, params.get(i));
            }

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                for (Service s : data) {
                    if (s.getServiceID() == serviceID) {
                        if (!txtCarID.getText().isEmpty()) s.setCarID(Integer.parseInt(txtCarID.getText()));
                        if (!txtCustomerID.getText().isEmpty()) s.setCustomerID(Integer.parseInt(txtCustomerID.getText()));
                        if (!txtServiceDate.getText().isEmpty()) s.setServiceDate(txtServiceDate.getText());
                        if (!txtDescription.getText().isEmpty()) s.setServiceDescription(txtDescription.getText());
                        if (!txtCost.getText().isEmpty()) s.setCost(Double.parseDouble(txtCost.getText()));
                        table.refresh();
                        break;
                    }
                }
                showAlert("Service updated successfully.");
            } else {
                showAlert("No service found with that ID.");
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error updating service.");
        }
    }

    private void deleteService(TableView<Service> table, TextField txtServiceID, ObservableList<Service> data) {
        if (txtServiceID.getText().isEmpty()) {
            showAlert("ServiceID is required for delete.");
            return;
        }
        int serviceID = Integer.parseInt(txtServiceID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM services WHERE ServiceID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, serviceID);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                data.removeIf(s -> s.getServiceID() == serviceID);
                showAlert("Service deleted successfully.");
            } else {
                showAlert("No service found with that ID.");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error deleting service.");
        }
    }

    private void deleteEmployee(TableView<Employee> table, TextField txtEmployeeID, ObservableList<Employee> data) {
        if (txtEmployeeID.getText().isEmpty()) {
            showAlert("EmployeeID is required for delete.");
            return;
        }
        int employeeID = Integer.parseInt(txtEmployeeID.getText());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM employees WHERE EmployeeID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeID);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                data.removeIf(emp -> emp.getEmployeeID() == employeeID);
                showAlert("Employee deleted successfully.");
            } else {
                showAlert("No employee found with that ID.");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error deleting employee.");
        }
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}

module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;

    // If the MySQL driver is on the module path and recognized as an automatic module, you can require it:
    // requires mysql.connector.java;

    // Export your packages so other modules (like JavaFX runtime) can access them if needed
    exports database_access;
    exports models;
    exports ui;
    exports utils;

    // Open the packages that contain classes loaded via FXML if you're using FXML
    // Typically, you open the package with your controllers to javafx.fxml
    // If your FXML controllers are in ui package:
    opens ui to javafx.fxml;
}

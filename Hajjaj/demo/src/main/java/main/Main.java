package main;
import ui.homepage;
import ui.Loginpage;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Application.launch(homepage.class, args);
    }
}

package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Payment;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentManagement {

    public static void refreshPayments(TableView<Payment> paymentTable) {
        paymentTable.getItems().clear();
        String query = "SELECT * FROM payments";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            ObservableList<Payment> paymentList = FXCollections.observableArrayList();
            while (rs.next()) {
                paymentList.add(new Payment(
                        rs.getInt("PaymentID"),
                        rs.getInt("OrderID"),
                        rs.getString("PaymentDate"),
                        rs.getString("PaymentMethod"),
                        rs.getDouble("Amount")
                ));
            }
            paymentTable.setItems(paymentList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addPayment(int paymentID, int orderID, String paymentDate, String paymentMethod, double amount) {
        String query = "INSERT INTO payments (PaymentID, OrderID, PaymentDate, PaymentMethod, Amount) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, paymentID);
            pstmt.setInt(2, orderID);
            pstmt.setString(3, paymentDate);
            pstmt.setString(4, paymentMethod);
            pstmt.setDouble(5, amount);
            pstmt.executeUpdate();
            System.out.println("Payment added successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updatePayment(int paymentID, int orderID, String paymentDate, String paymentMethod, double amount) {
        String query = "UPDATE payments SET OrderID = ?, PaymentDate = ?, PaymentMethod = ?, Amount = ? WHERE PaymentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderID);
            pstmt.setString(2, paymentDate);
            pstmt.setString(3, paymentMethod);
            pstmt.setDouble(4, amount);
            pstmt.setInt(5, paymentID);
            pstmt.executeUpdate();
            System.out.println("Payment updated successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deletePayment(int paymentID) {
        String query = "DELETE FROM payments WHERE PaymentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, paymentID);
            pstmt.executeUpdate();
            System.out.println("Payment deleted successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void applyFilters(TableView<Payment> paymentTable, ObservableList<Payment> paymentList, TextField... filters) {
        for (TextField filter : filters) {
            filter.textProperty().addListener((observable, oldValue, newValue) ->
                    filterPayments(paymentTable, paymentList, filters));
        }
    }

    private static void filterPayments(TableView<Payment> paymentTable, ObservableList<Payment> paymentList, TextField[] filters) {
        String paymentIDFilter = filters[0].getText();
        String orderIDFilter = filters[1].getText();
        String paymentDateFilter = filters[2].getText();
        String paymentMethodFilter = filters[3].getText();
        String amountFilter = filters[4].getText();

        List<Payment> filteredPayments = new ArrayList<>();
        for (Payment payment : paymentList) {
            if (matchesFilters(payment, paymentIDFilter, orderIDFilter, paymentDateFilter, paymentMethodFilter, amountFilter)) {
                filteredPayments.add(payment);
            }
        }

        paymentTable.setItems(FXCollections.observableArrayList(filteredPayments));
    }

    private static boolean matchesFilters(Payment payment, String paymentID, String orderID, String paymentDate, String paymentMethod, String amount) {
        return (paymentID.isEmpty() || Integer.toString(payment.getPaymentID()).contains(paymentID)) &&
                (orderID.isEmpty() || Integer.toString(payment.getOrderID()).contains(orderID)) &&
                (paymentDate.isEmpty() || payment.getPaymentDate().contains(paymentDate)) &&
                (paymentMethod.isEmpty() || payment.getPaymentMethod().contains(paymentMethod)) &&
                (amount.isEmpty() || Double.toString(payment.getAmount()).contains(amount));
    }
}

package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Employee;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManagement {

    // Refresh employees
    public static void refreshEmployees(TableView<Employee> employeeTable) {
        employeeTable.getItems().clear();
        String query = "SELECT * FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            ObservableList<Employee> employeeList = FXCollections.observableArrayList();
            while (rs.next()) {
                employeeList.add(new Employee(
                        rs.getInt("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Position"),
                        rs.getDouble("Salary"),
                        rs.getString("HireDate")
                ));
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addEmployee(int employeeID, String firstName, String lastName, String position, double salary, String hireDate) {
        String query = "INSERT INTO employees (EmployeeID, FirstName, LastName, Position, Salary, HireDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeID);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, position);
            pstmt.setDouble(5, salary);
            pstmt.setString(6, hireDate);
            pstmt.executeUpdate();
            System.out.println("Employee added successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateEmployee(int employeeID, String firstName, String lastName, String position, double salary, String hireDate) {
        String query = "UPDATE employees SET FirstName = ?, LastName = ?, Position = ?, Salary = ?, HireDate = ? WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, position);
            pstmt.setDouble(4, salary);
            pstmt.setString(5, hireDate);
            pstmt.setInt(6, employeeID);
            pstmt.executeUpdate();
            System.out.println("Employee updated successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteEmployee(int employeeID) {
        String query = "DELETE FROM employees WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeID);
            pstmt.executeUpdate();
            System.out.println("Employee deleted successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void applyFilters(TableView<Employee> employeeTable, ObservableList<Employee> employeeList, TextField... filters) {
        for (TextField filter : filters) {
            filter.textProperty().addListener((observable, oldValue, newValue) ->
                    filterEmployees(employeeTable, employeeList, filters));
        }
    }

    private static void filterEmployees(TableView<Employee> employeeTable, ObservableList<Employee> employeeList, TextField[] filters) {
        String employeeIDFilter = filters[0].getText();
        String firstNameFilter = filters[1].getText();
        String lastNameFilter = filters[2].getText();
        String positionFilter = filters[3].getText();
        String salaryFilter = filters[4].getText();
        String hireDateFilter = filters[5].getText();

        List<Employee> filteredEmployees = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (matchesFilters(employee, employeeIDFilter, firstNameFilter, lastNameFilter, positionFilter, salaryFilter, hireDateFilter)) {
                filteredEmployees.add(employee);
            }
        }

        employeeTable.setItems(FXCollections.observableArrayList(filteredEmployees));
    }

    private static boolean matchesFilters(Employee employee, String employeeID, String firstName, String lastName, String position, String salary, String hireDate) {
        return (employeeID.isEmpty() || Integer.toString(employee.getEmployeeID()).contains(employeeID)) &&
                (firstName.isEmpty() || employee.getFirstName().toLowerCase().contains(firstName.toLowerCase())) &&
                (lastName.isEmpty() || employee.getLastName().toLowerCase().contains(lastName.toLowerCase())) &&
                (position.isEmpty() || employee.getPosition().toLowerCase().contains(position.toLowerCase())) &&
                (salary.isEmpty() || Double.toString(employee.getSalary()).contains(salary)) &&
                (hireDate.isEmpty() || employee.getHireDate().contains(hireDate));
    }
}

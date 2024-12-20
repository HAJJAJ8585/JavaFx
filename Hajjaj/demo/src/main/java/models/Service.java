package models;

public class Service {

    private int serviceID;
    private int carID;
    private int customerID;
    private String serviceDate;
    private String serviceDescription;
    private double cost;

    // Constructor
    public Service(int serviceID, int carID, int customerID, String serviceDate, String serviceDescription, double cost) {
        this.serviceID = serviceID;
        this.carID = carID;
        this.customerID = customerID;
        this.serviceDate = serviceDate;
        this.serviceDescription = serviceDescription;
        this.cost = cost;
    }

    // Getters and setters
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}

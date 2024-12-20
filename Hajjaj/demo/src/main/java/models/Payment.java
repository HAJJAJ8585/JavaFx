package models;

public class Payment {
  private int paymentID;
  private int orderID;
  private String paymentDate;
  private String paymentMethod;
  private double amount;

  // Constructor
  public Payment(int paymentID, int orderID, String paymentDate, String paymentMethod, double amount) {
    this.paymentID = paymentID;
    this.orderID = orderID;
    this.paymentDate = paymentDate;
    this.paymentMethod = paymentMethod;
    this.amount = amount;
  }

  // Getters and Setters
  public int getPaymentID() {
    return paymentID;
  }

  public void setPaymentID(int paymentID) {
    this.paymentID = paymentID;
  }

  public int getOrderID() {
    return orderID;
  }

  public void setOrderID(int orderID) {
    this.orderID = orderID;
  }

  public String getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

}

package models;

import javafx.beans.property.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Car {
    private final IntegerProperty carId;
    private final StringProperty make;
    private final StringProperty model;
    private final IntegerProperty year;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final StringProperty vin;

    // Constructor with parameters
    public Car(int carId, String make, String model, int year, double price, int stock, String vin) {
        this.carId = new SimpleIntegerProperty(carId);
        this.make = new SimpleStringProperty(make);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleIntegerProperty(year);
        this.price = new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.vin = new SimpleStringProperty(vin);
    }

    // Default constructor for creating an empty car object
    public Car() {
        this.carId = new SimpleIntegerProperty(0);
        this.make = new SimpleStringProperty("");
        this.model = new SimpleStringProperty("");
        this.year = new SimpleIntegerProperty(0);
        this.price = new SimpleDoubleProperty(0.0);
        this.stock = new SimpleIntegerProperty(0);
        this.vin = new SimpleStringProperty("");
    }

    public IntegerProperty carIdProperty() {
        return carId;
    }

    public StringProperty makeProperty() {
        return make;
    }

    public StringProperty modelProperty() {
        return model;
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public StringProperty vinProperty() {
        return vin;
    }

    public int getcarId() {
        return carId.get();
    }

    public String getMake() {
        return make.get();
    }

    public String getModel() {
        return model.get();
    }

    public int getYear() {
        return year.get();
    }

    public double getPrice() {
        return price.get();
    }

    public int getStock() {
        return stock.get();
    }

    public String getVin() {
        return vin.get();
    }

    public void setCarId(int carId) {
        this.carId.set(carId);
    }

    public void setMake(String make) {
        this.make.set(make);
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public void setVin(String vin) {
        this.vin.set(vin);
    }
}

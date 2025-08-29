package com.M.act1;
import jakarta.persistence.*;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    private String licensePlateNumber;
    private String make;
    private String model;
    private int year;
    private String color;
    private String bodyType;
    private String engineType;
    private String transmission;

    public Car() {}

    // Getters and Setters
    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
    public String getLicensePlateNumber() { return licensePlateNumber; }
    public void setLicensePlateNumber(String licensePlateNumber) { this.licensePlateNumber = licensePlateNumber; }
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }
    public String getEngineType() { return engineType; }
    public void setEngineType(String engineType) { this.engineType = engineType; }
    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
}

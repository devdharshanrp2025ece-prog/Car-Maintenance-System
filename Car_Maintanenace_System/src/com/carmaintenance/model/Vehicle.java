package com.carmaintenance.model;

public class Vehicle {
    private int vehicleId;
    private int customerId;
    private String registrationNumber;
    private String brand;
    private String model;
    private int manufactureYear;
    private String fuelType;

    public Vehicle() {}

    public Vehicle(int vehicleId, int customerId, String registrationNumber, String brand, String model, int manufactureYear, String fuelType) {
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.manufactureYear = manufactureYear;
        this.fuelType = fuelType;
    }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getManufactureYear() { return manufactureYear; }
    public void setManufactureYear(int manufactureYear) { this.manufactureYear = manufactureYear; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
}

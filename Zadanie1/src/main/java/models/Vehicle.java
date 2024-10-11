package models;

import java.util.UUID;

public class Vehicle {

    protected UUID vehicleId;

    protected String plateNumber;

    protected String brand;

    protected final int basePrice;

    protected boolean isAvailable;

    public Vehicle(String plateNumber, String brand, int basePrice) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.basePrice = basePrice;
        this.isAvailable = true;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public double getActualRentalPrice() {
        return basePrice;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getVehicleInfo() {
        return "Plate number: " + plateNumber + "\nBrand: " + brand + "\nBase price: " + basePrice;
    }
}
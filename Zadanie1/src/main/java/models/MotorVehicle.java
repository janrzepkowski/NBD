package models;

import jakarta.persistence.*;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class MotorVehicle extends Vehicle {

    private double engineCapacity;

    public MotorVehicle() {
    }

    public MotorVehicle(String plateNumber, String brand, int basePrice, double engineCapacity) {
        super(plateNumber, brand, basePrice);
        this.engineCapacity = engineCapacity;
    }

    public double getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = getBasePrice();
        if (engineCapacity > 2) {
            rentalPrice *= 1.5;
        } else if (engineCapacity > 1) {
            rentalPrice *= (engineCapacity * 0.5) + 0.5;
        }
        return rentalPrice;
    }

    @Override
    public String toString() {
        return super.toString() + "\nEngine capacity: " + engineCapacity;
    }

}

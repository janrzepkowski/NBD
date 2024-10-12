package models;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Car")
public class Car extends MotorVehicle {
    private char segment; // A, B, C, D, E, F

    public Car() {
    }

    public Car(String plateNumber, String brand, int basePrice, char segment, double engineCapacity) {
        super(plateNumber, brand, basePrice, engineCapacity);
        this.segment = segment;
    }

    public char getSegment() {
        return segment;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = super.getActualRentalPrice();
        if (segment == 'A') {
            rentalPrice *= 1.1;
        } else if (segment == 'B') {
            rentalPrice *= 1.2;
        } else if (segment == 'C') {
            rentalPrice *= 1.3;
        } else if (segment == 'D') {
            rentalPrice *= 1.4;
        } else if (segment == 'E' || segment == 'F') {
            rentalPrice *= 1.5;
        }
        return rentalPrice;
    }

    @Override
    public String getVehicleInfo() {
        return super.getVehicleInfo() + "\nSegment: " + segment;
    }
}

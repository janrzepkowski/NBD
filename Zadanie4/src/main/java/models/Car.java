package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

import java.util.UUID;

@Entity(defaultKeyspace = "vehicle_rental")
@CqlName("vehicles")
public class Car extends Vehicle {

    private final char segment;
    private final double engineCapacity;

    public Car(UUID vehicleId, String plateNumber, String brand, int basePrice, char segment, double engineCapacity) {
        super(vehicleId, plateNumber, brand, basePrice);
        this.segment = segment;
        this.engineCapacity = engineCapacity;
    }

    public Car(String plateNumber, String brand, int basePrice, char segment, double engineCapacity) {
        this(null, plateNumber, brand, basePrice, segment, engineCapacity);
    }

    public char getSegment() {
        return segment;
    }

    public double getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = super.getBasePrice();
        if (engineCapacity > 2) {
            rentalPrice *= 1.5;
        } else if (engineCapacity > 1) {
            rentalPrice *= (engineCapacity * 0.5) + 0.5;
        }
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
    public String toString() {
        return super.toString() + "\nSegment: " + segment + "\nEngine capacity: " + engineCapacity;
    }
}
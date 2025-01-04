package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

import java.util.UUID;

@Entity(defaultKeyspace = "vehicle_rental")
@CqlName("vehicles")
public class Moped extends Vehicle {

    private final double engineCapacity;

    public Moped(UUID vehicleId, String plateNumber, String brand, int basePrice, double engineCapacity) {
        super(vehicleId, plateNumber, brand, basePrice, "moped");
        this.engineCapacity = engineCapacity;
    }

    public Moped(String plateNumber, String brand, int basePrice, double engineCapacity) {
        this(null, plateNumber, brand, basePrice, engineCapacity);
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
        return rentalPrice;
    }

    @Override
    public String toString() {
        return super.toString() + "\nEngine capacity: " + engineCapacity;
    }
}
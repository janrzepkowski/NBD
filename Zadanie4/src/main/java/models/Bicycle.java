package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

import java.util.UUID;

@Entity(defaultKeyspace = "vehicle_rental")
@CqlName("vehicles")
public class Bicycle extends Vehicle {

    public Bicycle(UUID vehicleId, String plateNumber, String brand, int basePrice) {
        super(vehicleId, plateNumber, brand, basePrice, "bicycle");
    }

    public Bicycle() {
    }
}
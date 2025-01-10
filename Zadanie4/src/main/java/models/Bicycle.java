package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_vehicle")
@CqlName("vehicles")
public class Bicycle extends Vehicle {

    public Bicycle(long vehicleId, int basePrice, String brand) {
        super(vehicleId, basePrice, "bicycle", brand);
    }

    public Bicycle() {}

    @Override
    public double getActualRentalPrice() {
        return super.getBasePrice();
    }
}
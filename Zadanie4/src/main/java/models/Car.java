package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_vehicle")
@CqlName("vehicles")
public class Car extends Vehicle {

    @CqlName("engine_capacity")
    private int engineCapacity;

    public Car(long vehicleId, int basePrice, String discriminator, String brand, int engineCapacity) {
        super(vehicleId, basePrice, discriminator, brand);
        this.engineCapacity = engineCapacity;
    }

    public Car() {}

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
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
}
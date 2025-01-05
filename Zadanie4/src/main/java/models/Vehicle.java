package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.Objects;

@Entity(defaultKeyspace = "rent_a_vehicle")
@CqlName("vehicles")
public class Vehicle {
    @PartitionKey
    @CqlName("vehicle_id")
    private long vehicleId;

    private String discriminator;
    private String brand;
    private int basePrice;
    private int available = 1;

    public Vehicle(long vehicleId, int basePrice, String discriminator, String brand) {
        this.vehicleId = vehicleId;
        this.basePrice = basePrice;
        this.discriminator = discriminator;
        this.brand = brand;
    }

    public Vehicle() {}

    public long getVehicleId() {
        return vehicleId;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getAvailable() {
        return available;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getBrand() {
        return brand;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getActualRentalPrice() {
        return basePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vehicleId == vehicle.vehicleId && basePrice == vehicle.basePrice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, basePrice);
    }
}
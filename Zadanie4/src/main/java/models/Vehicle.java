package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.Objects;
import java.util.UUID;

@Entity(defaultKeyspace = "vehicle_rental")
@CqlName("vehicles")
public abstract class Vehicle {

    @PartitionKey
    @CqlName("vehicle_id")
    private UUID vehicleId;

    private String plateNumber;
    private String brand;
    private int basePrice;
    private boolean isAvailable;
    private boolean archived;
    private String discriminator;

    public Vehicle(UUID vehicleId, String plateNumber, String brand, int basePrice, String discriminator) {
        this.vehicleId = vehicleId != null ? vehicleId : UUID.randomUUID();
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.basePrice = basePrice;
        this.isAvailable = true;
        this.archived = false;
        this.discriminator = discriminator;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public double getActualRentalPrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", plateNumber='" + plateNumber + '\'' +
                ", brand='" + brand + '\'' +
                ", basePrice=" + basePrice +
                ", isAvailable=" + isAvailable +
                ", archived=" + archived +
                ", discriminator='" + discriminator + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return basePrice == vehicle.basePrice && isAvailable == vehicle.isAvailable && archived == vehicle.archived && Objects.equals(vehicleId, vehicle.vehicleId) && Objects.equals(plateNumber, vehicle.plateNumber) && Objects.equals(brand, vehicle.brand) && Objects.equals(discriminator, vehicle.discriminator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, plateNumber, brand, basePrice, isAvailable, archived, discriminator);
    }
}
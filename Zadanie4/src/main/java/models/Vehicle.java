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

    public Vehicle(UUID vehicleId, String plateNumber, String brand, int basePrice) {
        this.vehicleId = vehicleId != null ? vehicleId : UUID.randomUUID();
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.basePrice = basePrice;
        this.isAvailable = true;
        this.archived = false;
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return basePrice == vehicle.basePrice && isAvailable == vehicle.isAvailable && archived == vehicle.archived && Objects.equals(vehicleId, vehicle.vehicleId) && Objects.equals(plateNumber, vehicle.plateNumber) && Objects.equals(brand, vehicle.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, plateNumber, brand, basePrice, isAvailable, archived);
    }
}
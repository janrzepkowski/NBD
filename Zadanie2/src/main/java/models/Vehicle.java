package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@BsonDiscriminator("vehicle")
public abstract class Vehicle implements Serializable {

    @BsonId
    private UUID vehicleId = UUID.randomUUID();

    @BsonProperty("plateNumber")
    private String plateNumber;

    @BsonProperty("brand")
    private String brand;

    @BsonProperty("basePrice")
    private int basePrice;

    @BsonProperty("isAvailable")
    private boolean isAvailable = true;

    @BsonProperty("archived")
    private boolean archived = false;

    public Vehicle() {
    }

    public Vehicle(@BsonProperty("plateNumber") String plateNumber,
                   @BsonProperty("brand") String brand,
                   @BsonProperty("basePrice") int basePrice) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.basePrice = basePrice;
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

    public double getActualRentalPrice() {
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

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
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
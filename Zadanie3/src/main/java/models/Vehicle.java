package models;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;
import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "vehicle")
@JsonbTypeInfo({
        @JsonbSubtype(alias = "CAR", type = Car.class),
        @JsonbSubtype(alias = "MOPED", type = Moped.class)
})
public abstract class Vehicle {

    @BsonId
    private final UUID vehicleId;

    @BsonProperty("plateNumber")
    private String plateNumber;

    @BsonProperty("brand")
    private final String brand;

    @BsonProperty("basePrice")
    private int basePrice;

    @BsonProperty("Available")
    private boolean isAvailable;

    @BsonProperty("archived")
    private boolean archived;

    @BsonCreator
    public Vehicle(@BsonId UUID vehicleId,
                   @BsonProperty("plateNumber") String plateNumber,
                   @BsonProperty("brand") String brand,
                   @BsonProperty("basePrice") int basePrice) {
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

    public double getActualRentalPrice() {
        return basePrice;
    }

    public boolean getAvailable() {
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
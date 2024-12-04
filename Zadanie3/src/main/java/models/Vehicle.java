package models;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
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
        @JsonbSubtype(alias = "MOPED", type = Moped.class),
        @JsonbSubtype(alias = "BICYCLE", type = Bicycle.class),
})
public abstract class Vehicle {

    @BsonId
    @JsonbProperty("vehicleId")
    private UUID vehicleId;

    @BsonProperty("plateNumber")
    @JsonbProperty("plateNumber")
    private String plateNumber;

    @BsonProperty("brand")
    @JsonbProperty("brand")
    private String brand;

    @BsonProperty("basePrice")
    @JsonbProperty("basePrice")
    private int basePrice;

    @BsonProperty("Available")
    @JsonbProperty("Available")
    private boolean isAvailable;

    @BsonProperty("archived")
    @JsonbProperty("archived")
    private boolean archived;

    @BsonCreator
    @JsonbCreator
    public Vehicle(@BsonId @JsonbProperty("vehicleId") UUID vehicleId,
                   @BsonProperty("plateNumber") @JsonbProperty("plateNumber") String plateNumber,
                   @BsonProperty("brand") @JsonbProperty("brand") String brand,
                   @BsonProperty("basePrice") @JsonbProperty("basePrice") int basePrice) {
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
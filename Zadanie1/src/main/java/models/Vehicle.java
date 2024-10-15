package models;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Access(AccessType.FIELD)
public abstract class Vehicle implements Serializable {

    @Id
    private final UUID vehicleId = UUID.randomUUID();

    @Version
    private long version;

    @Column(name = "plateNumber")
    private String plateNumber;

    @Column(name = "brand")
    private String brand;

    @Column(name = "basePrice")
    private int basePrice;

    @Column(name = "isAvailable")
    private boolean isAvailable = true;

    @Column(name = "archived")
    private boolean archived = false;

    public Vehicle() {
    }

    public Vehicle(String plateNumber, String brand, int basePrice) {
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

    public String getVehicleInfo() {
        return "Plate number: " + plateNumber + "\nBrand: " + brand + "\nBase price: " + basePrice;
    }
}

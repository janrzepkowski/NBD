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
    protected UUID vehicleId;

    @Column(name = "plateNumber")
    protected String plateNumber;

    @Column(name = "brand")
    protected String brand;

    @Column(name = "basePrice")
    protected int basePrice;

    @Column(name = "isAvailable")
    protected boolean isAvailable;

    @Column(name = "archived")
    protected boolean archived;

    public Vehicle() {
    }

    public Vehicle(String plateNumber, String brand, int basePrice) {
        this.vehicleId = UUID.randomUUID();
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

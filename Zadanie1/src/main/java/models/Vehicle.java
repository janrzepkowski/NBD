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
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID vehicleId;

    @Column(name = "plateNumber")
    protected String plateNumber;

    @Column(name = "brand")
    protected String brand;

    @Column(name = "basePrice")
    protected int basePrice;

    @Column(name = "isAvailable")
    protected boolean isAvailable;

    public Vehicle() {
    }

    public Vehicle(String plateNumber, String brand, int basePrice) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.basePrice = basePrice;
        this.isAvailable = true;
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

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getVehicleInfo() {
        return "Plate number: " + plateNumber + "\nBrand: " + brand + "\nBase price: " + basePrice;
    }
}

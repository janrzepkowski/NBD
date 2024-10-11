package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Bicycle")
public class Bicycle extends Vehicle {
    public Bicycle(String plateNumber, String brand, int basePrice) {
        super(plateNumber, brand, basePrice);
    }
}

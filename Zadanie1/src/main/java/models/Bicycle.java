package models;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Bicycle")
public class Bicycle extends Vehicle {

    public Bicycle(String plateNumber, String brand, int basePrice) {
        super(plateNumber, brand, basePrice);
    }

    public Bicycle() {

    }
}

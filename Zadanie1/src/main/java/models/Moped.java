package models;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Moped")
public class Moped extends MotorVehicle {

    public Moped() {
    }

    public Moped(String plateNumber, String brand, int basePrice, double engineCapacity) {
        super(plateNumber, brand, basePrice, engineCapacity);
    }
}

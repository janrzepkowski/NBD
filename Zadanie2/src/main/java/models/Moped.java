package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator("Moped")
public class Moped extends MotorVehicle {

    public Moped() {
    }

    public Moped(String plateNumber, String brand, int basePrice, double engineCapacity) {
        super(plateNumber, brand, basePrice, engineCapacity);
    }
}
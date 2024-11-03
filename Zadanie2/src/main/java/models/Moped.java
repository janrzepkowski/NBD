package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "moped")
public class Moped extends MotorVehicle {

    @BsonCreator
    public Moped(@BsonId UUID vehicleId,
                 @BsonProperty("plateNumber") String plateNumber,
                 @BsonProperty("brand") String brand,
                 @BsonProperty("basePrice") int basePrice,
                 @BsonProperty("engineCapacity") double engineCapacity) {
        super(vehicleId, plateNumber, brand, basePrice, engineCapacity);
    }

    public Moped(String plateNumber, String brand, int basePrice, double engineCapacity) {
        this(null, plateNumber, brand, basePrice, engineCapacity);
    }
}
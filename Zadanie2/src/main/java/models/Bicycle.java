package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "bicycle")
public class Bicycle extends Vehicle {

    @BsonCreator
    public Bicycle(@BsonId UUID vehicleId,
                   @BsonProperty("plateNumber") String plateNumber,
                   @BsonProperty("brand") String brand,
                   @BsonProperty("basePrice") int basePrice) {
        super(vehicleId, plateNumber, brand, basePrice);
    }

    public Bicycle(String plateNumber, String brand, int basePrice) {
        this(null, plateNumber, brand, basePrice);
    }
}
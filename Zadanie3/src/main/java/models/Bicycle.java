package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "bicycle")
public class Bicycle extends Vehicle {

    @BsonCreator
    @JsonbCreator
    public Bicycle(@BsonId @JsonbProperty("vehicleId") UUID vehicleId,
                   @BsonProperty("plateNumber") @JsonbProperty("plateNumber") String plateNumber,
                   @BsonProperty("brand") @JsonbProperty("brand") String brand,
                   @BsonProperty("basePrice") @JsonbProperty("basePrice") int basePrice) {
        super(vehicleId, plateNumber, brand, basePrice);
    }

    public Bicycle(String plateNumber, String brand, int basePrice) {
        this(null, plateNumber, brand, basePrice);
    }
}
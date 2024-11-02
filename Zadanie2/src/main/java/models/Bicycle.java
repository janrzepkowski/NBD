package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;

@BsonDiscriminator("bicycle")
public class Bicycle extends Vehicle {

    public Bicycle(String plateNumber, String brand, int basePrice) {
        super(plateNumber, brand, basePrice);
    }

    public Bicycle() {
    }
}
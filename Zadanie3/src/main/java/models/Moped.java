package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "moped")
public class Moped extends Vehicle {

    @BsonProperty("engineCapacity")
    @JsonbProperty("engineCapacity")
    private final double engineCapacity;

    @BsonCreator
    @JsonbCreator
    public Moped(@BsonId @JsonbProperty("vehicleId") UUID vehicleId,
                 @BsonProperty("plateNumber") @JsonbProperty("plateNumber") String plateNumber,
                 @BsonProperty("brand") @JsonbProperty("brand") String brand,
                 @BsonProperty("basePrice") @JsonbProperty("basePrice") int basePrice,
                 @BsonProperty("engineCapacity") @JsonbProperty("engineCapacity") double engineCapacity) {
        super(vehicleId, plateNumber, brand, basePrice);
        this.engineCapacity = engineCapacity;
    }

    public Moped(String plateNumber, String brand, int basePrice, double engineCapacity) {
        this(null, plateNumber, brand, basePrice, engineCapacity);
    }

    public double getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = super.getActualRentalPrice();
        if (engineCapacity > 2) {
            rentalPrice *= 1.5;
        } else if (engineCapacity > 1) {
            rentalPrice *= (engineCapacity * 0.5) + 0.5;
        }
        return rentalPrice;
    }

    @Override
    public String toString() {
        return super.toString() + "\nEngine capacity: " + engineCapacity;
    }
}
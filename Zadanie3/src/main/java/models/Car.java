package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "car")
public class Car extends Vehicle {

    @BsonProperty("segment")
    @JsonbProperty("segment")
    private char segment; // A, B, C, D, E, F

    @BsonProperty("engineCapacity")
    @JsonbProperty("engineCapacity")
    private double engineCapacity;

    @BsonCreator
    @JsonbCreator
    public Car(@BsonId @JsonbProperty("vehicleId") UUID vehicleId,
               @BsonProperty("plateNumber") @JsonbProperty("plateNumber") String plateNumber,
               @BsonProperty("brand") @JsonbProperty("brand") String brand,
               @BsonProperty("basePrice") @JsonbProperty("basePrice") int basePrice,
               @BsonProperty("segment") @JsonbProperty("segment") char segment,
               @BsonProperty("engineCapacity") @JsonbProperty("engineCapacity") double engineCapacity) {
        super(vehicleId, plateNumber, brand, basePrice);
        this.segment = segment;
        this.engineCapacity = engineCapacity;
    }

    public Car(String plateNumber, String brand, int basePrice, char segment, double engineCapacity) {
        this(null, plateNumber, brand, basePrice, segment, engineCapacity);
    }

    public char getSegment() {
        return segment;
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
        if (segment == 'A') {
            rentalPrice *= 1.1;
        } else if (segment == 'B') {
            rentalPrice *= 1.2;
        } else if (segment == 'C') {
            rentalPrice *= 1.3;
        } else if (segment == 'D') {
            rentalPrice *= 1.4;
        } else if (segment == 'E' || segment == 'F') {
            rentalPrice *= 1.5;
        }
        return rentalPrice;
    }

    @Override
    public String toString() {
        return super.toString() + "\nSegment: " + segment + "\nEngine capacity: " + engineCapacity;
    }
}
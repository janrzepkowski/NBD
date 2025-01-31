package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "car")
public class Car extends MotorVehicle {

    @BsonProperty("segment")
    private char segment; // A, B, C, D, E, F

    @BsonCreator
    public Car(@BsonId UUID vehicleId,
               @BsonProperty("plateNumber") String plateNumber,
               @BsonProperty("brand") String brand,
               @BsonProperty("basePrice") int basePrice,
               @BsonProperty("segment") char segment,
               @BsonProperty("engineCapacity") double engineCapacity) {
        super(vehicleId, plateNumber, brand, basePrice, engineCapacity);
        this.segment = segment;
    }

    public Car(String plateNumber, String brand, int basePrice, char segment, double engineCapacity) {
        this(null, plateNumber, brand, basePrice, segment, engineCapacity);
    }

    public char getSegment() {
        return segment;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = super.getActualRentalPrice();
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
        return super.toString() + "\nSegment: " + segment;
    }
}
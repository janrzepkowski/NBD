package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "_type", value = "car")
public class Car extends MotorVehicle {

    @BsonProperty("segment")
    private char segment; // A, B, C, D, E, F

    public Car() {
    }

    public Car(String plateNumber, String brand, int basePrice, char segment, double engineCapacity) {
        super(plateNumber, brand, basePrice, engineCapacity);
        this.segment = segment;
    }

    public char getSegment() {
        return segment;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = super.getActualRentalPrice();
        switch (segment) {
            case 'A': rentalPrice *= 1.1; break;
            case 'B': rentalPrice *= 1.2; break;
            case 'C': rentalPrice *= 1.3; break;
            case 'D': rentalPrice *= 1.4; break;
            case 'E': rentalPrice *= 1.5; break;
            case 'F': rentalPrice *= 1.6; break;
        }
        return rentalPrice;
    }

    @Override
    public String toString() {
        return super.toString() + "\nSegment: " + segment;
    }
}
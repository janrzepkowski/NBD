package models;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "motorVehicle")
@JsonbTypeInfo({
        @JsonbSubtype(alias = "CAR", type = Car.class),
        @JsonbSubtype(alias = "MOPED", type = Moped.class)
})

public abstract class MotorVehicle extends Vehicle {

    @BsonProperty("engineCapacity")
    private final double engineCapacity;

    @BsonCreator
    public MotorVehicle(@BsonId UUID vehicleId,
                        @BsonProperty("plateNumber") String plateNumber,
                        @BsonProperty("brand") String brand,
                        @BsonProperty("basePrice") int basePrice,
                        @BsonProperty("engineCapacity") double engineCapacity) {
        super(vehicleId, plateNumber, brand, basePrice);
        this.engineCapacity = engineCapacity;
    }

    public double getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = getBasePrice();
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
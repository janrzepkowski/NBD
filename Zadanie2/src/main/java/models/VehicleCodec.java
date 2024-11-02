package models;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.BsonType;

public class VehicleCodec implements Codec<Vehicle> {

    @Override
    public void encode(BsonWriter writer, Vehicle vehicle, EncoderContext encoderContext) {
        writer.writeStartDocument();

        if (vehicle instanceof Bicycle) {
            writer.writeString("_type", "bicycle");
        } else if (vehicle instanceof Moped) {
            writer.writeString("_type", "moped");
        } else if (vehicle instanceof Car) {
            writer.writeString("_type", "car");
        }

        writer.writeString("plateNumber", vehicle.getPlateNumber());
        writer.writeString("brand", vehicle.getBrand());
        writer.writeInt32("basePrice", vehicle.getBasePrice());
        writer.writeBoolean("isAvailable", vehicle.isAvailable());
        writer.writeBoolean("archived", vehicle.isArchived());

        if (vehicle instanceof MotorVehicle) {
            writer.writeDouble("engineCapacity", ((MotorVehicle) vehicle).getEngineCapacity());
        }

        if (vehicle instanceof Car) {
            writer.writeString("segment", String.valueOf(((Car) vehicle).getSegment()));
        }

        writer.writeEndDocument();
    }

    @Override
    public Vehicle decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String type = null;
        String plateNumber = null;
        String brand = null;
        int basePrice = 0;
        boolean isAvailable = true;
        boolean archived = false;
        double engineCapacity = 0;
        char segment = 'A';

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            switch (fieldName) {
                case "_type":
                    type = reader.readString();
                    break;
                case "plateNumber":
                    plateNumber = reader.readString();
                    break;
                case "brand":
                    brand = reader.readString();
                    break;
                case "basePrice":
                    basePrice = reader.readInt32();
                    break;
                case "isAvailable":
                    isAvailable = reader.readBoolean();
                    break;
                case "archived":
                    archived = reader.readBoolean();
                    break;
                case "engineCapacity":
                    engineCapacity = reader.readDouble();
                    break;
                case "segment":
                    segment = reader.readString().charAt(0);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.readEndDocument();

        return switch (type) {
            case "bicycle" -> new Bicycle(plateNumber, brand, basePrice);
            case "moped" -> new Moped(plateNumber, brand, basePrice, engineCapacity);
            case "car" -> new Car(plateNumber, brand, basePrice, segment, engineCapacity);
            case null, default -> throw new IllegalArgumentException("Unsupported vehicle type: " + type);
        };
    }

    @Override
    public Class<Vehicle> getEncoderClass() {
        return Vehicle.class;
    }
}
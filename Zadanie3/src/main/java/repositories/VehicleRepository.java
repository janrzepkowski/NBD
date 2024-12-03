package repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.Vehicle;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VehicleRepository extends AbstractMongoRepository implements IVehicleRepository {

    @Override
    public void create(Vehicle vehicle) {
        MongoCollection<Vehicle> collection = getDatabase().getCollection("vehicles", Vehicle.class);
        collection.insertOne(vehicle);
    }

    @Override
    public Vehicle read(UUID id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Vehicle> collection = getDatabase().getCollection("vehicles", Vehicle.class);
        FindIterable<Vehicle> vehicles = collection.find(filter);
        return vehicles.first();
    }

    @Override
    public List<Vehicle> readAll() {
        MongoCollection<Vehicle> collection = getDatabase().getCollection("vehicles", Vehicle.class);
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public void update(Vehicle vehicle) {
        Bson filter = Filters.eq("_id", vehicle.getVehicleId());
        MongoCollection<Vehicle> collection = getDatabase().getCollection("vehicles", Vehicle.class);
        Bson updates = Updates.combine(
                Updates.set("plateNumber", vehicle.getPlateNumber()),
                Updates.set("brand", vehicle.getBrand()),
                Updates.set("basePrice", vehicle.getBasePrice()),
                Updates.set("isAvailable", vehicle.getAvailable()),
                Updates.set("archived", vehicle.isArchived())
        );
        collection.findOneAndUpdate(filter, updates);
    }

    @Override
    public void delete(Vehicle vehicle) {
        Bson filter = Filters.eq("_id", vehicle.getVehicleId());
        MongoCollection<Vehicle> collection = getDatabase().getCollection("vehicles", Vehicle.class);
        collection.findOneAndDelete(filter);
    }
}
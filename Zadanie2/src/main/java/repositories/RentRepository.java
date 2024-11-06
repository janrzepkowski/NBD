package repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.Client;
import models.Rent;
import models.Vehicle;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class RentRepository extends AbstractMongoRepository {

    public void create(Rent rent) {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        collection.insertOne(rent);
    }

    public Rent read(UUID id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        FindIterable<Rent> rents = collection.find(filter);
        return rents.first();
    }

    public ArrayList<Rent> readAll() {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        return collection.find().into(new ArrayList<>());
    }

    public void update(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getRentId());
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        Bson updates = Updates.combine(
                Updates.set("client", rent.getClient()),
                Updates.set("vehicle", rent.getVehicle()),
                Updates.set("rentStart", rent.getRentStart()),
                Updates.set("rentEnd", rent.getRentEnd()),
                Updates.set("rentCost", rent.getRentCost()),
                Updates.set("archived", rent.isArchived())
        );
        collection.findOneAndUpdate(filter, updates);
    }

    public void delete(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getRentId());
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        collection.findOneAndDelete(filter);
    }

    public void bookVehicle(Client client, Vehicle vehicle, LocalDateTime rentStart) {
        MongoCollection<Vehicle> vehicleCollection = getDatabase().getCollection("vehicles", Vehicle.class);
        MongoCollection<Client> clientCollection = getDatabase().getCollection("clients", Client.class);
        MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);

        Bson vehicleFilter = Filters.eq("_id", vehicle.getVehicleId());
        Bson clientFilter = Filters.eq("_id", client.getClientId());

        vehicleCollection.findOneAndUpdate(vehicleFilter, Updates.set("isAvailable", false));
        clientCollection.findOneAndUpdate(clientFilter, Updates.inc("rents", 1));

        Rent rent = new Rent(client, vehicle, rentStart);
        rentCollection.insertOne(rent);
    }

    public void returnVehicle(Rent rent) {
        MongoCollection<Vehicle> vehicleCollection = getDatabase().getCollection("vehicles", Vehicle.class);
        MongoCollection<Client> clientCollection = getDatabase().getCollection("clients", Client.class);
        MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);

        Bson vehicleFilter = Filters.eq("_id", rent.getVehicle().getVehicleId());
        Bson clientFilter = Filters.eq("_id", rent.getClient().getClientId());

        vehicleCollection.findOneAndUpdate(vehicleFilter, Updates.set("isAvailable", true));
        clientCollection.findOneAndUpdate(clientFilter, Updates.inc("rents", -1));

        rent.endRent(LocalDateTime.now());
        Bson rentFilter = Filters.eq("_id", rent.getRentId());
        Bson updates = Updates.combine(
                Updates.set("rentEnd", rent.getRentEnd()),
                Updates.set("rentCost", rent.getRentCost()),
                Updates.set("archived", rent.isArchived())
        );
        rentCollection.findOneAndUpdate(rentFilter, updates);
    }
}
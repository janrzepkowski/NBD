package repositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.Client;
import models.Rent;
import models.Vehicle;
import org.bson.conversions.Bson;
import kafka.CustomKafkaProducer;

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
        try (ClientSession clientSession = getMongoClient().startSession()) {
            clientSession.startTransaction();

            MongoCollection<Vehicle> vehicleCollection = getDatabase().getCollection("vehicles", Vehicle.class);
            MongoCollection<Client> clientCollection = getDatabase().getCollection("clients", Client.class);
            MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);

            Bson vehicleFilter = Filters.eq("_id", vehicle.getVehicleId());
            Bson clientFilter = Filters.eq("_id", client.getClientId());

            Vehicle existingVehicle = vehicleCollection.find(vehicleFilter).first();
            if (existingVehicle == null || !existingVehicle.getAvailable()) {
                throw new IllegalStateException("Booking failed: Vehicle does not exist or is already unavailable.");
            }
            existingVehicle.setAvailable(false);
            vehicleCollection.replaceOne(clientSession, vehicleFilter, existingVehicle);

            clientCollection.findOneAndUpdate(clientSession, clientFilter, Updates.inc("rents", 1));

            Client updatedClient = clientCollection.find(clientFilter).first();
            Vehicle updatedVehicle = vehicleCollection.find(vehicleFilter).first();

            Rent rent = new Rent(updatedClient, updatedVehicle, rentStart);
            rentCollection.insertOne(clientSession, rent);

            clientSession.commitTransaction();

            CustomKafkaProducer kafkaProducer = new CustomKafkaProducer();
            kafkaProducer.sendRent(rent);

        } catch (Exception e) {
            throw new RuntimeException("Booking vehicle failed", e);
        }
    }

    public void returnVehicle(Rent rent) {
        try (ClientSession clientSession = getMongoClient().startSession()) {
            clientSession.startTransaction();

            MongoCollection<Vehicle> vehicleCollection = getDatabase().getCollection("vehicles", Vehicle.class);
            MongoCollection<Client> clientCollection = getDatabase().getCollection("clients", Client.class);
            MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);

            Bson vehicleFilter = Filters.eq("_id", rent.getVehicle().getVehicleId());
            Bson clientFilter = Filters.eq("_id", rent.getClient().getClientId());

            Vehicle existingVehicle = vehicleCollection.find(vehicleFilter).first();
            if (existingVehicle == null || existingVehicle.getAvailable()) {
                throw new IllegalStateException("Returning failed: Vehicle does not exist or is already available.");
            }
            existingVehicle.setAvailable(true);
            vehicleCollection.replaceOne(clientSession, vehicleFilter, existingVehicle);

            clientCollection.findOneAndUpdate(clientSession, clientFilter, Updates.inc("rents", -1));

            Client updatedClient = clientCollection.find(clientFilter).first();
            Vehicle updatedVehicle = vehicleCollection.find(vehicleFilter).first();

            rent.endRent(LocalDateTime.now());

            Rent updatedRent = new Rent(rent.getRentId(), updatedClient, updatedVehicle, rent.getRentStart());
            updatedRent.endRent(rent.getRentEnd());

            Bson rentFilter = Filters.eq("_id", rent.getRentId());
            Bson updates = Updates.combine(
                    Updates.set("client", updatedClient),
                    Updates.set("vehicle", updatedVehicle),
                    Updates.set("rentEnd", updatedRent.getRentEnd()),
                    Updates.set("rentCost", updatedRent.getRentCost()),
                    Updates.set("archived", updatedRent.isArchived())
            );
            rentCollection.findOneAndUpdate(clientSession, rentFilter, updates);

            clientSession.commitTransaction();
        } catch (Exception e) {
            throw new RuntimeException("Returning vehicle failed", e);
        }
    }
}
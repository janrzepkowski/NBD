package repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.Client;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.UUID;

public class ClientRepository extends AbstractMongoRepository {

    public void create(Client client) {
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        collection.insertOne(client);
    }

    public Client read(UUID id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        FindIterable<Client> clients = collection.find(filter);
        return clients.first();
    }

    public ArrayList<Client> readAll() {
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        return collection.find().into(new ArrayList<>());
    }

    public void update(Client client) {
        Bson filter = Filters.eq("_id", client.getClientId());
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        Bson updates = Updates.combine(
                Updates.set("firstName", client.getFirstName()),
                Updates.set("lastName", client.getLastName()),
                Updates.set("phoneNumber", client.getPhoneNumber()),
                Updates.set("rents", client.getRents()),
                Updates.set("archived", client.isArchived())
        );
        collection.findOneAndUpdate(filter, updates);
    }

    public void delete(Client client) {
        Bson filter = Filters.eq("_id", client.getClientId());
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        collection.findOneAndDelete(filter);
    }
}
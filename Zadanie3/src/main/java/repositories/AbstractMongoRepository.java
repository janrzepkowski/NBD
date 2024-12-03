package repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import models.*;
import org.bson.BsonType;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public abstract class AbstractMongoRepository implements AutoCloseable {

    private final ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017,mongodb2:27017,mongodb3:27017/?replicaSet=replica_set_single");
    private final MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(
                    PojoCodecProvider.builder()
                            .automatic(true)
                            .conventions(Conventions.DEFAULT_CONVENTIONS)
                            .register(Vehicle.class, Car.class, Moped.class, Bicycle.class, Rent.class, Client.class)
                            .build()
            )
    );

    private MongoClient mongoClient;
    private MongoDatabase database;

    private void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(pojoCodecRegistry)
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("rental");
        if (!getDatabase().listCollectionNames().into(new ArrayList<>()).contains("vehicles")) {
            createVehiclesCollection();
        }
    }

    private void createVehiclesCollection() {
        Bson isAvailableType = Filters.type("available", BsonType.BOOLEAN);
        Bson isAvailableYes = Filters.eq("available", true);
        Bson isAvailableNo = Filters.eq("available", false);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(Filters.and(isAvailableType, Filters.or(isAvailableYes, isAvailableNo)));

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
        getDatabase().createCollection("vehicles", createCollectionOptions);
    }

    public MongoDatabase getDatabase() {
        if (database == null) {
            initDbConnection();
        }
        return database;
    }

    public MongoClient getMongoClient() {
        if (mongoClient == null) {
            initDbConnection();
        }
        return mongoClient;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
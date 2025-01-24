import kafka.CustomKafkaProducer;
import kafka.KafkaConsumer;
import managers.ClientManager;
import managers.RentManager;
import managers.VehicleManager;
import models.Car;
import models.Client;
import models.Rent;
import repositories.AbstractMongoRepository;
import repositories.ClientRepository;
import repositories.RentRepository;
import repositories.VehicleRepository;

import com.mongodb.client.MongoDatabase;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AbstractMongoRepository mongoRepository = new AbstractMongoRepository() {};
        MongoDatabase database = mongoRepository.getDatabase();

        database.getCollection("vehicles").drop();
        database.getCollection("clients").drop();
        database.getCollection("rents").drop();
        database.getCollection("messages").drop();

        VehicleRepository vehicleRepository = new VehicleRepository();
        ClientRepository clientRepository = new ClientRepository();
        RentRepository rentRepository = new RentRepository();

        VehicleManager vehicleManager = new VehicleManager(vehicleRepository);
        ClientManager clientManager = new ClientManager(clientRepository);
        RentManager rentManager = new RentManager(rentRepository);

        Client client = new Client("John", "Doe", "1234567890");
        clientManager.registerClient(client);

        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        vehicleManager.registerVehicle(car);

        CustomKafkaProducer producer = new CustomKafkaProducer();

        try {
            rentManager.rentVehicle(client, car, LocalDateTime.now());
            System.out.println("Rent created for client and vehicle:");
            Rent rent = rentRepository.readAll().get(0);
            System.out.println(rent);

            producer.sendRent(rent);

            KafkaConsumer kafkaConsumer = new KafkaConsumer(2);
            kafkaConsumer.initConsumers();
            kafkaConsumer.consumeTopicByAllConsumers();

            Thread.sleep(10000);

            System.out.println("Messages saved in MongoDB:");
            database.getCollection("messages").find().forEach(doc -> System.out.println(doc.toJson()));

            rentManager.returnVehicle(rent.getRentId(), LocalDateTime.now().plusDays(2));
            System.out.println("Vehicle returned:");
            System.out.println(rentRepository.read(rent.getRentId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
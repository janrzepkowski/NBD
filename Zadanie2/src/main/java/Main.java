//import managers.ClientManager;
//import managers.RentManager;
//import managers.VehicleManager;
//import models.Car;
//import models.Client;
//import models.Rent;
//import repositories.AbstractMongoRepository;
//import repositories.ClientRepository;
//import repositories.RentRepository;
//import repositories.VehicleRepository;
//
//import com.mongodb.client.MongoDatabase;
//
//import java.time.LocalDateTime;
//
//public class Main {
//    public static void main(String[] args) {
//
//        AbstractMongoRepository mongoRepository = new AbstractMongoRepository() {};
//        MongoDatabase database = mongoRepository.getDatabase();
//
//        database.getCollection("vehicles").drop();
//        database.getCollection("clients").drop();
//        database.getCollection("rents").drop();
//
//        VehicleRepository vehicleRepository = new VehicleRepository();
//        ClientRepository clientRepository = new ClientRepository();
//        RentRepository rentRepository = new RentRepository();
//
//        VehicleManager vehicleManager = new VehicleManager(vehicleRepository);
//        ClientManager clientManager = new ClientManager(clientRepository);
//        RentManager rentManager = new RentManager(rentRepository);
//
//        Client client = new Client("John", "Doe", "1234567890");
//        clientManager.registerClient(client);
//
//        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
//        vehicleManager.registerVehicle(car);
//
//        try {
//            rentManager.rentVehicle(client, car, LocalDateTime.now());
//            System.out.println("Rent created for client and vehicle:");
//            Rent rent = rentRepository.readAll().get(0);
//            System.out.println(rent);
//
//            rentManager.returnVehicle(rent.getRentId(), LocalDateTime.now().plusDays(2));
//            System.out.println("Vehicle returned:");
//            System.out.println(rentRepository.read(rent.getRentId()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
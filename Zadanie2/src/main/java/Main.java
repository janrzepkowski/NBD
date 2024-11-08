import managers.ClientManager;
import managers.RentManager;
import managers.VehicleManager;
import models.Car;
import models.Client;
import models.Rent;
import repositories.ClientRepository;
import repositories.RentRepository;
import repositories.VehicleRepository;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        // Initialize repositories and managers
        VehicleRepository vehicleRepository = new VehicleRepository();
        ClientRepository clientRepository = new ClientRepository();
        RentRepository rentRepository = new RentRepository();

        VehicleManager vehicleManager = new VehicleManager(vehicleRepository);
        ClientManager clientManager = new ClientManager(clientRepository);
        RentManager rentManager = new RentManager(rentRepository);

        // Register a client
        Client client = new Client("John", "Doe", "1234567890");
        clientManager.registerClient(client);

        // Register a vehicle
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        vehicleManager.registerVehicle(car);

        // Create a rent for the client and the vehicle
        try {
            rentManager.rentVehicle(client, car, LocalDateTime.now());
            System.out.println("Rent created for client and vehicle:");
            Rent rent = rentRepository.readAll().get(0); // Assuming the first rent is the one we just created
            System.out.println(rent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
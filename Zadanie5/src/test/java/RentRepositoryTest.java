import models.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;
import repositories.RentRepository;
import repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RentRepositoryTest {

    private static RentRepository rentRepository;
    private static VehicleRepository vehicleRepository;
    private static ClientRepository clientRepository;

    @BeforeAll
    public static void setUp() {
        rentRepository = new RentRepository();
        clientRepository = new ClientRepository();
        vehicleRepository = new VehicleRepository();
    }

    @BeforeEach
    public void cleanUp() {
        rentRepository.getDatabase().getCollection("rents", Rent.class).drop();
        clientRepository.getDatabase().getCollection("clients", Client.class).drop();
        vehicleRepository.getDatabase().getCollection("vehicles", Vehicle.class).drop();
    }

    @AfterAll
    public static void tearDown() {
        rentRepository.getDatabase().getCollection("rents", Rent.class).drop();
        clientRepository.getDatabase().getCollection("clients", Client.class).drop();
        vehicleRepository.getDatabase().getCollection("vehicles", Vehicle.class).drop();
        rentRepository.close();
        clientRepository.close();
        vehicleRepository.close();
    }

    @Test
    void testAddRent() {
        Client client = new Client("John", "Doe", "11111111110");
        Car car = new Car("MAG 1C", "BMW", 150, 'D', 2.5);

        clientRepository.create(client);
        vehicleRepository.create(car);

        Rent rent = new Rent(client, car, LocalDateTime.now());
        rentRepository.create(rent);
        Rent foundRent = rentRepository.read(rent.getRentId());

        assertEquals(rent, foundRent);
    }

    @Test
    void testRemoveRent() {
        Client client = new Client("Jane", "Doe", "11111111111");
        Car car = new Car("AMS 1", "Ford", 120, 'D', 2.2);

        clientRepository.create(client);
        vehicleRepository.create(car);

        Rent rent = new Rent(client, car, LocalDateTime.now());
        rentRepository.create(rent);

        Rent foundRent1 = rentRepository.read(rent.getRentId());
        assertNotNull(foundRent1);

        rentRepository.delete(rent);

        Rent foundRent2 = rentRepository.read(rent.getRentId());
        assertNull(foundRent2);
    }

    @Test
    void testUpdateRent() {
        Client client = new Client("Robert", "Smith", "22222222221");
        Car car = new Car("QRS789", "Vespa", 80, 'A', 2.0);
        Rent rent = new Rent(client, car, LocalDateTime.now());

        clientRepository.create(client);
        vehicleRepository.create(car);
        rentRepository.create(rent);

        UUID rentId = rent.getRentId();
        Rent retrievedRent = rentRepository.read(rentId);
        assertNotNull(retrievedRent);
        assertEquals(rentId, retrievedRent.getRentId());
    }

    @Test
    void testGetAllRents() {
        List<Rent> rents = rentRepository.readAll();
        int initialSize = rents.size();

        Client client1 = new Client("Alice", "Johnson", "33333333332");
        Client client2 = new Client("Michael", "Brown", "44444444443");
        Car car1 = new Car("JKL456", "Mazda", 110, 'A', 1.6);
        Car car2 = new Car("MNO789", "Subaru", 160, 'B', 2.0);

        clientRepository.create(client1);
        clientRepository.create(client2);
        vehicleRepository.create(car1);
        vehicleRepository.create(car2);

        Rent rent1 = new Rent(client1, car1, LocalDateTime.now());
        Rent rent2 = new Rent(client2, car2, LocalDateTime.now());

        rentRepository.create(rent1);
        rentRepository.create(rent2);

        rents = rentRepository.readAll();
        int finalSize = rents.size();

        assertEquals(initialSize + 2, finalSize);
    }

    @Test
    void testBookVehicle() {
        Client client = new Client("Lucas", "White", "55555555554");
        Car car = new Car("XYZ789", "Toyota", 100, 'B', 1.8);

        clientRepository.create(client);
        vehicleRepository.create(car);

        rentRepository.bookVehicle(client, car, LocalDateTime.now());

        Vehicle foundCar = vehicleRepository.read(car.getVehicleId());
        assertFalse(foundCar.getAvailable());

        Client foundClient = clientRepository.read(client.getClientId());
        assertEquals(1, foundClient.getRents());
    }
}
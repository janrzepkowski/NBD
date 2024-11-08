import managers.RentManager;
import models.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;
import repositories.RentRepository;
import repositories.VehicleRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RentManagerTest {

    private static RentRepository rentRepository;
    private static RentManager rentManager;
    private static ClientRepository clientRepository;
    private static VehicleRepository vehicleRepository;

    @BeforeAll
    public static void setUp() {
        rentRepository = new RentRepository();
        rentManager = new RentManager(rentRepository);
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
    void testRentVehicleWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new RentManager(null));
        assertEquals("rentRepository cannot be null", exception.getMessage());
    }

    @Test
    void testRentVehicleWithUnavailableVehicle() {
        Client client = new Client("Tyler", "Okonma", "1234567890");
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);

        clientRepository.create(client);
        vehicleRepository.create(car);
        car.setAvailable(false);

        Exception exception = assertThrows(Exception.class, () -> rentManager.rentVehicle(client, car, LocalDateTime.now()));

        String expectedMessage = "Vehicle is not available: " + car.getVehicleId();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testRentVehicleWithTooManyRents() {
        Client client = new Client("Tyler", "Okonma", "1234567890");
        clientRepository.create(client);
        for (int i = 1; i <= 5; i++) {
            Car car = new Car("ABC123" + i, "Toyota" + i, 100 + i, 'B', 1.8 + i);
            vehicleRepository.create(car);
            rentRepository.bookVehicle(client, car, LocalDateTime.now().minusDays(i));
        }
        Car car6 = new Car("XYZ789", "Toyota6", 106, 'B', 2.4);
        vehicleRepository.create(car6);

        Exception exception = assertThrows(Exception.class, () -> rentManager.rentVehicle(client, car6, LocalDateTime.now()));

        String expectedMessage = "Client has reached the maximum number of rents: " + client.getClientId();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testReturnVehicle() throws Exception {
        Client client = new Client("Tyler", "Okonma", "1234567890");
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        clientRepository.create(client);
        vehicleRepository.create(car);
        rentManager.rentVehicle(client, car, LocalDateTime.now());

        Client foundClient1 = clientRepository.read(client.getClientId());
        assertEquals(1, foundClient1.getRents());

        Rent rent = rentRepository.readAll().getFirst();
        rentManager.returnVehicle(rent.getRentId(), LocalDateTime.now().plusDays(1));

        Client foundClient2 = clientRepository.read(client.getClientId());
        assertEquals(0, foundClient2.getRents());
        assertTrue(car.isAvailable());
    }
}
import jakarta.persistence.*;
import managers.RentManager;
import managers.ClientManager;
import managers.VehicleManager;
import models.Client;
import models.Rent;
import models.Vehicle;
import models.Car;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;
import repositories.RentRepository;
import repositories.VehicleRepository;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RentManagerTest {
    private static RentRepository rentRepository;
    private static RentManager rentManager;
    private static ClientRepository clientRepository;
    private static ClientManager clientManager;
    private static VehicleRepository vehicleRepository;
    private static VehicleManager vehicleManager;
    private static EntityManager em;
    private static EntityManager em1;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        rentRepository = new RentRepository(entityManager);
        rentManager = new RentManager(rentRepository);
        clientRepository = new ClientRepository(entityManager);
        clientManager = new ClientManager(clientRepository);
        vehicleRepository = new VehicleRepository(entityManager);
        vehicleManager = new VehicleManager(vehicleRepository);
        em = entityManagerFactory.createEntityManager();
        em1 = entityManagerFactory.createEntityManager();
    }

    @Test
    void testRentVehicleWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RentManager(null);
        });
        assertEquals("rentRepository cannot be null", exception.getMessage());
    }

    @Test
    void testRentVehicleWithUnavailableVehicle() throws Exception {
        Client client = new Client("Tyler", "Okonma", "1234567890");
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);

        clientRepository.add(client);
        vehicleRepository.add(car);
        car.setAvailable(false);

        Exception exception = assertThrows(Exception.class, () -> {
            rentManager.rentVehicle(client, car, LocalDateTime.now());
        });

        String expectedMessage = "Vehicle is not available: " + car.getVehicleId();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Unexpected exception message: " + actualMessage);
    }

    @Test
    void testRentVehicleWithTooManyRents() throws Exception {
        Client client = new Client("Tyler", "Okonma", "1234567890");
        clientRepository.add(client);
        for (int i = 1; i <= 5; i++) {
            Car car = new Car("ABC123" + i, "Toyota" + i, 100 + i, 'B', 1.8 + i);
            vehicleRepository.add(car);
            rentRepository.bookVehicle(client, car, LocalDateTime.now().minusDays(i));
        }
        Car car6 = new Car("XYZ789", "Toyota6", 106, 'B', 2.4);
        vehicleRepository.add(car6);

        Exception exception = assertThrows(Exception.class, () -> {
            rentManager.rentVehicle(client, car6, LocalDateTime.now());
        });

        String expectedMessage = "Client has reached the maximum number of rents: " + client.getClientId();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Unexpected exception message: " + actualMessage);
    }

    @Test
    void testReturnVehicle() throws Exception {
        Client client = new Client("Tyler", "Okonma", "1234567890");
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        clientRepository.add(client);
        vehicleRepository.add(car);
        rentManager.rentVehicle(client, car, LocalDateTime.now());

        assertEquals(1, client.getRents());

        Rent rent = rentRepository.getAll().get(0);
        rentManager.returnVehicle(rent.getRentId(), LocalDateTime.now().plusDays(1));

        assertEquals(0, client.getRents());
        assertTrue(car.isAvailable());
    }
}
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import managers.RentManager;
import models.Client;
import models.Rent;
import models.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.AbstractCassandraRepository;
import repositories.ClientRepository;
import repositories.RentRepository;
import repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentManagerTest {
    private static CqlSession session;
    private static RentRepository rentRepository;
    private static ClientRepository clientRepository;
    private static VehicleRepository vehicleRepository;
    private static RentManager rentManager;

    @BeforeAll
    static void setUp() {
        AbstractCassandraRepository abstractCassandraRepository = new AbstractCassandraRepository();
        session = abstractCassandraRepository.getSession();
        vehicleRepository = new VehicleRepository(session);
        rentRepository = new RentRepository(session);
        clientRepository = new ClientRepository(session);
        rentManager = new RentManager(rentRepository, clientRepository, vehicleRepository);
    }

    @AfterEach
    void cleanUp() {
        Truncate truncateRentsByClient = QueryBuilder.truncate("rents_by_client");
        session.execute(truncateRentsByClient.build());
        Truncate truncateRentsByVehicle = QueryBuilder.truncate("rents_by_vehicle");
        session.execute(truncateRentsByVehicle.build());
        Truncate truncateVehicles = QueryBuilder.truncate("vehicles");
        session.execute(truncateVehicles.build());
        Truncate truncateClients = QueryBuilder.truncate("clients");
        session.execute(truncateClients.build());
    }

    @Test
    void testRentManagerWithNullRepositories() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new RentManager(null, null, null));
        assertEquals("rentRepository cannot be null", exception.getMessage());
    }

    @Test
    void testRentVehicle() {
        Client client = new Client(1, "Jane", "Smith", "987654321", false);
        Car car = new Car(1, 200, "car", "Toyota", 2);
        clientRepository.create(client);
        vehicleRepository.create(car);

        rentManager.rentVehicle(1, client, car, LocalDateTime.now());

        List<Rent> rentsByClient = rentManager.findRentsByClientId(client.getClientId());
        assertFalse(rentsByClient.isEmpty());

        List<Rent> rentsByVehicle = rentManager.findRentsByVehicleId(car.getVehicleId());
        assertFalse(rentsByVehicle.isEmpty());
    }

    @Test
    void testRentVehicleClientMaxRents() {
        Client client = new Client(1, "Jane", "Smith", "987654321", false);
        Car car = new Car(1, 200, "car", "Toyota", 2);
        client.setRents(5);
        clientRepository.create(client);
        vehicleRepository.create(car);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rentManager.rentVehicle(1, client, car, LocalDateTime.now());
        });

        assertEquals("Client has reached the maximum number of rents: 5", exception.getMessage());
    }

    @Test
    void testReturnVehicle() {
        Client client = new Client(1, "Jane", "Smith", "987654321", false);
        Car car = new Car(1, 200, "car", "Toyota", 2);
        clientRepository.create(client);
        vehicleRepository.create(car);

        rentManager.rentVehicle(1, client, car, LocalDateTime.now());

        List<Rent> rents = rentManager.findRentsByVehicleId(car.getVehicleId());
        assertFalse(rents.get(0).isArchived());

        rentManager.returnVehicle(1, LocalDateTime.now().plusDays(7));

        rents = rentManager.findRentsByVehicleId(1);
        assertTrue(rents.get(0).isArchived());
    }
}
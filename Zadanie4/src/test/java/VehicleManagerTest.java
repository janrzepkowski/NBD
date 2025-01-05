import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import managers.VehicleManager;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.AbstractCassandraRepository;
import repositories.VehicleRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleManagerTest {

    private static CqlSession session;
    private static VehicleRepository vehicleRepository;
    private static VehicleManager vehicleManager;

    @BeforeAll
    static void setUp() {
        AbstractCassandraRepository abstractCassandraRepository = new AbstractCassandraRepository();
        session = abstractCassandraRepository.getSession();
        vehicleRepository = new VehicleRepository(session);
        vehicleManager = new VehicleManager(vehicleRepository);
    }

    @AfterEach
    void dropDB() {
        Truncate truncate = QueryBuilder.truncate("vehicles");
        session.execute(truncate.build());
    }

    @Test
    void testRegisterVehicleWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new VehicleManager(null);
        });

        assertEquals("vehicleRepository cannot be null", exception.getMessage());
    }

    @Test
    void testRegisterBicycle() {
        Bicycle bicycle = new Bicycle(UUID.randomUUID(), "BIKE123", "Giant", 50);
        vehicleManager.registerVehicle(bicycle);

        Vehicle registeredVehicle = vehicleRepository.read(bicycle.getVehicleId());
        assertEquals(bicycle, registeredVehicle);
    }

    @Test
    void testRegisterVehicleWithExistingId() {
        Car car = new Car(UUID.randomUUID(), "ABC123", "Toyota", 100, 'B', 1.8);
        vehicleRepository.create(car);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicleManager.registerVehicle(car);
        });

        assertEquals("Vehicle with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testUnregisterVehicle() {
        Car car = new Car(UUID.randomUUID(), "XYZ789", "Honda", 150, 'C', 2.0);
        vehicleRepository.create(car);

        vehicleManager.unregisterVehicle(car);

        Vehicle unregisteredVehicle = vehicleRepository.read(car.getVehicleId());
        assertTrue(unregisteredVehicle.isArchived());
    }

    @Test
    void testDeleteVehicle() {
        Car car = new Car(UUID.randomUUID(), "XYZ789", "Honda", 150, 'C', 2.0);
        vehicleManager.registerVehicle(car);

        vehicleManager.deleteVehicle(car.getVehicleId());
        Vehicle deletedVehicle = vehicleRepository.read(car.getVehicleId());
        assertNull(deletedVehicle);
    }

    @Test
    void testUpdateVehicle() {
        Car car = new Car(UUID.randomUUID(), "XYZ789", "Honda", 150, 'C', 2.0);
        vehicleManager.registerVehicle(car);

        car.setBasePrice(200);
        vehicleManager.updateVehicle(car);

        Vehicle updatedVehicle = vehicleRepository.read(car.getVehicleId());
        assertEquals(200, updatedVehicle.getBasePrice());
    }
}
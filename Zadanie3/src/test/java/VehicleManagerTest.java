import managers.VehicleManager;
import models.Car;
import models.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.DecoratorVehicleRepository;
import repositories.RedisVehicleRepository;
import repositories.VehicleRepository;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleManagerTest {

    private static VehicleRepository vehicleRepository2;
    private static VehicleManager vehicleManager;
    private static RedisVehicleRepository redisVehicleRepository;

    @BeforeAll
    public static void setUp() {
        vehicleRepository2 = new VehicleRepository();
        redisVehicleRepository = new RedisVehicleRepository();
        DecoratorVehicleRepository vehicleRepository = new DecoratorVehicleRepository(vehicleRepository2, redisVehicleRepository);
        vehicleManager = new VehicleManager(vehicleRepository);
    }

    @AfterEach
    void dropDB() {
        vehicleRepository2.getDatabase().getCollection("vehicles", Vehicle.class).drop();
        redisVehicleRepository.clearCache();
    }

    @Test
    void testRegisterVehicle() {
        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        vehicleManager.registerVehicle(car);

        Vehicle registeredVehicle = vehicleManager.getVehicle(car.getVehicleId());
        assertEquals(car, registeredVehicle);
    }

    @Test
    void testRegisterVehicleWithExistingId() {
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        vehicleManager.registerVehicle(car);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vehicleManager.registerVehicle(car));

        assertEquals("Vehicle with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testUnregisterVehicle() {
        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        vehicleManager.registerVehicle(car);

        vehicleManager.unregisterVehicle(car);

        Vehicle unregisteredVehicle = vehicleManager.getVehicle(car.getVehicleId());
        assertTrue(unregisteredVehicle.isArchived());
    }

    @Test
    void testUpdateVehicleInformation() {
        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        vehicleManager.registerVehicle(car);

        car.setBasePrice(200);
        vehicleManager.updateVehicleInformation(car);

        Vehicle updatedVehicle = vehicleManager.getVehicle(car.getVehicleId());
        assertEquals(200, updatedVehicle.getBasePrice());
    }

    @Test
    void testDeleteVehicle() {
        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        vehicleManager.registerVehicle(car);

        vehicleManager.deleteVehicle(car.getVehicleId());

        Vehicle deletedVehicle = vehicleManager.getVehicle(car.getVehicleId());
        assertNull(deletedVehicle);
    }
}
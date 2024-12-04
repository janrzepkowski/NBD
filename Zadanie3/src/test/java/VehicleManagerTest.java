import managers.VehicleManager;
import models.Car;
import models.Vehicle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.DecoratorVehicleRepository;
import repositories.RedisVehicleRepository;
import repositories.VehicleRepository;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleManagerTest {

    private static VehicleRepository vehicleRepository;
    private static RedisVehicleRepository redisVehicleRepository;
    private static DecoratorVehicleRepository decoratorVehicleRepository;
    private static VehicleManager vehicleManager;

    @BeforeAll
    public static void setUp() {
        vehicleRepository = new VehicleRepository();
        redisVehicleRepository = new RedisVehicleRepository();
        decoratorVehicleRepository = new DecoratorVehicleRepository(vehicleRepository, redisVehicleRepository);
        vehicleManager = new VehicleManager(decoratorVehicleRepository);
    }

    @Test
    void testRegisterVehicleWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new VehicleManager(null);
        });

        assertEquals("vehicleRepository cannot be null", exception.getMessage());
    }

    @Test
    void testRegisterVehicle() {
        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        vehicleManager.registerVehicle(car);

        Vehicle registeredVehicle = decoratorVehicleRepository.read(car.getVehicleId());
        assertEquals(car, registeredVehicle);
    }

    @Test
    void testRegisterVehicleWithExistingId() {
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        decoratorVehicleRepository.create(car);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicleManager.registerVehicle(car);
        });

        assertEquals("Vehicle with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testUnregisterVehicle() {
        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        decoratorVehicleRepository.create(car);

        vehicleManager.unregisterVehicle(car);

        Vehicle unregisteredVehicle = decoratorVehicleRepository.read(car.getVehicleId());
        assertTrue(unregisteredVehicle.isArchived());
    }
}
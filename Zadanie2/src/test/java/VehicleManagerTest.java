import managers.VehicleManager;
import models.Car;
import models.Vehicle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.VehicleRepository;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleManagerTest {

    private static VehicleRepository vehicleRepository;
    private static VehicleManager vehicleManager;

    @BeforeAll
    public static void setUp() {
        vehicleRepository = new VehicleRepository();
        vehicleManager = new VehicleManager(vehicleRepository);
    }

    @Test
    void testRegisterVehicleWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new VehicleManager(null);
        });

        assertEquals("vehicleRepository cannot be null", exception.getMessage());
    }

//    @Test
//    void testRegisterVehicle() {
//        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
//        vehicleManager.registerVehicle(car);
//
//        Vehicle registeredVehicle = vehicleRepository.read(car.getVehicleId());
//        assertEquals(car, registeredVehicle);
//    }
//
//    @Test
//    void testRegisterVehicleWithExistingId() {
//        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
//        vehicleRepository.create(car);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            vehicleManager.registerVehicle(car);
//        });
//
//        assertEquals("Vehicle with the same ID already exists.", exception.getMessage());
//    }
//
//    @Test
//    void testUnregisterVehicle() {
//        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
//        vehicleRepository.create(car);
//
//        vehicleManager.unregisterVehicle(car);
//
//        Vehicle unregisteredVehicle = vehicleRepository.read(car.getVehicleId());
//        assertTrue(unregisteredVehicle.isArchived());
//    }
}
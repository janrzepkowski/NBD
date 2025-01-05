import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import managers.VehicleManager;
import models.Bicycle;
import models.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.AbstractCassandraRepository;
import repositories.VehicleRepository;

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
    void testRegisterVehicle() {
        Car car = new Car(3, 200, "car", "Toyota", 2);
        vehicleManager.registerVehicle(3, 200, "Toyota", 2);

        Car registeredCar = (Car) vehicleRepository.read(3);
        assertEquals(car, registeredCar);
    }

    @Test
    void testRegisterVehicleWithExistingId() {
        Car car = new Car(4, 150, "car", "Honda", 2);
        vehicleRepository.create(car);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicleManager.registerVehicle(4, 150, "Honda", 2);
        });

        assertEquals("Vehicle with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testDeleteVehicle() {
        vehicleManager.registerVehicle(1, 100, "Ford", 2);
        Car car = (Car) vehicleManager.getVehicle(1);
        assertNotNull(car);

        vehicleManager.deleteVehicle(1);
        Car deletedCar = (Car) vehicleManager.getVehicle(1);
        assertNull(deletedCar);
    }

    @Test
    void testUpdateVehicle() {
        vehicleManager.registerVehicle(1, 100, "Ford", 2);
        vehicleManager.registerVehicle(2, 150, "Giant");
        Car car = (Car) vehicleManager.getVehicle(1);
        Bicycle bicycle = (Bicycle) vehicleManager.getVehicle(2);
        assertEquals(100, car.getBasePrice());
        assertEquals(150, bicycle.getBasePrice());

        vehicleManager.updateVehicle(1, 200, "Ford", 3);
        vehicleManager.updateVehicle(2, 250, "Giant");
        Car updatedCar = (Car) vehicleManager.getVehicle(1);
        Bicycle updatedBicycle = (Bicycle) vehicleManager.getVehicle(2);
        assertEquals(200, updatedCar.getBasePrice());
        assertEquals(250, updatedBicycle.getBasePrice());
    }
}
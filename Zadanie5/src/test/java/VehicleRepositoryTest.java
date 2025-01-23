import models.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.VehicleRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleRepositoryTest {

    private static VehicleRepository vehicleRepository;

    @BeforeAll
    public static void setUp() {
        vehicleRepository = new VehicleRepository();
    }

    @BeforeEach
    public void cleanUp() {
        vehicleRepository.getDatabase().getCollection("vehicles", Vehicle.class).drop();
    }

    @AfterAll
    public static void tearDown() {
        vehicleRepository.getDatabase().getCollection("vehicles", Vehicle.class).drop();
        vehicleRepository.close();
    }

    @Test
    void testAddVehicle() {
        Car car = new Car("MAG 1C", "BMW", 150, 'D', 2.5);
        Bicycle bicycle = new Bicycle("AWE 5OM", "Bianchi", 50);

        vehicleRepository.create(car);
        vehicleRepository.create(bicycle);

        Car foundVehicle1 = (Car) vehicleRepository.read(car.getVehicleId());
        Bicycle foundVehicle2 = (Bicycle) vehicleRepository.read(bicycle.getVehicleId());

        assertEquals(car, foundVehicle1);
        assertEquals(bicycle, foundVehicle2);
    }

    @Test
    void testRemoveVehicle() {
        Car car = new Car("AMS 1", "Ford", 120, 'D', 2.2);
        vehicleRepository.create(car);

        Car foundCar1 = (Car) vehicleRepository.read(car.getVehicleId());
        assertNotNull(foundCar1);

        vehicleRepository.delete(car);

        Car foundCar2 = (Car) vehicleRepository.read(car.getVehicleId());
        assertNull(foundCar2);
    }

    @Test
    void testUpdateVehicle() {
        Moped moped = new Moped("QRS789", "Vespa", 80, 2.5);
        vehicleRepository.create(moped);

        Moped foundMoped1 = (Moped) vehicleRepository.read(moped.getVehicleId());
        assertEquals("QRS789", foundMoped1.getPlateNumber());
        assertEquals(80, foundMoped1.getBasePrice());

        moped.setPlateNumber("WAT123");
        moped.setBasePrice(90);

        vehicleRepository.update(moped);

        Moped foundMoped2 = (Moped) vehicleRepository.read(moped.getVehicleId());
        assertEquals("WAT123", foundMoped2.getPlateNumber());
        assertEquals(90, foundMoped2.getBasePrice());
    }

    @Test
    void testGetAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.readAll();
        int initialSize = vehicles.size();

        Car car1 = new Car("JKL456", "Mazda", 110, 'A', 1.6);
        Car car2 = new Car("MNO789", "Subaru", 160, 'B', 2.0);

        vehicleRepository.create(car1);
        vehicleRepository.create(car2);

        vehicles = vehicleRepository.readAll();
        int finalSize = vehicles.size();

        assertEquals(initialSize + 2, finalSize);
    }
}
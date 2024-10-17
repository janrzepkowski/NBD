import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.VehicleRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleRepositoryTest {

    private static VehicleRepository vehicleRepository;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        vehicleRepository = new VehicleRepository(entityManager);
    }

    @Test
    void testAddVehicle() {
        Car car = new Car("MAG 1C", "BMW", 150, 'D', 2.5);
        Bicycle bicycle = new Bicycle("AWE 5OM", "Bianchi", 50);

        vehicleRepository.add(car);
        vehicleRepository.add(bicycle);

        Car foundVehicle1 = (Car) vehicleRepository.get(car.getVehicleId());
        Bicycle foundVehicle2 = (Bicycle) vehicleRepository.get(bicycle.getVehicleId());

        assertEquals(car, foundVehicle1);
        assertEquals(bicycle, foundVehicle2);
    }

    @Test
    void testRemoveVehicle() {
        Car car = new Car("AMS 1", "Ford", 120, 'D', 2.2);
        vehicleRepository.add(car);

        Car foundCar1 = (Car) vehicleRepository.get(car.getVehicleId());
        assertNotNull(foundCar1);

        vehicleRepository.remove(car);

        Car foundCar2 = (Car) vehicleRepository.get(car.getVehicleId());
        assertNull(foundCar2);
    }

    @Test
    void testUpdateVehicle() {
        Moped moped = new Moped("QRS789", "Vespa", 80, 2.5);
        vehicleRepository.add(moped);

        Moped foundMoped1 = (Moped) vehicleRepository.get(moped.getVehicleId());
        assertEquals("QRS789", foundMoped1.getPlateNumber());
        assertEquals(80, foundMoped1.getBasePrice());

        moped.setPlateNumber("WAT123");
        moped.setBasePrice(90);

        vehicleRepository.update(moped);

        Moped foundMoped2 = (Moped) vehicleRepository.get(moped.getVehicleId());
        assertEquals("WAT123", foundMoped2.getPlateNumber());
        assertEquals(90, foundMoped2.getBasePrice());
    }

    @Test
    void testGetAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.getAll();
        int initialSize = vehicles.size();

        Car car1 = new Car("JKL456", "Mazda", 110, 'A', 1.6);
        Car car2 = new Car("MNO789", "Subaru", 160, 'B', 2.0);

        vehicleRepository.add(car1);
        vehicleRepository.add(car2);

        vehicles = vehicleRepository.getAll();
        int finalSize = vehicles.size();

        assertEquals(initialSize + 2, finalSize);
    }
}
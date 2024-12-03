import models.Car;
import models.Moped;
import models.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repositories.RedisVehicleRepository;

import java.util.List;

public class RedisVehicleTest {

    private final RedisVehicleRepository redisVehicleRepository = new RedisVehicleRepository();

    @AfterEach
    public void clearCache() {
        redisVehicleRepository.clearCache();
    }

    @Test
    public void createAndReadVehicleFromRedis() {
        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        redisVehicleRepository.create(car);

        Vehicle vehicleFromRedis = redisVehicleRepository.read(car.getVehicleId());
        Assertions.assertEquals(car, vehicleFromRedis);
    }

    @Test
    public void readAllVehiclesFromRedis() {
        Car car1 = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        Car car2 = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        Moped moped = new Moped("MOP456", "Vespa", 80, 1.5);

        redisVehicleRepository.create(car1);
        redisVehicleRepository.create(car2);
        redisVehicleRepository.create(moped);

        List<Vehicle> vehicles = redisVehicleRepository.readAll();

        Assertions.assertEquals(3, vehicles.size());
        Assertions.assertTrue(vehicles.contains(car1));
        Assertions.assertTrue(vehicles.contains(car2));
        Assertions.assertTrue(vehicles.contains(moped));
    }

    @Test
    public void clearCacheTest() {
        Car car1 = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        Car car2 = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        redisVehicleRepository.create(car1);
        redisVehicleRepository.create(car2);

        redisVehicleRepository.clearCache();

        Assertions.assertEquals(0, redisVehicleRepository.readAll().size());
    }

    @Test
    public void loseConnectionWithRedis() {
        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
        redisVehicleRepository.create(car);
        redisVehicleRepository.close();

        Vehicle vehicleFromRedis = redisVehicleRepository.read(car.getVehicleId());
        Assertions.assertNull(vehicleFromRedis);
    }
}
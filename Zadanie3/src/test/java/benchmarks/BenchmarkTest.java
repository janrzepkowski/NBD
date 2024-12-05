package benchmarks;

import managers.VehicleManager;
import models.*;
import org.openjdk.jmh.annotations.*;
import repositories.DecoratorVehicleRepository;
import repositories.RedisVehicleRepository;
import repositories.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@State(Scope.Benchmark)
public class BenchmarkTest {
    private RedisVehicleRepository redisVehicleRepository;
    private VehicleRepository vehicleRepository;
    private VehicleManager vehicleManager;
    private List<UUID> vehicleIds;

    private int numberOfVehicles;

    @Setup
    public void init() {
        redisVehicleRepository = new RedisVehicleRepository();
        vehicleRepository = new VehicleRepository();
        DecoratorVehicleRepository decoratorVehicleRepository = new DecoratorVehicleRepository(vehicleRepository, redisVehicleRepository);
        vehicleManager = new VehicleManager(decoratorVehicleRepository);
        numberOfVehicles = 10;
        vehicleIds = new ArrayList<>();
        for (int i = 1; i <= numberOfVehicles; i++) {
            Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
            vehicleManager.registerVehicle(car);
            vehicleIds.add(car.getVehicleId());
        }
    }

    @Benchmark
    @Warmup(iterations = 0)
    @Fork(value = 2)
    public void readFromCache() {
        for (UUID vehicleId : vehicleIds) {
            vehicleManager.getVehicle(vehicleId);
        }
    }

    @Benchmark
    @Warmup(iterations = 0)
    @Fork(value = 2)
    public void readFromMongoUsingManager() {
        redisVehicleRepository.clearCache();
        for (UUID vehicleId : vehicleIds) {
            vehicleManager.getVehicle(vehicleId);
        }
    }

    @Benchmark
    @Warmup(iterations = 0)
    @Fork(value = 2)
    public void readFromMongoUsingRepository() {
        for (UUID vehicleId : vehicleIds) {
            vehicleRepository.read(vehicleId);
        }
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @TearDown
    public void cleanUp() {
        vehicleRepository.getDatabase().getCollection("vehicles", Vehicle.class).drop();
        redisVehicleRepository.clearCache();
    }
}
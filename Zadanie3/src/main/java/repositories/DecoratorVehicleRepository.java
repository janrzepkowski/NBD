package repositories;

import models.Vehicle;

import java.util.List;
import java.util.UUID;

public class DecoratorVehicleRepository implements IVehicleRepository {
    private final VehicleRepository repository;
    private final RedisVehicleRepository redisVehicleRepository;

    public DecoratorVehicleRepository(VehicleRepository repository, RedisVehicleRepository redisVehicleRepository) {
        this.repository = repository;
        this.redisVehicleRepository = redisVehicleRepository;
    }

    @Override
    public Vehicle read(UUID vehicleId) {
        Vehicle vehicle = redisVehicleRepository.read(vehicleId);
        if (vehicle == null) {
            vehicle = repository.read(vehicleId);
            if (vehicle != null && vehicle.getAvailable()) {
                redisVehicleRepository.create(vehicle);
            }
            return vehicle;
        }
        return vehicle;
    }

    @Override
    public List<Vehicle> readAll() {
        List<Vehicle> vehicles = repository.readAll();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getAvailable()) {
                redisVehicleRepository.create(vehicle);
            }
        }
        return vehicles;
    }

    @Override
    public void create(Vehicle vehicle) {
        repository.create(vehicle);
        redisVehicleRepository.create(vehicle);
    }

    @Override
    public void delete(Vehicle vehicle) {
        redisVehicleRepository.delete(vehicle.getVehicleId());
        repository.delete(vehicle);
    }

    @Override
    public void update(Vehicle vehicle) {
        repository.update(vehicle);
        if (!vehicle.getAvailable()) {
            redisVehicleRepository.delete(vehicle.getVehicleId());
        } else {
            redisVehicleRepository.update(vehicle);
        }
    }
}
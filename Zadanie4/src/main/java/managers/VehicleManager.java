package managers;

import models.Bicycle;
import models.Car;
import models.Vehicle;
import repositories.VehicleRepository;

public class VehicleManager {
    private VehicleRepository vehicleRepository;

    public VehicleManager(VehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new IllegalArgumentException("vehicleRepository cannot be null");
        } else {
            this.vehicleRepository = vehicleRepository;
        }
    }

    private boolean vehicleExists(long vehicleId) {
        return vehicleRepository.read(vehicleId) != null;
    }

    public Vehicle getVehicle(long vehicleId) {
        return vehicleRepository.read(vehicleId);
    }

    public void registerVehicle(long vehicleId, int basePrice, String brand, int engineCapacity) {
        if (vehicleExists(vehicleId)) {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
        Vehicle vehicle = new Car(vehicleId, basePrice, "car", brand, engineCapacity);
        vehicleRepository.create(vehicle);
    }

    public void registerVehicle(long vehicleId, int basePrice, String brand) {
        if (vehicleExists(vehicleId)) {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
        Vehicle vehicle = new Bicycle(vehicleId, basePrice, brand);
        vehicleRepository.create(vehicle);
    }

    public void deleteVehicle(long vehicleId) {
        if (vehicleExists(vehicleId)) {
            vehicleRepository.delete(vehicleId);
        }
    }

    public void updateVehicle(long vehicleId, int basePrice, String brand, int engineCapacity) {
        if (vehicleExists(vehicleId)) {
            Vehicle vehicle = new Car(vehicleId, basePrice, "car", brand, engineCapacity);
            vehicleRepository.update(vehicle);
        }
    }

    public void updateVehicle(long vehicleId, int basePrice, String brand) {
        if (vehicleExists(vehicleId)) {
            Vehicle vehicle = new Bicycle(vehicleId, basePrice, brand);
            vehicleRepository.update(vehicle);
        }
    }
}
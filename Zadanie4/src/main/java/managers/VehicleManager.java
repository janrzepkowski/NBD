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

    public void registerVehicle(long vehicleId, int basePrice, int engineCapacity) {
        if (vehicleExists(vehicleId)) {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
        Vehicle vehicle = new Car(vehicleId, basePrice, "car", engineCapacity);
        vehicleRepository.create(vehicle);
    }

    public void registerVehicle(long vehicleId, int basePrice) {
        if (vehicleExists(vehicleId)) {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
        Vehicle vehicle = new Bicycle(vehicleId, basePrice);
        vehicleRepository.create(vehicle);
    }

    public void deleteVehicle(long vehicleId) {
        if (vehicleExists(vehicleId)) {
            vehicleRepository.delete(vehicleId);
        }
    }

    public void updateVehicleInformation(long vehicleId, int basePrice, int engineCapacity) {
        if (vehicleExists(vehicleId)) {
            Vehicle vehicle = new Car(vehicleId, basePrice, "car", engineCapacity);
            vehicleRepository.update(vehicle);
        }
    }

    public void updateVehicleInformation(long vehicleId, int basePrice) {
        if (vehicleExists(vehicleId)) {
            Vehicle vehicle = new Bicycle(vehicleId, basePrice);
            vehicleRepository.update(vehicle);
        }
    }
}
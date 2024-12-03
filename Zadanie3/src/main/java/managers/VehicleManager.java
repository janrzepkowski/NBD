package managers;

import models.Vehicle;
import repositories.DecoratorVehicleRepository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class VehicleManager implements Serializable {

    private final DecoratorVehicleRepository vehicleRepository;

    public VehicleManager(DecoratorVehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new IllegalArgumentException("vehicleRepository cannot be null");
        } else {
            this.vehicleRepository = vehicleRepository;
        }
    }

    private boolean vehicleExists(UUID vehicleId) {
        List<Vehicle> vehicles = vehicleRepository.readAll();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                return true;
            }
        }
        return false;
    }

    public Vehicle getVehicle(UUID vehicleId) {
        return vehicleRepository.read(vehicleId);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.readAll();
    }

    public void registerVehicle(Vehicle vehicle) {
        if (!vehicleExists(vehicle.getVehicleId())) {
            vehicleRepository.create(vehicle);
        } else {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
    }

    public void unregisterVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            vehicle.setArchived(true);
            vehicleRepository.update(vehicle);
        }
    }

    public void updateVehicleInformation(Vehicle vehicle) {
        if (vehicleExists(vehicle.getVehicleId())) {
            vehicleRepository.update(vehicle);
        }
    }

    public void deleteVehicle(UUID vehicleId) {
        if (vehicleExists(vehicleId)) {
            vehicleRepository.delete(vehicleRepository.read(vehicleId));
        }
    }
}
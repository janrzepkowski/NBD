package managers;

import models.Vehicle;
import repositories.VehicleRepository;

import java.io.Serializable;
import java.util.UUID;

public class VehicleManager implements Serializable {

    private final VehicleRepository vehicleRepository;

    public VehicleManager(VehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new IllegalArgumentException("vehicleRepository cannot be null");
        } else {
            this.vehicleRepository = vehicleRepository;
        }
    }

    private boolean vehicleExists(UUID vehicleId) {
        return vehicleRepository.read(vehicleId) != null;
    }

    public Vehicle getVehicle(UUID vehicleId) {
        return vehicleRepository.read(vehicleId);
    }

    public void registerVehicle(Vehicle vehicle) {
        if (!vehicleExists(vehicle.getVehicleId())) {
            vehicleRepository.create(vehicle);
        } else {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
    }

    public void deleteVehicle(UUID vehicleId) {
        if (vehicleExists(vehicleId)) {
            vehicleRepository.delete(vehicleId);
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        if (vehicleExists(vehicle.getVehicleId())) {
            vehicleRepository.update(vehicle);
        }
    }

    public void unregisterVehicle(Vehicle vehicle) {
        if (vehicle != null && vehicleExists(vehicle.getVehicleId())) {
            vehicle.setArchived(true);
            vehicleRepository.update(vehicle);
        }
    }
}
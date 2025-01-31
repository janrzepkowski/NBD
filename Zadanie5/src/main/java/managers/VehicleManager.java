package managers;

import models.Vehicle;
import repositories.VehicleRepository;

import java.io.Serializable;

public class VehicleManager implements Serializable {

    private final VehicleRepository vehicleRepository;

    public VehicleManager(VehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new IllegalArgumentException("vehicleRepository cannot be null");
        } else {
            this.vehicleRepository = vehicleRepository;
        }
    }

    public void registerVehicle(Vehicle vehicle) {
        if (vehicleRepository.read(vehicle.getVehicleId()) != null) {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
        vehicleRepository.create(vehicle);
    }

    public void unregisterVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            vehicle.setArchived(true);
            vehicleRepository.update(vehicle);
        }
    }
}
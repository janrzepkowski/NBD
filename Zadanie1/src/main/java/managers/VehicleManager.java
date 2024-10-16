package managers;

import models.Vehicle;
import repositories.VehicleRepository;

import java.io.Serializable;

public class VehicleManager implements Serializable {

    private VehicleRepository vehicleRepository;

    public VehicleManager(VehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new IllegalArgumentException("vehicleRepository cannot be null");
        } else {
            this.vehicleRepository = vehicleRepository;
        }
    }

    public void registerVehicle(Vehicle vehicle) {
        if (vehicleRepository.get(vehicle.getVehicleId()) != null) {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
        vehicleRepository.add(vehicle);
    }

    public void unregisterVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            vehicle.setArchived(true);
            vehicle.setAvailable(false);
            vehicleRepository.update(vehicle);
        }
    }
}

package managers;

import models.Bicycle;
import models.Car;
import models.Moped;
import models.Vehicle;
import repositories.VehicleRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class VehicleManager implements Serializable {

    private VehicleRepository vehicleRepository;

    public VehicleManager(VehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new IllegalArgumentException("vehicleRepository cannot be null");
        } else {
            this.vehicleRepository = vehicleRepository;
        }
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        if (vehicleRepository.get(vehicle.getVehicleId()) != null) {
            throw new IllegalArgumentException("Vehicle with the same ID already exists.");
        }
        return vehicleRepository.add(vehicle);
    }

    public void unregisterVehicle(Vehicle vehicle) {
        vehicle.setArchived(true);
    }
}

package repositories;

import models.Vehicle;

import java.util.List;
import java.util.UUID;

public interface IVehicleRepository {
    Vehicle read(UUID vehicleId);
    List<Vehicle> readAll();
    void create(Vehicle vehicle);
    void delete(Vehicle vehicle);
    void update(Vehicle vehicle);
}
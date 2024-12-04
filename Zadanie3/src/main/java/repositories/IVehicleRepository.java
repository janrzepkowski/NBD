package repositories;

import models.Vehicle;

import java.util.List;
import java.util.UUID;

public interface IVehicleRepository {
    void create(Vehicle vehicle);
    Vehicle read(UUID vehicleId);
    List<Vehicle> readAll();
    void update(Vehicle vehicle);
    void delete(Vehicle vehicle);
}
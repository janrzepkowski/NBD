package managers;

import models.Bicycle;
import models.Car;
import models.Moped;
import models.Vehicle;

import java.util.List;
import java.util.Optional;

public class VehicleManager {
    private List<Vehicle> vehicles;

    public VehicleManager(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public Optional<Vehicle> getVehicle(String plateNumber) {
        return vehicles.stream().filter(vehicle -> vehicle.getPlateNumber().equals(plateNumber)).findFirst();
    }

    public void registerBicycle(String plateNumber, String brand, int basePrice) {
        if (vehicles.stream().anyMatch(vehicle -> vehicle.getPlateNumber().equals(plateNumber))) {
            throw new IllegalArgumentException("Vehicle with plate number " + plateNumber + " already exists.");
        }
        vehicles.add(new Bicycle(plateNumber, brand, basePrice));
    }

    public void registerCar(String plateNumber, String brand, int basePrice, char segment, double engineCapacity) {
        if (vehicles.stream().anyMatch(vehicle -> vehicle.getPlateNumber().equals(plateNumber))) {
            throw new IllegalArgumentException("Vehicle with plate number " + plateNumber + " already exists.");
        }
        vehicles.add(new Car(plateNumber, brand, basePrice, segment, engineCapacity));
    }

    public void registerMoped(String plateNumber, String brand, int basePrice, double engineCapacity) {
        if (vehicles.stream().anyMatch(vehicle -> vehicle.getPlateNumber().equals(plateNumber))) {
            throw new IllegalArgumentException("Vehicle with plate number " + plateNumber + " already exists.");
        }
        vehicles.add(new Moped(plateNumber, brand, basePrice, engineCapacity));
    }

    public void removeVehicle(String plateNumber) {
        vehicles.removeIf(vehicle -> vehicle.getPlateNumber().equals(plateNumber));
    }

    public String getVehiclesInfo() {
        StringBuilder vehiclesInfo = new StringBuilder();
        vehicles.forEach(vehicle -> vehiclesInfo.append(vehicle.getVehicleInfo()).append("\n\n"));
        return vehiclesInfo.toString();
    }
}

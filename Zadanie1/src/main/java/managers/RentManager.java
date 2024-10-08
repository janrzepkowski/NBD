package managers;

import models.Client;
import models.Rent;
import models.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RentManager {
    private List<Rent> rents;

    public RentManager(List<Rent> rents) {
        this.rents = rents;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public List<Rent> getActiveRents() {
        return rents.stream()
                .filter(rent -> !rent.isArchived())
                .collect(Collectors.toList());
    }

    public Optional<Rent> getRent(String rentId) {
        return rents.stream().filter(rent -> rent.getRentId().equals(rentId)).findFirst();
    }

    public void rentVehicle(Client client, Vehicle vehicle, LocalDateTime rentStart) {
        if (!vehicle.isAvailable()) {
            throw new IllegalArgumentException("Vehicle is not available for rent.");
        }
        Rent newRent = new Rent(client, vehicle, rentStart);
        rents.add(newRent);
        vehicle.setAvailable(false);
    }

    public void returnVehicle(Vehicle vehicle) {
        rents.stream()
                .filter(rent -> rent.getVehicle().equals(vehicle))
                .findFirst()
                .ifPresent(rent -> {
                    rent.setRentEnd(LocalDateTime.now());
                    rent.setArchived(true);
                    vehicle.setAvailable(true);
                });
    }

    public String getRentsInfo() {
        StringBuilder rentsInfo = new StringBuilder();
        rents.forEach(rent -> rentsInfo.append(rent.getRentInfo()).append("\n\n"));
        return rentsInfo.toString();
    }

    public String getActiveRentsInfo() {
        StringBuilder rentsInfo = new StringBuilder();
        getActiveRents().forEach(rent -> rentsInfo.append(rent.getRentInfo()).append("\n\n"));
        return rentsInfo.toString();
    }
}
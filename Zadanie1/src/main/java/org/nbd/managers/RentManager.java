package org.nbd.managers;

import org.nbd.models.Client;
import org.nbd.models.Rent;
import org.nbd.models.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RentManager {
    private List<Rent> rents;

    public RentManager(List<Rent> rents) {
        this.rents = rents;
    }

    public List<Rent> getRents() {
        return rents;
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
                    vehicle.setAvailable(true);
                });
    }

    public String getRentsInfo() {
        StringBuilder rentsInfo = new StringBuilder();
        rents.forEach(rent -> rentsInfo.append(rent.getRentInfo()).append("\n\n"));
        return rentsInfo.toString();
    }
}
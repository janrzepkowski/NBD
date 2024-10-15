package managers;

import models.Client;
import models.Rent;
import models.Vehicle;
import repositories.RentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentManager {

    RentRepository rentRepository;

    public RentManager(RentRepository rentRepository) {
        if (rentRepository == null) {
            throw new IllegalArgumentException("rentRepository cannot be null");
        } else {
            this.rentRepository = rentRepository;
        }
    }

    public void rentVehicle(Client client, Vehicle vehicle, LocalDateTime rentStart) throws Exception {
        if (!vehicle.isAvailable()) {
            throw new Exception("Vehicle is already rented: " + vehicle.getVehicleId());
        }
        try {
            rentRepository.bookVehicle(client, vehicle, rentStart);
        } catch (RuntimeException e) {
            throw new RuntimeException("The rental vehicle failed: " + vehicle.getVehicleId(), e);
        }
    }

    public void returnVehicle(UUID id, LocalDateTime rentEnd) {
        Rent rent = rentRepository.get(id);
        rent.endRent(rentEnd);
        rentRepository.update(rent);
    }
}
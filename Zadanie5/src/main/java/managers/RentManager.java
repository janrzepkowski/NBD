package managers;

import models.Client;
import models.Rent;
import models.Vehicle;
import repositories.RentRepository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class RentManager implements Serializable {

    private final RentRepository rentRepository;

    public RentManager(RentRepository rentRepository) {
        if (rentRepository == null) {
            throw new IllegalArgumentException("rentRepository cannot be null");
        } else {
            this.rentRepository = rentRepository;
        }
    }

    public void rentVehicle(Client client, Vehicle vehicle, LocalDateTime rentStart) throws Exception {
        if (!vehicle.getAvailable()) {
            throw new Exception("Vehicle is not available: " + vehicle.getVehicleId());
        }
        if (client.getRents() >= 5) {
            throw new Exception("Client has reached the maximum number of rents: " + client.getClientId());
        }
        try {
            rentRepository.bookVehicle(client, vehicle, rentStart);
        } catch (RuntimeException e) {
            throw new RuntimeException("The rental vehicle failed: " + vehicle.getVehicleId(), e);
        }
    }

    public void returnVehicle(UUID id, LocalDateTime rentEnd) {
        Rent rent = rentRepository.read(id);
        rent.endRent(rentEnd);
        rent.setArchived(true);
        rentRepository.update(rent);
        rentRepository.returnVehicle(rent);
    }
}
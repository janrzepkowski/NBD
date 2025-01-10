package managers;

import models.Client;
import models.Rent;
import models.Vehicle;
import repositories.ClientRepository;
import repositories.RentRepository;
import repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.util.List;

public class RentManager {
    private RentRepository rentRepository;
    private ClientRepository clientRepository;
    private VehicleRepository vehicleRepository;

    public RentManager(RentRepository rentRepository, ClientRepository clientRepository, VehicleRepository vehicleRepository) {
        if (rentRepository == null) {
            throw new NullPointerException("rentRepository cannot be null");
        } else {
            this.rentRepository = rentRepository;
        }
        if (clientRepository == null) {
            throw new NullPointerException("clientRepository cannot be null");
        } else {
            this.clientRepository = clientRepository;
        }
        if (vehicleRepository == null) {
            throw new NullPointerException("vehicleRepository cannot be null");
        } else {
            this.vehicleRepository = vehicleRepository;
        }
    }

    public boolean rentExists(long clientId, long vehicleId) {
        List<Rent> rentsByClient = rentRepository.findByClientId(clientId);
        List<Rent> rentsByVehicle = rentRepository.findByVehicleId(vehicleId);
        return !rentsByClient.isEmpty() || !rentsByVehicle.isEmpty();
    }

    public List<Rent> findRentsByClientId(long clientId) {
        List<Rent> rents = rentRepository.findByClientId(clientId);
        for (Rent rent : rents) {
            rent.setClient(clientRepository.read(rent.getClientId()));
            rent.setVehicle(vehicleRepository.read(rent.getVehicleId()));
        }
        return rents;
    }

    public List<Rent> findRentsByVehicleId(long vehicleId) {
        List<Rent> rents = rentRepository.findByVehicleId(vehicleId);
        for (Rent rent : rents) {
            rent.setClient(clientRepository.read(rent.getClientId()));
            rent.setVehicle(vehicleRepository.read(rent.getVehicleId()));
        }
        return rents;
    }

    public void rentVehicle(long rentId, Client client, Vehicle vehicle, LocalDateTime startDate) {
        if (!rentExists(client.getClientId(), vehicle.getVehicleId())) {
            if (!vehicle.getAvailable()) {
                throw new IllegalArgumentException("Vehicle is already rented");
            }
            if (client.getRents() >= 5) {
                throw new IllegalArgumentException("Client has reached the maximum number of rents: " + client.getRents());
            }
            vehicle.setAvailable(false);
            vehicleRepository.update(vehicle);
            client.setRents(client.getRents() + 1);
            clientRepository.update(client);
            Rent rent = new Rent(rentId, client, vehicle, startDate);
            rentRepository.create(rent);
        }
    }

    public void returnVehicle(long vehicleId, LocalDateTime rentEnd) {
        Rent rent = findRentsByVehicleId(vehicleId).get(0);
        if (rent != null) {
            rent.endRent(rentEnd);
            rentRepository.update(rent);
            Vehicle vehicle = rent.getVehicle();
            vehicle.setAvailable(true);
            vehicleRepository.update(vehicle);
            Client client = rent.getClient();
            client.setRents(client.getRents() - 1);
            clientRepository.update(client);
        }
    }
}
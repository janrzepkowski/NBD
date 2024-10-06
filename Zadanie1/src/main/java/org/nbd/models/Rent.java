package org.nbd.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Rent {
    private String rentId;
    private Client client;
    private Vehicle vehicle;
    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;
    private boolean archived;

    public Rent(Client client, Vehicle vehicle, LocalDateTime rentStart, LocalDateTime rentEnd) {
        this.rentId = UUID.randomUUID().toString();
        this.client = client;
        this.vehicle = vehicle;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
        this.archived = false;
    }

    public String getRentId() {
        return rentId;
    }

    public Client getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getRentStart() {
        return rentStart;
    }

    public LocalDateTime getRentEnd() {
        return rentEnd;
    }

    public long getRentDays() {
        return rentStart.toLocalDate().until(rentEnd.toLocalDate()).getDays();
    }

    public double getRentPrice() {
        return vehicle.getActualRentalPrice() * getRentDays();
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getRentInfo() {
        return "Rent ID: " + rentId + "\nClient: " + client.getFirstName() + " " + client.getLastName() + "\nVehicle: " + vehicle.getBrand() + "\nRent start: " + rentStart + "\nRent end: " + rentEnd + "\nRent days: " + getRentDays() + "\nRent price: " + getRentPrice() + "\nArchived: " + archived;
    }
}

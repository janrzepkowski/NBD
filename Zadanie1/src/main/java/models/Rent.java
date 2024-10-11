package models;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "Rent")
@Access(AccessType.FIELD)
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID rentId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;
    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id",nullable = false)
    private Vehicle vehicle;
    @Column(name = "rentStart")
    private LocalDateTime rentStart;
    @Column(name = "rentEnd")
    private LocalDateTime rentEnd;
    @Column(name = "archived")
    private boolean archived;

    public Rent(Client client, Vehicle vehicle, LocalDateTime rentStart) {
        this.rentId = UUID.randomUUID();
        this.client = client;
        this.vehicle = vehicle;
        this.rentStart = rentStart;
        this.archived = false;
    }

    public UUID getRentId() {
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

    public void setRentEnd(LocalDateTime rentEnd) {
        this.rentEnd = rentEnd;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getRentInfo() {
        return "Client: " + client.getFirstName() + " " + client.getLastName() + "\nVehicle: " + vehicle.getBrand() + "\nRent start: " + rentStart + "\nRent end: " + rentEnd + "\nRent days: " + getRentDays() + "\nRent price: " + getRentPrice();
    }
}
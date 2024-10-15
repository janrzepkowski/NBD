package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.sun.istack.NotNull;
import jakarta.persistence.*;


@Entity
@Table(name = "Rent")
@Access(AccessType.FIELD)
public class Rent implements Serializable {

    @Id
    private final UUID rentId = UUID.randomUUID();

    @Version
    private long version;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    @NotNull
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    @NotNull
    private Vehicle vehicle;

    @Column(name = "rentStart")
    private LocalDateTime rentStart;

    @Column(name = "rentEnd")
    private LocalDateTime rentEnd = null;

    @Column(name = "rentCost")
    private double rentCost = 0;

    @Column(name = "archived")
    private boolean archived = false;

    public Rent() {
    }

    public Rent(Client client, Vehicle vehicle, LocalDateTime rentStart) {
        this.client = client;
        this.vehicle = vehicle;
        this.rentStart = (rentStart == null) ? LocalDateTime.now() : rentStart;
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
        if (rentEnd != null) {
            return rentStart.toLocalDate().until(rentEnd.toLocalDate()).getDays();
        }
        return 0;
    }

    public double getRentCost() {
        if (rentEnd != null) {
            return vehicle.getActualRentalPrice() * getRentDays();
        }
        return 0;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void endRent(LocalDateTime endTime) {
        if (this.rentEnd == null) {
            this.rentEnd = (endTime == null || !endTime.isAfter(rentStart)) ? LocalDateTime.now() : endTime;
            this.archived = true;
            this.rentCost = getRentCost();
        }
    }

    @Override
    public String toString() {
        return "Rent{" +
                "rentId=" + rentId +
                ", version=" + version +
                ", client=" + client +
                ", vehicle=" + vehicle +
                ", rentStart=" + rentStart +
                ", rentEnd=" + rentEnd +
                ", rentCost=" + rentCost +
                ", archived=" + archived +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rent rent = (Rent) o;
        return version == rent.version && Double.compare(rentCost, rent.rentCost) == 0 && archived == rent.archived && Objects.equals(rentId, rent.rentId) && Objects.equals(client, rent.client) && Objects.equals(vehicle, rent.vehicle) && Objects.equals(rentStart, rent.rentStart) && Objects.equals(rentEnd, rent.rentEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rentId, version, client, vehicle, rentStart, rentEnd, rentCost, archived);
    }
}

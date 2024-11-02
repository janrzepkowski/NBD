package models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Rent implements Serializable {

    @BsonId
    private UUID rentId = UUID.randomUUID();

    @BsonProperty(value = "client", useDiscriminator = true)
    private Client client;

    @BsonProperty(value = "vehicle", useDiscriminator = true)
    private Vehicle vehicle;

    @BsonProperty("rentStart")
    private LocalDateTime rentStart;

    @BsonProperty("rentEnd")
    private LocalDateTime rentEnd = null;

    @BsonProperty("rentCost")
    private double rentCost = 0;

    @BsonProperty("archived")
    private boolean archived = false;

    public Rent() {
    }

    public Rent(@BsonProperty("client") Client client,
                @BsonProperty("vehicle") Vehicle vehicle,
                @BsonProperty("rentStart") LocalDateTime rentStart) {
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
        return Double.compare(rentCost, rent.rentCost) == 0 && archived == rent.archived && Objects.equals(rentId, rent.rentId) && Objects.equals(client, rent.client) && Objects.equals(vehicle, rent.vehicle) && Objects.equals(rentStart, rent.rentStart) && Objects.equals(rentEnd, rent.rentEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rentId, client, vehicle, rentStart, rentEnd, rentCost, archived);
    }
}
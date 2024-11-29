package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public class Rent implements Serializable {

    @BsonId
    private final UUID rentId;

    @BsonProperty(value = "client", useDiscriminator = true)
    private final Client client;

    @BsonProperty(value = "vehicle", useDiscriminator = true)
    private final Vehicle vehicle;

    @BsonProperty("rentStart")
    private final LocalDateTime rentStart;

    @BsonProperty("rentEnd")
    private LocalDateTime rentEnd;

    @BsonProperty("rentCost")
    private double rentCost;

    @BsonProperty("archived")
    private boolean archived;

    @BsonCreator
    public Rent(@BsonId UUID rentId,
                @BsonProperty("client") Client client,
                @BsonProperty("vehicle") Vehicle vehicle,
                @BsonProperty("rentStart") LocalDateTime rentStart) {
        this.rentId = rentId != null ? rentId : UUID.randomUUID();
        this.client = client;
        this.vehicle = vehicle;
        this.rentStart = rentStart != null ? rentStart.truncatedTo(ChronoUnit.SECONDS) : LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.rentEnd = null;
        this.rentCost = 0;
        this.archived = false;
    }

    public Rent(Client client, Vehicle vehicle, LocalDateTime rentStart) {
        this(UUID.randomUUID(), client, vehicle, rentStart);
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

    @BsonIgnore
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
            this.rentEnd = (endTime == null || !endTime.isAfter(rentStart)) ? LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) : endTime.truncatedTo(ChronoUnit.SECONDS);
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
package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Rent {
    private long rentId;
    private long clientId;
    private Client client;
    private long vehicleId;
    private Vehicle vehicle;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private double rentCost;
    private boolean archived;

    public Rent(long rentId, Client client, Vehicle vehicle, LocalDateTime beginTime) {
        this.rentId = rentId;
        this.client = client;
        this.vehicle = vehicle;
        this.beginTime = (beginTime == null) ? LocalDateTime.now() : beginTime;
        this.endTime = null;
        this.rentCost = 0;
        this.archived = false;
        this.clientId = client.getClientId();
        this.vehicleId = vehicle.getVehicleId();
    }

    public Rent(long rentId, Client client, Vehicle vehicle, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean archived) {
        this.rentId = rentId;
        this.client = client;
        this.vehicle = vehicle;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.rentCost = rentCost;
        this.archived = archived;
        this.clientId = client.getClientId();
        this.vehicleId = vehicle.getVehicleId();
    }

    public Rent(long rentId, long clientId, long vehicleId, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean archived) {
        this.rentId = rentId;
        this.clientId = clientId;
        this.vehicleId = vehicleId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.rentCost = rentCost;
        this.archived = archived;
    }

    public Rent() {}

    public long getRentId() {
        return rentId;
    }

    public Client getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getRentCost() {
        return rentCost;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setRentId(long rentId) {
        this.rentId = rentId;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setRentCost(double rentCost) {
        this.rentCost = rentCost;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getClientId() {
        return clientId;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void endRent(LocalDateTime endTime) {
        if (this.endTime == null) {
            if (endTime == null) {
                this.endTime = LocalDateTime.now();
            } else {
                if (endTime.isAfter(beginTime)) {
                    this.endTime = endTime;
                } else {
                    this.endTime = beginTime;
                }
            }
            this.setArchived(true);
            this.rentCost = calculateRentCost();
        }
    }

    public long getRentDays() {
        if (endTime == null) {
            return 0;
        }

        Duration period = Duration.between(beginTime, endTime);
        long days = period.toHours() / 24;

        if (period.toHours() % 24 >= 1) {
            days += 1;
        }

        return days;
    }

    private double calculateRentCost() {
        return Math.round(100 * getRentDays() * vehicle.getActualRentalPrice()) / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rent rent = (Rent) o;

        if (rentId != rent.rentId) return false;

        if (!Objects.equals(client, rent.client)) return false;

        if (!Objects.equals(vehicle, rent.vehicle)) return false;

        if (!Objects.equals(beginTime.truncatedTo(ChronoUnit.MINUTES), rent.beginTime.truncatedTo(ChronoUnit.MINUTES))) return false;

        if (endTime != null && rent.endTime != null) {
            if (!Objects.equals(endTime.truncatedTo(ChronoUnit.MINUTES), rent.endTime.truncatedTo(ChronoUnit.MINUTES))) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rentId, client, vehicle, beginTime, endTime, rentCost, archived);
    }
}
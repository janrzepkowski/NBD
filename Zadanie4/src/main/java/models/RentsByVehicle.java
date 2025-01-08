package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.time.LocalDateTime;

@Entity(defaultKeyspace = "rent_a_vehicle")
@CqlName("rents_by_vehicle")
public class RentsByVehicle extends Rent {
    @CqlName("rent_id")
    private long rentId;
    private long clientId;
    @CqlName("vehicle_id")
    @PartitionKey
    private long vehicleId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private double rentCost;
    private boolean archived;

    public RentsByVehicle() {}

    public RentsByVehicle(long rentId, Client client, Vehicle vehicle, LocalDateTime beginTime) {
        super(rentId, client, vehicle, beginTime);
    }

    public RentsByVehicle(long rentId, Client client, Vehicle vehicle, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean archived) {
        super(rentId, client, vehicle, beginTime, endTime, rentCost, archived);
    }

    public RentsByVehicle(long rentId, long clientId, long vehicleId, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean archived) {
        super(rentId, clientId, vehicleId, beginTime, endTime, rentCost, archived);
    }

    public long getRentId() {
        return rentId;
    }

    public long getClientId() {
        return clientId;
    }

    public long getVehicleId() {
        return vehicleId;
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

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
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
}
package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import models.Bicycle;
import models.Car;
import models.Moped;
import models.Vehicle;
import providers.VehicleProvider;

import java.util.UUID;

@Dao
public interface VehicleDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Bicycle.class, Car.class, Moped.class})
    void create(Vehicle vehicle);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Bicycle.class, Car.class, Moped.class})
    Vehicle findById(UUID vehicleId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Bicycle.class, Car.class, Moped.class})
    void update(Vehicle vehicle);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Bicycle.class, Car.class, Moped.class})
    void remove(UUID vehicleId);
}



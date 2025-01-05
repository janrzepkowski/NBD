package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import models.Bicycle;
import models.Car;
import models.Vehicle;
import providers.VehicleProvider;

@Dao
public interface VehicleDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Car.class, Bicycle.class})
    void create(Vehicle vehicle);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Car.class, Bicycle.class})
    Vehicle findById(long vehicleId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Car.class, Bicycle.class})
    void update(Vehicle vehicle);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Car.class, Bicycle.class})
    void remove(long vehicleId);
}
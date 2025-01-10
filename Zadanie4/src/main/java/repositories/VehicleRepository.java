package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.VehicleDao;
import mappers.VehicleMapper;
import mappers.VehicleMapperBuilder;
import models.Vehicle;

public class VehicleRepository {

    private final CqlSession session;
    private final VehicleMapper vehicleMapper;
    private final VehicleDao vehicleDao;

    public VehicleRepository(CqlSession session) {
        this.session = session;
        makeTable();
        this.vehicleMapper = new VehicleMapperBuilder(session).build();
        this.vehicleDao = vehicleMapper.vehicleDao();
    }

    public void makeTable() {
        SimpleStatement createVehicles =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("vehicles"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("vehicle_id"), DataTypes.BIGINT)
                        .withColumn("base_price", DataTypes.INT)
                        .withColumn("available", DataTypes.BOOLEAN)
                        .withColumn("discriminator", DataTypes.TEXT)
                        .withColumn("brand", DataTypes.TEXT)
                        .withColumn("engine_capacity", DataTypes.INT) // For Car
                        .build();
        session.execute(createVehicles);
    }

    public void create(Vehicle vehicle) {
        vehicleDao.create(vehicle);
    }

    public Vehicle read(long vehicleId) {
        return vehicleDao.findById(vehicleId);
    }

    public void update(Vehicle vehicle) {
        vehicleDao.update(vehicle);
    }

    public void delete(long vehicleId) {
        vehicleDao.remove(vehicleId);
    }
}
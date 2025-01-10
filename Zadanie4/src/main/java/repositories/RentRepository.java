package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.RentDao;
import mappers.RentMapper;
import mappers.RentMapperBuilder;
import models.Rent;

import java.util.List;

public class RentRepository {

    private final CqlSession session;
    private final RentMapper rentMapper;
    private final RentDao rentDao;

    public RentRepository(CqlSession session) {
        this.session = session;
        makeTable();
        this.rentMapper = new RentMapperBuilder(session).build();
        this.rentDao = rentMapper.rentDao();
    }

    private void makeTable() {
        SimpleStatement createRentsByClient =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_client"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.BIGINT)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                        .withColumn("vehicle_id", DataTypes.BIGINT)
                        .withColumn("begin_time", DataTypes.TIMESTAMP)
                        .withColumn("end_time", DataTypes.TIMESTAMP)
                        .withColumn("rent_cost", DataTypes.DOUBLE)
                        .withColumn("archived", DataTypes.BOOLEAN)
                        .build();
        session.execute(createRentsByClient);

        SimpleStatement createRentsByVehicle =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_vehicle"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("vehicle_id"), DataTypes.BIGINT)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                        .withColumn("client_id", DataTypes.BIGINT)
                        .withColumn("begin_time", DataTypes.TIMESTAMP)
                        .withColumn("end_time", DataTypes.TIMESTAMP)
                        .withColumn("rent_cost", DataTypes.DOUBLE)
                        .withColumn("archived", DataTypes.BOOLEAN)
                        .build();
        session.execute(createRentsByVehicle);
    }

    public void create(Rent rent) {
        rentDao.create(rent);
    }

    public List<Rent> findByClientId(long clientId) {
        return rentDao.findByClientId(clientId);
    }

    public List<Rent> findByVehicleId(long vehicleId) {
        return rentDao.findByVehicleId(vehicleId);
    }

    public void update(Rent rent) {
        rentDao.update(rent);
    }

    public void delete(Rent rent) {
        rentDao.remove(rent);
    }
}
package providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import codec.LocalDateTimeCodec;
import models.Rent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentProvider {
    private final CqlSession session;
    private final LocalDateTimeCodec localDateTimeCodec = new LocalDateTimeCodec();

    public static final CqlIdentifier RENT_A_VEHICLE_NAMESPACE = CqlIdentifier.fromCql("rent_a_vehicle");
    public static final CqlIdentifier RENTS_BY_CLIENT = CqlIdentifier.fromCql("rents_by_client");
    public static final CqlIdentifier RENTS_BY_VEHICLE = CqlIdentifier.fromCql("rents_by_vehicle");
    public static final CqlIdentifier CLIENT_ID = CqlIdentifier.fromCql("client_id");
    public static final CqlIdentifier RENT_ID = CqlIdentifier.fromCql("rent_id");
    public static final CqlIdentifier VEHICLE_ID = CqlIdentifier.fromCql("vehicle_id");
    public static final CqlIdentifier BEGIN_TIME = CqlIdentifier.fromCql("begin_time");
    public static final CqlIdentifier END_TIME = CqlIdentifier.fromCql("end_time");
    public static final CqlIdentifier RENT_COST = CqlIdentifier.fromCql("rent_cost");
    public static final CqlIdentifier ARCHIVED = CqlIdentifier.fromCql("archived");

    public RentProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void create(Rent rent) {
        Insert insertClient = QueryBuilder.insertInto(RENTS_BY_CLIENT)
                .value(CLIENT_ID, literal(rent.getClientId()))
                .value(RENT_ID, literal(rent.getRentId()))
                .value(VEHICLE_ID, literal(rent.getVehicleId()))
                .value(BEGIN_TIME, literal(rent.getBeginTime(), localDateTimeCodec))
                .value(END_TIME, literal(rent.getEndTime(), localDateTimeCodec))
                .value(RENT_COST, literal(rent.getRentCost()))
                .value(ARCHIVED, literal(rent.isArchived()))
                .ifNotExists();

        Insert insertVehicle = QueryBuilder.insertInto(RENTS_BY_VEHICLE)
                .value(VEHICLE_ID, literal(rent.getVehicleId()))
                .value(RENT_ID, literal(rent.getRentId()))
                .value(CLIENT_ID, literal(rent.getClientId()))
                .value(BEGIN_TIME, literal(rent.getBeginTime(), localDateTimeCodec))
                .value(END_TIME, literal(rent.getEndTime(), localDateTimeCodec))
                .value(RENT_COST, literal(rent.getRentCost()))
                .value(ARCHIVED, literal(rent.isArchived()))
                .ifNotExists();

        session.execute(insertClient.build());
        session.execute(insertVehicle.build());
    }

    public List<Rent> findByClientId(long clientId) {
        Select select = QueryBuilder.selectFrom(RENTS_BY_CLIENT)
                .all()
                .where(Relation.column(CLIENT_ID).isEqualTo(literal(clientId)));
        ResultSet resultSet = session.execute(select.build());
        List<Row> rows = resultSet.all();

        return convertRowsToRents(rows);
    }

    public List<Rent> findByVehicleId(long vehicleId) {
        Select select = QueryBuilder.selectFrom(RENTS_BY_VEHICLE)
                .all()
                .where(Relation.column(VEHICLE_ID).isEqualTo(literal(vehicleId)));
        ResultSet resultSet = session.execute(select.build());
        List<Row> rows = resultSet.all();

        return convertRowsToRents(rows);
    }

    private List<Rent> convertRowsToRents(List<Row> rows) {
        ArrayList<Rent> rents = new ArrayList<>();

        for (Row row : rows) {
            LocalDateTime beginTime = row.isNull(BEGIN_TIME) ? null : LocalDateTime.ofInstant(row.getInstant(BEGIN_TIME), ZoneOffset.UTC);
            LocalDateTime endTime = row.isNull(END_TIME) ? null : LocalDateTime.ofInstant(row.getInstant(END_TIME), ZoneOffset.UTC);

            Rent rent = new Rent(
                    row.getLong(RENT_ID),
                    row.getLong(CLIENT_ID),
                    row.getLong(VEHICLE_ID),
                    beginTime,
                    endTime,
                    row.getDouble(RENT_COST),
                    row.getBoolean(ARCHIVED)
            );
            rents.add(rent);
        }
        return rents;
    }

    public void update(Rent rent) {
        Update updateClient = QueryBuilder.update(RENTS_BY_CLIENT)
                .setColumn(END_TIME, literal(rent.getEndTime(), localDateTimeCodec))
                .setColumn(RENT_COST, literal(rent.getRentCost()))
                .setColumn(ARCHIVED, literal(rent.isArchived()))
                .where(Relation.column(CLIENT_ID).isEqualTo(literal(rent.getClientId())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getRentId())));

        Update updateVehicle = QueryBuilder.update(RENTS_BY_VEHICLE)
                .setColumn(END_TIME, literal(rent.getEndTime(), localDateTimeCodec))
                .setColumn(RENT_COST, literal(rent.getRentCost()))
                .setColumn(ARCHIVED, literal(rent.isArchived()))
                .where(Relation.column(VEHICLE_ID).isEqualTo(literal(rent.getVehicleId())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getRentId())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateClient.build())
                .addStatement(updateVehicle.build())
                .build();

        session.execute(batchStatement);
    }

    public void remove(Rent rent) {
        Delete deleteClient = QueryBuilder.deleteFrom(RENTS_BY_CLIENT)
                .where(Relation.column(CLIENT_ID).isEqualTo(literal(rent.getClientId())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getRentId())));

        Delete deleteVehicle = QueryBuilder.deleteFrom(RENTS_BY_VEHICLE)
                .where(Relation.column(VEHICLE_ID).isEqualTo(literal(rent.getVehicleId())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getRentId())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(deleteClient.build())
                .addStatement(deleteVehicle.build())
                .build();

        session.execute(batchStatement);
    }
}
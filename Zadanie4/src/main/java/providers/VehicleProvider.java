package providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import models.Bicycle;
import models.Car;
import models.Moped;
import models.Vehicle;

import java.util.UUID;

public class VehicleProvider {
    private final CqlSession session;
    private final EntityHelper<Bicycle> bicycleHelper;
    private final EntityHelper<Car> carHelper;
    private final EntityHelper<Moped> mopedHelper;

    public VehicleProvider(MapperContext ctx, EntityHelper<Bicycle> bicycleHelper, EntityHelper<Car> carHelper, EntityHelper<Moped> mopedHelper) {
        this.session = ctx.getSession();
        this.bicycleHelper = bicycleHelper;
        this.carHelper = carHelper;
        this.mopedHelper = mopedHelper;
    }

    public void create(Vehicle vehicle) {
        session.execute(
                switch (vehicle.getDiscriminator()) {
                    case "bicycle" -> {
                        Bicycle bicycle = (Bicycle) vehicle;
                        yield session.prepare(bicycleHelper.insert().build())
                                .bind()
                                .setUuid("vehicle_id", bicycle.getVehicleId())
                                .setString("plate_number", bicycle.getPlateNumber())
                                .setString("brand", bicycle.getBrand())
                                .setInt("base_price", bicycle.getBasePrice())
                                .setBoolean("is_available", bicycle.isAvailable())
                                .setBoolean("archived", bicycle.isArchived())
                                .setString("discriminator", "bicycle");
                    }
                    case "car" -> {
                        Car car = (Car) vehicle;
                        yield session.prepare(carHelper.insert().build())
                                .bind()
                                .setUuid("vehicle_id", car.getVehicleId())
                                .setString("plate_number", car.getPlateNumber())
                                .setString("brand", car.getBrand())
                                .setInt("base_price", car.getBasePrice())
                                .setBoolean("is_available", car.isAvailable())
                                .setBoolean("archived", car.isArchived())
                                .setString("segment", String.valueOf(car.getSegment()))
                                .setDouble("engine_capacity", car.getEngineCapacity())
                                .setString("discriminator", "car");
                    }
                    case "moped" -> {
                        Moped moped = (Moped) vehicle;
                        yield session.prepare(mopedHelper.insert().build())
                                .bind()
                                .setUuid("vehicle_id", moped.getVehicleId())
                                .setString("plate_number", moped.getPlateNumber())
                                .setString("brand", moped.getBrand())
                                .setInt("base_price", moped.getBasePrice())
                                .setBoolean("is_available", moped.isAvailable())
                                .setBoolean("archived", moped.isArchived())
                                .setDouble("engine_capacity", moped.getEngineCapacity())
                                .setString("discriminator", "moped");
                    }
                    default -> throw new IllegalArgumentException();
                }
        );
    }

    public Vehicle findById(UUID vehicleId) {
        Select selectVehicle = QueryBuilder.selectFrom(CqlIdentifier.fromCql("vehicles"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("vehicle_id")).isEqualTo(QueryBuilder.literal(vehicleId)));
        try {
            Row row = session.execute(selectVehicle.build()).one();
            String discriminator = row.getString("discriminator");
            return switch (discriminator) {
                case "bicycle" -> getBicycle(row);
                case "car" -> getCar(row);
                case "moped" -> getMoped(row);
                default -> throw new IllegalArgumentException();
            };
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Bicycle getBicycle(Row row) {
        return new Bicycle(
                row.getUuid("vehicle_id"),
                row.getString("plate_number"),
                row.getString("brand"),
                row.getInt("base_price")
        );
    }

    private Car getCar(Row row) {
        return new Car(
                row.getUuid("vehicle_id"),
                row.getString("plate_number"),
                row.getString("brand"),
                row.getInt("base_price"),
                row.getString("segment").charAt(0),
                row.getDouble("engine_capacity")
        );
    }

    private Moped getMoped(Row row) {
        return new Moped(
                row.getUuid("vehicle_id"),
                row.getString("plate_number"),
                row.getString("brand"),
                row.getInt("base_price"),
                row.getDouble("engine_capacity")
        );
    }

    public void update(Vehicle vehicle) {
        try {
            session.execute(
                    switch (vehicle.getDiscriminator()) {
                        case "bicycle" -> {
                            Bicycle bicycle = (Bicycle) vehicle;
                            yield session.prepare(bicycleHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setString("plate_number", bicycle.getPlateNumber())
                                    .setString("brand", bicycle.getBrand())
                                    .setInt("base_price", bicycle.getBasePrice())
                                    .setBoolean("is_available", bicycle.isAvailable())
                                    .setBoolean("archived", bicycle.isArchived())
                                    .setUuid("vehicle_id", bicycle.getVehicleId());
                        }
                        case "car" -> {
                            Car car = (Car) vehicle;
                            yield session.prepare(carHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setString("plate_number", car.getPlateNumber())
                                    .setString("brand", car.getBrand())
                                    .setInt("base_price", car.getBasePrice())
                                    .setBoolean("is_available", car.isAvailable())
                                    .setBoolean("archived", car.isArchived())
                                    .setString("segment", String.valueOf(car.getSegment()))
                                    .setDouble("engine_capacity", car.getEngineCapacity())
                                    .setUuid("vehicle_id", car.getVehicleId());
                        }
                        case "moped" -> {
                            Moped moped = (Moped) vehicle;
                            yield session.prepare(mopedHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setString("plate_number", moped.getPlateNumber())
                                    .setString("brand", moped.getBrand())
                                    .setInt("base_price", moped.getBasePrice())
                                    .setBoolean("is_available", moped.isAvailable())
                                    .setBoolean("archived", moped.isArchived())
                                    .setDouble("engine_capacity", moped.getEngineCapacity())
                                    .setUuid("vehicle_id", moped.getVehicleId());
                        }
                        default -> throw new IllegalArgumentException();
                    }
            );
        } catch (NullPointerException e) {
            System.out.println("Vehicle does not exist");
        }
    }

    public void remove(UUID vehicleId) {
        Delete deleteVehicle = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("vehicles"))
                .where(Relation.column(CqlIdentifier.fromCql("vehicle_id")).isEqualTo(QueryBuilder.literal(vehicleId)));
        session.execute(deleteVehicle.build());
    }
}
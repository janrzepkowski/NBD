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
import models.Vehicle;

public class VehicleProvider {
    private final CqlSession session;
    private final EntityHelper<Car> carHelper;
    private final EntityHelper<Bicycle> bicycleHelper;

    public VehicleProvider(MapperContext ctx, EntityHelper<Car> carHelper, EntityHelper<Bicycle> bicycleHelper) {
        this.session = ctx.getSession();
        this.carHelper = carHelper;
        this.bicycleHelper = bicycleHelper;
    }

    public void create(Vehicle vehicle) {
        session.execute(
                switch (vehicle.getDiscriminator()) {
                    case "car" -> {
                        Car car = (Car) vehicle;
                        yield session.prepare(carHelper.insert().build())
                                .bind()
                                .setLong("vehicle_id", car.getVehicleId())
                                .setInt("base_price", car.getBasePrice())
                                .setInt("engine_capacity", car.getEngineCapacity())
                                .setString("discriminator", "car")
                                .setString("brand", car.getBrand())
                                .setInt("available", car.getAvailable());
                    }
                    case "bicycle" -> {
                        Bicycle bicycle = (Bicycle) vehicle;
                        yield session.prepare(bicycleHelper.insert().build())
                                .bind()
                                .setLong("vehicle_id", bicycle.getVehicleId())
                                .setInt("base_price", bicycle.getBasePrice())
                                .setString("discriminator", "bicycle")
                                .setString("brand", bicycle.getBrand())
                                .setInt("available", bicycle.getAvailable());
                    }
                    default -> throw new IllegalArgumentException();
                }
        );
    }

    public Vehicle findById(long vehicleId) {
        Select selectVehicle = QueryBuilder.selectFrom(CqlIdentifier.fromCql("vehicles"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("vehicle_id")).isEqualTo(QueryBuilder.literal(vehicleId)));
        try {
            Row row = session.execute(selectVehicle.build()).one();
            String discriminator = row.getString("discriminator");
            return switch (discriminator) {
                case "car" -> getCar(row);
                case "bicycle" -> getBicycle(row);
                default -> throw new IllegalArgumentException();
            };
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Car getCar(Row row) {
        return new Car(
                row.getLong("vehicle_id"),
                row.getInt("base_price"),
                row.getString("discriminator"),
                row.getString("brand"),
                row.getInt("engine_capacity")
        );
    }

    private Bicycle getBicycle(Row row) {
        return new Bicycle(
                row.getLong("vehicle_id"),
                row.getInt("base_price"),
                row.getString("brand")
        );
    }

    public void update(Vehicle vehicle) {
        try {
            session.execute(
                    switch (vehicle.getDiscriminator()) {
                        case "car" -> {
                            Car car = (Car) vehicle;
                            yield session.prepare(carHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setInt("base_price", car.getBasePrice())
                                    .setInt("engine_capacity", car.getEngineCapacity())
                                    .setInt("available", car.getAvailable())
                                    .setString("brand", car.getBrand())
                                    .setLong("vehicle_id", car.getVehicleId());
                        }
                        case "bicycle" -> {
                            Bicycle bicycle = (Bicycle) vehicle;
                            yield session.prepare(bicycleHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setInt("base_price", bicycle.getBasePrice())
                                    .setInt("available", bicycle.getAvailable())
                                    .setString("brand", bicycle.getBrand())
                                    .setLong("vehicle_id", bicycle.getVehicleId());
                        }
                        default -> throw new IllegalArgumentException();
                    }
            );
        } catch (NullPointerException e) {
            System.out.println("Vehicle does not exist");
        }
    }

    public void remove(long vehicleId) {
        Delete deleteVehicle = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("vehicles"))
                .where(Relation.column(CqlIdentifier.fromCql("vehicle_id")).isEqualTo(QueryBuilder.literal(vehicleId)));
        session.execute(deleteVehicle.build());
    }
}
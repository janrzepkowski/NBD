package repositories;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import models.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RedisVehicleRepository extends AbstractRedisRepository implements IVehicleRepository {

    private final String hashPrefix = "vehicle:";
    private final Jsonb jsonb = JsonbBuilder.create();

    public RedisVehicleRepository() {
        this.initDbConnection();
    }

    @Override
    public void create(Vehicle vehicle) {
        try {
            String key = hashPrefix + vehicle.getVehicleId();
            String json = jsonb.toJson(vehicle);
            pool.set(key, json);
            pool.expire(key, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vehicle read(UUID vehicleId) {
        try {
            String key = hashPrefix + vehicleId;
            String json = pool.get(key);
            if (json == null) {
                return null;
            }
            return jsonb.fromJson(json, Vehicle.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Vehicle> readAll() {
        try {
            Set<String> keys = pool.keys(hashPrefix + "*");
            List<Vehicle> vehicles = new ArrayList<>();
            for (String key : keys) {
                String json = pool.get(key);
                if (json != null) {
                    vehicles.add(jsonb.fromJson(json, Vehicle.class));
                }
            }
            return vehicles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Vehicle vehicle) {
        try {
            String key = hashPrefix + vehicle.getVehicleId();
            String json = jsonb.toJson(vehicle);
            pool.set(key, json);
            pool.expire(key, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Vehicle vehicle) {
        try {
            String key = hashPrefix + vehicle.getVehicleId();
            pool.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
        try {
            pool.flushAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        clearCache();
        pool.close();
    }
}
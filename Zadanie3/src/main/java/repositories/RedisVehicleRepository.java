package repositories;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import models.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RedisVehicleRepository extends AbstractRedisRepository {

    private final String hashPrefix = "vehicle:";
    private final Jsonb jsonb = JsonbBuilder.create();

    public RedisVehicleRepository() {
        this.initDbConnection();
    }

    public void create(Vehicle vehicle) {
        try {
            String key = hashPrefix + vehicle.getVehicleId().toString();
            String json = jsonb.toJson(vehicle);
            pool.set(key, json);
            pool.expire(key, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID vehicleId) {
        try {
            String key = hashPrefix + vehicleId.toString();
            pool.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Vehicle vehicle) {
        try {
            String key = hashPrefix + vehicle.getVehicleId().toString();
            String json = jsonb.toJson(vehicle);
            pool.set(key, json);
            pool.expire(key, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vehicle read(UUID vehicleId) {
        try {
            String key = hashPrefix + vehicleId.toString();
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

    public List<Vehicle> readAll() {
        try {
            List<Vehicle> vehicles = new ArrayList<>();
            Set<String> keys = pool.keys(hashPrefix + "*");
            for (String key : keys) {
                String json = pool.get(key);
                Vehicle vehicle = jsonb.fromJson(json, Vehicle.class);
                vehicles.add(vehicle);
            }
            return vehicles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
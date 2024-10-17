package repositories;

import jakarta.persistence.EntityManager;
import models.Vehicle;

import java.util.List;
import java.util.UUID;

public class VehicleRepository implements Repository<Vehicle> {

    private final EntityManager em;

    public VehicleRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Vehicle get(UUID id) {
        return em.find(Vehicle.class, id);
    }

    @Override
    public List<Vehicle> getAll() {
        return em.createQuery("FROM Vehicle v", Vehicle.class).getResultList();
    }

    @Override
    public Vehicle add(Vehicle vehicle) {
        try {
            em.getTransaction().begin();
            em.persist(vehicle);
            em.getTransaction().commit();
            return vehicle;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add vehicle: " + vehicle.getVehicleId(), e);
        }
    }

    @Override
    public void remove(Vehicle vehicle) {
        try {
            em.getTransaction().begin();
            Vehicle managedVehicle = em.contains(vehicle) ? vehicle : em.merge(vehicle);
            em.remove(managedVehicle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to remove vehicle: " + vehicle.getVehicleId(), e);
        }
    }

    @Override
    public void update(Vehicle vehicle) {
        try {
            em.getTransaction().begin();
            em.merge(vehicle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update vehicle: " + vehicle.getVehicleId(), e);
        }
    }
}
package repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Vehicle;

import java.util.List;
import java.util.UUID;

public class VehicleRepository implements Repository<Vehicle> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @Override
    public Vehicle get(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Vehicle.class, id);
        }
    }

    @Override
    public List<Vehicle> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("FROM Vehicle v", Vehicle.class).getResultList();
        }
    }

    @Override
    public Vehicle add(Vehicle vehicle) {
        EntityManager em = emf.createEntityManager();
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
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Vehicle vehicle) {
        EntityManager em = emf.createEntityManager();
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
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Vehicle vehicle) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(vehicle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update vehicle: " + vehicle.getVehicleId(), e);
        } finally {
            em.close();
        }
    }
}

package repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import models.Vehicle;

import java.util.List;
import java.util.UUID;

public class VehicleRepository implements Repository<Vehicle> {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @Override
    public Vehicle get(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Vehicle.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get vehicle: " + id, e);
        }
    }

    @Override
    public List<Vehicle> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Vehicle> vehicles = em.createQuery("FROM Vehicle v", Vehicle.class)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
            em.getTransaction().commit();
            return vehicles;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to get all vehicles", e);
        } finally {
            em.close();
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
            em.getTransaction().rollback();
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

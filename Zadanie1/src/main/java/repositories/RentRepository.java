package repositories;

import jakarta.persistence.*;
import models.Client;
import models.Rent;
import models.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RentRepository implements Repository<Rent> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @Override
    public Rent get(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Rent.class, id);
        }
    }

    @Override
    public List<Rent> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("FROM Rent r", Rent.class).getResultList();
        }
    }

    @Override
    public Rent add(Rent rent) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(rent);
            em.getTransaction().commit();
            return rent;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add rent: " + rent.getRentId(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Rent rent) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Rent managedRent = em.contains(rent) ? rent : em.merge(rent);
            em.remove(managedRent);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to remove rent: " + rent.getRentId(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Rent rent) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(rent);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update rent: " + rent.getRentId(), e);
        } finally {
            em.close();
        }
    }

    public void bookVehicle(Client client, Vehicle vehicle, LocalDateTime rentStart) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Vehicle managedVehicle = em.find(Vehicle.class, vehicle.getVehicleId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Client managedClient = em.find(Client.class, client.getClientId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Rent rent = new Rent(managedClient, managedVehicle, rentStart);
            em.persist(rent);
            managedVehicle.setAvailable(false);
            em.merge(managedVehicle);
            managedClient.setRents(managedClient.getRents() + 1);
            em.merge(managedClient);
            em.getTransaction().commit();
        } catch (OptimisticLockException ole) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Optimistic lock exception: The vehicle or client was modified concurrently: " + vehicle.getVehicleId(), ole);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("The vehicle could not be booked: " + vehicle.getVehicleId(), e);
        } finally {
            em.close();
        }
    }
}
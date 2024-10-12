package repositories;

import jakarta.persistence.*;
import models.Client;

import java.util.List;
import java.util.UUID;

public class ClientRepository implements Repository<Client> {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @Override
    public Client get(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Client.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get client: " + id, e);
        }
    }


    @Override
    public List<Client> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("FROM Client c", Client.class);
            q.setLockMode(LockModeType.PESSIMISTIC_WRITE);
            List<Client> clients = q.getResultList();
            em.getTransaction().commit();
            return clients;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to get all clients", e);
        } finally {
            em.close();
        }
    }


    @Override
    public Client add(Client client) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
            return client;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to add client: " + client.getClientId(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean remove(Client client) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Client managedClient = em.contains(client) ? client : em.merge(client);
            em.remove(managedClient);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to remove client: " + client.getClientId(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Client update(Client client) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Client updatedClient = em.merge(client);
            em.getTransaction().commit();
            return updatedClient;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update client: " + client.getClientId(), e);
        }
    }

}
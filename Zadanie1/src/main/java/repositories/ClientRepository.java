package repositories;

import jakarta.persistence.*;
import models.Client;

import java.util.List;
import java.util.UUID;

public class ClientRepository implements Repository<Client> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @Override
    public Client get(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Client.class, id);
        }
    }

    @Override
    public List<Client> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            Query query = em.createQuery("FROM Client c", Client.class);
            return query.getResultList();
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
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add client: " + client.getClientId(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Client client) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Client managedClient = em.contains(client) ? client : em.merge(client);
            em.remove(managedClient);
            em.getTransaction().commit();
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
    public void update(Client client) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(client);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update client: " + client.getClientId(), e);
        } finally {
            em.close();
        }
    }
}

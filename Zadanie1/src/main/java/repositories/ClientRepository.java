package repositories;

import jakarta.persistence.EntityManager;
import models.Client;

import java.util.List;
import java.util.UUID;

public class ClientRepository implements Repository<Client> {

    private final EntityManager em;

    public ClientRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Client get(UUID id) {
        return em.find(Client.class, id);
    }

    @Override
    public List<Client> getAll() {
        return em.createQuery("FROM Client c", Client.class).getResultList();
    }

    @Override
    public Client add(Client client) {
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
        }
    }

    @Override
    public void remove(Client client) {
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
        }
    }

    @Override
    public void update(Client client) {
        try {
            em.getTransaction().begin();
            em.merge(client);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update client: " + client.getClientId(), e);
        }
    }
}
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import managers.ClientManager;
import models.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ClientManagerTest {

    private static ClientRepository clientRepository;
    private static ClientManager clientManager;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        clientRepository = new ClientRepository(entityManager);
        clientManager = new ClientManager(clientRepository);
    }

    @Test
    void testRegisterClientWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ClientManager(null);
        });

        assertEquals("clientRepository cannot be null", exception.getMessage());
    }

    @Test
    void testRegisterClientWithExistingId() {
        Client client = new Client("Tyler", "Okonma", "1234567890");
        clientRepository.add(client);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clientManager.registerClient(client);
        });

        assertEquals("Client with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testUnregisterClient() {
        Client client = new Client("Kali", "Uchis", "987654321");
        clientRepository.add(client);

        clientManager.unregisterClient(client);

        Client unregisteredClient = clientRepository.get(client.getClientId());
        assertTrue(unregisteredClient.isArchived());
    }
}
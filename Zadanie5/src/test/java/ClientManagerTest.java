import managers.ClientManager;
import models.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ClientManagerTest {

    private static ClientRepository clientRepository;
    private static ClientManager clientManager;

    @BeforeAll
    public static void setUp() {
        clientRepository = new ClientRepository();
        clientManager = new ClientManager(clientRepository);
    }

    @BeforeEach
    public void cleanUp() {
        clientRepository.getDatabase().getCollection("clients", Client.class).drop();
    }

    @AfterAll
    public static void tearDown() {
        clientRepository.getDatabase().getCollection("clients", Client.class).drop();
        clientRepository.close();
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
        clientRepository.create(client);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clientManager.registerClient(client);
        });

        assertEquals("Client with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testUnregisterClient() {
        Client client = new Client("Kali", "Uchis", "987654321");
        clientRepository.create(client);

        clientManager.unregisterClient(client);

        Client unregisteredClient = clientRepository.read(client.getClientId());
        assertTrue(unregisteredClient.isArchived());
    }
}
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import managers.ClientManager;
import models.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.AbstractCassandraRepository;
import repositories.ClientRepository;

import static org.junit.jupiter.api.Assertions.*;

class ClientManagerTest {

    private static CqlSession session;
    private static ClientRepository clientRepository;
    private static ClientManager clientManager;

    @BeforeAll
    static void setUp() {
        AbstractCassandraRepository abstractCassandraRepository = new AbstractCassandraRepository();
        session = abstractCassandraRepository.getSession();
        clientRepository = new ClientRepository(session);
        clientManager = new ClientManager(clientRepository);
    }

    @AfterEach
    void dropDB() {
        Truncate truncate = QueryBuilder.truncate("clients");
        session.execute(truncate.build());
    }

    @Test
    void testClientManagerWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ClientManager(null));

        assertEquals("clientRepository cannot be null", exception.getMessage());
    }

    @Test
    void testRegisterClient() {
        Client client = new Client(1, "John", "Doe", "123456789", false);
        clientManager.registerClient(1, "John", "Doe", "123456789", false);

        Client registeredClient = clientRepository.read(1);
        assertEquals(client, registeredClient);
    }

    @Test
    void testRegisterClientWithExistingId() {
        Client client = new Client(2, "Jane", "Smith", "987654321", false);
        clientRepository.create(client);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> clientManager.registerClient(2, "Jane", "Smith", "987654321", false));

        assertEquals("Client with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testUpdateClient() {
        clientManager.registerClient(4, "Bob", "Brown", "444444444", false);
        Client client = clientManager.getClient(4);
        assertEquals("Bob", client.getFirstName());
        assertEquals("Brown", client.getLastName());

        clientManager.updateClient(4, "Robert", "Brownson", "444444444", false);
        Client updatedClient = clientManager.getClient(4);
        assertEquals("Robert", updatedClient.getFirstName());
        assertEquals("Brownson", updatedClient.getLastName());
    }

    @Test
    void testDeleteClient() {
        clientManager.registerClient(6, "David", "Black", "222222222", false);
        Client client = clientManager.getClient(6);
        assertNotNull(client);

        clientManager.deleteClient(6);
        Client deletedClient = clientManager.getClient(6);
        assertNull(deletedClient);
    }

    @Test
    void testUnregisterClient() {
        clientManager.registerClient(7, "Alice", "White", "333333333", false);
        Client client = clientManager.getClient(7);
        assertNotNull(client);
        assertFalse(client.isArchived());

        clientManager.unregisterClient(7);
        Client unregisteredClient = clientManager.getClient(7);
        assertNotNull(unregisteredClient);
        assertTrue(unregisteredClient.isArchived());
    }
}
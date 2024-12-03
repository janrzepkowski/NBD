import models.Client;
import org.junit.jupiter.api.*;
import repositories.ClientRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {

    private static ClientRepository clientRepository;

    @BeforeAll
    public static void setUp() {
        clientRepository = new ClientRepository();
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
    void testAddClient() {
        Client client1 = new Client("Tyler", "Okonma", "1234567890");
        Client client2 = new Client("Lonny", "Breaux", "6131725069");

        clientRepository.create(client1);
        clientRepository.create(client2);

        Client foundClient1 = clientRepository.read(client1.getClientId());
        Client foundClient2 = clientRepository.read(client2.getClientId());

        assertEquals(client1, foundClient1);
        assertEquals(client2, foundClient2);
    }

    @Test
    void testRemoveClient() {
        Client client = new Client("Charlotte", "Aitchison", "1223344556");
        clientRepository.create(client);

        Client foundClient1 = clientRepository.read(client.getClientId());
        assertNotNull(foundClient1);

        clientRepository.delete(client);

        Client foundClient2 = clientRepository.read(client.getClientId());
        assertNull(foundClient2);
    }

    @Test
    void testUpdateClient() {
        Client client = new Client("Lonny", "Breaux", "1613172506");
        clientRepository.create(client);

        Client foundClient1 = clientRepository.read(client.getClientId());
        assertEquals("Lonny", foundClient1.getFirstName());
        assertEquals("Breaux", foundClient1.getLastName());
        assertEquals("1613172506", foundClient1.getPhoneNumber());

        client.setFirstName("Frank");
        client.setLastName("Ocean");
        client.setPhoneNumber("6782252252");

        clientRepository.update(client);

        Client foundClient2 = clientRepository.read(client.getClientId());
        assertEquals("Frank", foundClient2.getFirstName());
        assertEquals("Ocean", foundClient2.getLastName());
        assertEquals("6782252252", foundClient2.getPhoneNumber());
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = clientRepository.readAll();
        int initialSize = clients.size();

        Client client1 = new Client("Tyler", "Okonma", "1234567890");
        Client client2 = new Client("Lonny", "Breaux", "6131725069");

        clientRepository.create(client1);
        clientRepository.create(client2);

        clients = clientRepository.readAll();
        int finalSize = clients.size();

        assertEquals(initialSize + 2, finalSize);
    }
}
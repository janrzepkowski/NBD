import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Client;
import repositories.ClientRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {

    private static ClientRepository clientRepository;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        clientRepository = new ClientRepository(entityManager);
    }

    @Test
    void testAddClient() {
        Client client1 = new Client("Tyler", "Okonma", "1234567890");
        Client client2 = new Client("Lonny", "Breaux", "6131725069");

        clientRepository.add(client1);
        clientRepository.add(client2);

        Client foundClient1 = clientRepository.get(client1.getClientId());
        Client foundClient2 = clientRepository.get(client2.getClientId());

        assertEquals(client1, foundClient1);
        assertEquals(client2, foundClient2);
    }

    @Test
    void testRemoveClient() {
        Client client = new Client("Charlotte", "Aitchison", "1223344556");
        clientRepository.add(client);

        Client foundClient1 = clientRepository.get(client.getClientId());
        assertNotNull(foundClient1);

        clientRepository.remove(client);

        Client foundClient2 = clientRepository.get(client.getClientId());
        assertNull(foundClient2);
    }

    @Test
    void testUpdateClient() {
        Client client = new Client("Lonny", "Breaux", "1613172506");
        clientRepository.add(client);

        Client foundClient1 = clientRepository.get(client.getClientId());
        assertEquals("Lonny", foundClient1.getFirstName());
        assertEquals("Breaux", foundClient1.getLastName());
        assertEquals("1613172506", foundClient1.getPhoneNumber());

        client.setFirstName("Frank");
        client.setLastName("Ocean");
        client.setPhoneNumber("6782252252");

        clientRepository.update(client);

        Client foundClient2 = clientRepository.get(client.getClientId());
        assertEquals("Frank", foundClient2.getFirstName());
        assertEquals("Ocean", foundClient2.getLastName());
        assertEquals("6782252252", foundClient2.getPhoneNumber());
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = clientRepository.getAll();
        int initialSize = clients.size();

        Client client1 = new Client("Tyler", "Okonma", "1234567890");
        Client client2 = new Client("Lonny", "Breaux", "6131725069");

        clientRepository.add(client1);
        clientRepository.add(client2);

        clients = clientRepository.getAll();
        int finalSize = clients.size();

        assertEquals(initialSize + 2, finalSize);
    }
}
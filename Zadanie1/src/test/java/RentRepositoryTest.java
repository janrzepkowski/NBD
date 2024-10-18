import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;
import repositories.RentRepository;
import repositories.VehicleRepository;
import models.Rent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RentRepositoryTest {

    private static RentRepository rentRepository;
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static VehicleRepository vehicleRepository;
    private static ClientRepository clientRepository;
    private static EntityManager em;

    @BeforeAll
    public static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
        rentRepository = new RentRepository(entityManager);
        clientRepository = new ClientRepository(entityManager);
        vehicleRepository = new VehicleRepository(entityManager);
        em = entityManagerFactory.createEntityManager();
    }

    @Test
    void testAddRent() {
        Client client = new Client("John", "Doe", "11111111110");
        Car car = new Car("MAG 1C", "BMW", 150, 'D', 2.5);

        clientRepository.add(client);
        vehicleRepository.add(car);

        Rent rent = new Rent(client, car, LocalDateTime.now());
        rentRepository.add(rent);
        Rent foundRent = rentRepository.get(rent.getRentId());

        assertEquals(rent, foundRent);
    }

    @Test
    void testRemoveRent() {
        Client client = new Client("Jane", "Doe", "11111111111");
        Car car = new Car("AMS 1", "Ford", 120, 'D', 2.2);

        clientRepository.add(client);
        vehicleRepository.add(car);

        Rent rent = new Rent(client, car, LocalDateTime.now());
        rentRepository.add(rent);

        Rent foundRent1 = rentRepository.get(rent.getRentId());
        assertNotNull(foundRent1);

        rentRepository.remove(rent);

        Rent foundRent2 = rentRepository.get(rent.getRentId());
        assertNull(foundRent2);
    }

    @Test
    void testUpdateRent() {
        Client client = new Client("Robert", "Smith", "22222222221");
        Car car = new Car("QRS789", "Vespa", 80, 'A', 2.0);
        Rent rent = new Rent(client, car, LocalDateTime.now());

        em.getTransaction().begin();
        em.persist(client);
        em.persist(car);
        em.getTransaction().commit();

        rentRepository.add(rent);
        UUID rentId = rent.getRentId();
        assertNotNull(rentId,"The rent ID should not be null after persisting");
        Rent retrievedRent = rentRepository.get(rentId);
        assertNotNull(retrievedRent, "The retrieved rent should not be null");
        assertEquals(rentId, retrievedRent.getRentId(), "The retrieved rent ID should match the persisted rent ID");
    }

    @Test
    void testGetAllRents() {


        List<Rent> rents = rentRepository.getAll();
        int initialSize = rents.size();

        Client client1 = new Client("Alice", "Johnson", "33333333332");
        Client client2 = new Client("Michael", "Brown", "44444444443");
        Car car1 = new Car("JKL456", "Mazda", 110, 'A', 1.6);
        Car car2 = new Car("MNO789", "Subaru", 160, 'B', 2.0);

        entityManager.getTransaction().begin();
        entityManager.persist(client1);
        entityManager.persist(client2);
        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.getTransaction().commit();

        Rent rent1 = new Rent(client1, car1, LocalDateTime.now());
        Rent rent2 = new Rent(client2, car2, LocalDateTime.now());

        rentRepository.add(rent1);
        rentRepository.add(rent2);

        rents = rentRepository.getAll();
        int finalSize = rents.size();

        assertEquals(initialSize + 2, finalSize);
    }

    @Test
    void testBookVehicle() {
        entityManager.getTransaction().begin();

        Client client = new Client("Lucas", "White", "55555555554");
        Car car = new Car("XYZ789", "Toyota", 100, 'B', 1.8);

        entityManager.persist(client);
        entityManager.persist(car);

        entityManager.getTransaction().commit();

        rentRepository.bookVehicle(client, car, LocalDateTime.now());

        Vehicle foundCar = entityManager.find(Vehicle.class, car.getVehicleId());
        assertFalse(foundCar.isAvailable());

        Client foundClient = entityManager.find(Client.class, client.getClientId());
        assertEquals(1, foundClient.getRents());
    }
}
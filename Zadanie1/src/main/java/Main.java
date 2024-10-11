import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Client;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

        Client client = new Client("John", "Doe", "1234567890");
        Client client2 = new Client("Jane", "Smith", "0987654321");

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(client); // persist nie działa
            em.merge(client2);
            em.getTransaction().commit();
        }
    }
}
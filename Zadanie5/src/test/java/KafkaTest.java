import com.mongodb.client.model.Filters;
import org.apache.kafka.clients.producer.Producer;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import models.*;
import repositories.RentRepository;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class KafkaTest {

    @Test
    void testSendRent() throws ExecutionException, InterruptedException {
        Client client = new Client("Adam", "Małysz", "123456789");

        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);

        LocalDateTime startDate = LocalDateTime.now();
        Rent rent = new Rent(client, car, startDate);

        CustomKafkaProducer producer = new CustomKafkaProducer();

        producer.sendRent(rent);
    }

    @Test
    void testProducerConsumer() throws ExecutionException, InterruptedException {
        CustomKafkaProducer producer = new CustomKafkaProducer();
        CustomKafkaProducer.initProducer();

        Client client = new Client("Adam", "Małysz", "123456789");

        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);

        LocalDateTime startDate = LocalDateTime.now();
        Rent rent = new Rent(client, car, startDate);

        producer.sendRent(rent);

        Thread.sleep(1000);

        Bson filter = Filters.eq("client.firstName", "Adam");
        Rent savedRent = RentRepository.getDatabase().getCollection("rents", Rent.class).find(filter).first();
        //Assertions.assertNotNull(savedRent);
        Assertions.assertEquals(RentManagerTest.rentRepository.readAll().getFirst().getRentId(), rent.getRentId());
    }
}

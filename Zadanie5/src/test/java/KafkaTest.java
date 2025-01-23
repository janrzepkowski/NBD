import org.junit.jupiter.api.Test;
import models.*;

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
}
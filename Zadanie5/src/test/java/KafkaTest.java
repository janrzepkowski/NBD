import kafka.CustomKafkaProducer;
import kafka.KafkaConsumer;
import models.Car;
import models.Client;
import models.Rent;
import org.bson.Document;
import org.junit.jupiter.api.*;
import repositories.RentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaTest {

    private KafkaConsumer kafkaConsumer1;
    private KafkaConsumer kafkaConsumer2;
    private Thread consumerThread1;
    private Thread consumerThread2;
    private RentRepository rentRepository;
    private CustomKafkaProducer producer;

    @BeforeAll
    public void setUp() throws ExecutionException, InterruptedException {
        rentRepository = new RentRepository();
        kafkaConsumer1 = new KafkaConsumer(1);
        kafkaConsumer2 = new KafkaConsumer(1);
        kafkaConsumer1.initConsumers();
        kafkaConsumer2.initConsumers();

        consumerThread1 = new Thread(() -> {
            try {
                kafkaConsumer1.consumeTopicByAllConsumers();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumerThread2 = new Thread(() -> {
            try {
                kafkaConsumer2.consumeTopicByAllConsumers();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumerThread1.start();
        consumerThread2.start();

        producer = new CustomKafkaProducer();
    }

    @BeforeEach
    public void cleanDatabase() {
        rentRepository.getDatabase().getCollection("messages").drop();
    }

    @AfterAll
    public void tearDown() {
        consumerThread1.interrupt();
        consumerThread2.interrupt();
    }

    @Test
    public void testConsumerAssignment() throws InterruptedException {
        Thread.sleep(5000);
        try {
            synchronized (kafkaConsumer1) {
                kafkaConsumer1.getKafkaConsumers().forEach(consumer -> {
                    synchronized (consumer) {
                        assertFalse(consumer.assignment().isEmpty());
                    }
                });
            }
            synchronized (kafkaConsumer2) {
                kafkaConsumer2.getKafkaConsumers().forEach(consumer -> {
                    synchronized (consumer) {
                        assertFalse(consumer.assignment().isEmpty());
                    }
                });
            }
        } catch (ConcurrentModificationException e) {
            System.err.println("ConcurrentModificationException caught and ignored: " + e.getMessage());
        }
    }

    @Test
    public void testCreateAndReceiveRentals() throws InterruptedException {
        Client client = new Client("John", "Doe", "1234567890");
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        Rent rent = new Rent(client, car, LocalDateTime.now());
        producer.sendRent(rent);
        Thread.sleep(5000);
        List<Document> messages = rentRepository.getDatabase().getCollection("messages").find().into(new ArrayList<>());
        assertFalse(messages.isEmpty());
    }

    @Test
    public void testConsumerRestart() throws InterruptedException {
        Client client = new Client("John", "Doe", "1234567890");
        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
        Rent rent = new Rent(client, car, LocalDateTime.now());
        producer.sendRent(rent);
        Thread.sleep(5000);
        consumerThread1.interrupt();
        consumerThread2.interrupt();
        Thread.sleep(2000);
        consumerThread1 = new Thread(() -> {
            try {
                kafkaConsumer1.consumeTopicByAllConsumers();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumerThread2 = new Thread(() -> {
            try {
                kafkaConsumer2.consumeTopicByAllConsumers();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumerThread1.start();
        consumerThread2.start();
        Thread.sleep(5000);
        List<Document> messages = rentRepository.getDatabase().getCollection("messages").find().into(new ArrayList<>());
        assertEquals(1, messages.size());
    }
}
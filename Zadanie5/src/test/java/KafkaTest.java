//import com.mongodb.client.model.Filters;
//import kafka.CustomKafkaProducer;
//import kafka.KafkaConsumer;
//import models.Car;
//import models.Client;
//import models.Rent;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.junit.jupiter.api.*;
//import repositories.RentRepository;
//
//import java.time.LocalDateTime;
//import java.util.concurrent.ExecutionException;
//
//public class KafkaTest {
//
//    private static KafkaConsumer kafkaConsumer;
//    private static Thread consumerThread;
//    private static RentRepository rentRepository;
//
//    @BeforeAll
//    public static void setUp() throws ExecutionException, InterruptedException {
//        rentRepository = new RentRepository();
//        kafkaConsumer = new KafkaConsumer(2);
//        kafkaConsumer.initConsumers();
//
//        consumerThread = new Thread(() -> {
//            try {
//                kafkaConsumer.consumeTopicByAllConsumers();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        consumerThread.start();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        consumerThread.interrupt();
//        kafkaConsumer.getKafkaConsumers().forEach(org.apache.kafka.clients.consumer.KafkaConsumer::wakeup);
//    }
//
//    @BeforeEach
//    public void clearDatabase() {
//        rentRepository.getDatabase().getCollection("rents", Rent.class).deleteMany(new Document());
//        rentRepository.getDatabase().getCollection("messages", Rent.class).deleteMany(new Document());
//    }
//
//    @Test
//    void testSendRent() throws ExecutionException, InterruptedException {
//        Client client = new Client("Adam", "Małysz", "123456789");
//        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
//        LocalDateTime startDate = LocalDateTime.now();
//        Rent rent = new Rent(client, car, startDate);
//
//        CustomKafkaProducer producer = new CustomKafkaProducer();
//        producer.sendRent(rent);
//    }
//
//    @Test
//    void testProducerConsumer() throws ExecutionException, InterruptedException {
//        CustomKafkaProducer producer = new CustomKafkaProducer();
//        CustomKafkaProducer.initProducer();
//
//        Client client = new Client("Adam", "Małysz", "123456789");
//        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
//        LocalDateTime startDate = LocalDateTime.now();
//        Rent rent = new Rent(client, car, startDate);
//
//        producer.sendRent(rent);
//
//        long timeout = System.currentTimeMillis() + 60000;
//        Bson filter = Filters.eq("client.firstName", "Adam");
//        Rent savedRent = null;
//
//        while (System.currentTimeMillis() < timeout) {
//            savedRent = rentRepository.getDatabase().getCollection("rents", Rent.class).find(filter).first();
//            if (savedRent != null) {
//                break;
//            }
//            Thread.sleep(100);
//        }
//
//        Assertions.assertNotNull(savedRent);
//        Assertions.assertEquals(rent.getRentId(), savedRent.getRentId());
//    }
//}
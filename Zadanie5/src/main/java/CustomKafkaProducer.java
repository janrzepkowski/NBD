import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import models.Rent;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CustomKafkaProducer {
    static KafkaProducer<UUID, String> kafkaProducer;
    private final String RENT_TOPIC = "rents";

    public CustomKafkaProducer() throws ExecutionException, InterruptedException {
        initProducer();
    }

    public static void initProducer() throws ExecutionException, InterruptedException {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "kafka1:9192,kafka2:9292,kafka3:9392");
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
        producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        kafkaProducer = new KafkaProducer<>(producerConfig);
    }

    public void sendRent(Rent rent) throws InterruptedException {
        createTopic();
        Jsonb jsonb = JsonbBuilder.create();
        Callback callback= this::onCompletion;
        String jsonClient = jsonb.toJson(rent);
        System.out.println(jsonClient);
        ProducerRecord<UUID, String> record = new ProducerRecord<>(RENT_TOPIC, rent.getRentId(), jsonClient);
        kafkaProducer.send(record,callback);
    }

    private void onCompletion (RecordMetadata data, Exception exception) {
        if (exception == null) {
            System.out.println(data.offset());
        } else {
            System.out.println(exception);
        }
    }

    public void createTopic() throws InterruptedException { Properties properties = new Properties();
        properties.put
                (AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka1:9292, kafka1:9392");
        int partitionsNumber = 5;
        short replicationFactor = 3;
        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(RENT_TOPIC, partitionsNumber, replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs(1000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult result = admin.createTopics (List.of (newTopic), options);
            KafkaFuture<Void> futureResult = result.values().get(RENT_TOPIC); futureResult.get();
        } catch (ExecutionException e) {
            System.out.println("Topic already exists"+e.getCause());
        }
    }
}
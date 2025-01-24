import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import repositories.MessageRepository;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomKafkaConsumer {
    List<KafkaConsumer<UUID, String>> kafkaConsumers = new ArrayList<>();
    private final String RENT_TOPIC = "rents";
    private int numberOfConsumers;
    private MessageRepository messageRepository;

    public CustomKafkaConsumer(int numConsumers) throws ExecutionException, InterruptedException {
        this.numberOfConsumers = numConsumers;
        this.messageRepository = new MessageRepository();
        initConsumers();
    }

    public void initConsumers() throws ExecutionException, InterruptedException {
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "group-rents");
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        if (kafkaConsumers.isEmpty()) {
            for (int i = 0; i < numberOfConsumers; i++) {
                KafkaConsumer<UUID, String> kafkaConsumer = new KafkaConsumer<>(consumerConfig);
                kafkaConsumer.subscribe(Collections.singleton(RENT_TOPIC));
                kafkaConsumers.add(kafkaConsumer);
                System.out.println("Creating consumer " + (i + 1));
            }
        }
    }

    public void consume(KafkaConsumer<UUID, String> consumer) {
        boolean saved = false;
        try {
            consumer.poll(Duration.ofMillis(1000));
            Set<TopicPartition> consumerAssignment = consumer.assignment();
            consumer.seekToBeginning(consumerAssignment);
            Duration timeout = Duration.ofMillis(100);
            MessageFormat formatter = new MessageFormat("Consumer {5}, Topic {0}, Partition {1}, Offset {2, number, integer}, Key {3}, Value {4}");

            while (true) {
                ConsumerRecords<UUID, String> records = consumer.poll(timeout);

                for (ConsumerRecord<UUID, String> record : records) {
                    String result = formatter.format(new Object[]{
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value(),
                            consumer.groupMetadata().memberId()
                    });

                    if (!saved) {
                        messageRepository.saveMessageToRepository(record.value());
                    }
                    saved = true;
                    System.out.println(result);
                    consumer.commitAsync();
                }
            }
        } catch (WakeupException we) {
            System.out.println("Job Finished");
        }
    }

    public void consumeTopicByAllConsumers() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfConsumers);
        for (KafkaConsumer<UUID, String> consumer : kafkaConsumers) {
            executorService.execute(() -> consume(consumer));
        }
        Thread.sleep(10000);
        for (KafkaConsumer<UUID, String> consumer : kafkaConsumers) {
            consumer.wakeup();
        }
        executorService.shutdown();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CustomKafkaConsumer consumer = new CustomKafkaConsumer(2);
        consumer.consumeTopicByAllConsumers();
    }
}
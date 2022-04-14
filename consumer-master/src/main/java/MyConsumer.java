import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MyConsumer {
    private static final String ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        //generate trapdoor
        String enc_topic_name = MatchingSchema.Enc("topic-2", MatchingSchema.EJ1);
        System.out.println("Trapdoor is " + enc_topic_name);

        //get topic info
        ZooKeeper zk_client = new ZooKeeper(ZOOKEEPER_ADDRESS, 10000, null);
        String topicInfo = zk_client.enc_match(enc_topic_name);
        try {
            //get topic name
            JSONObject json = new JSONObject(topicInfo);
            String topic_name = json.getString("topic_name");

            //get broker address
            String brokerAdress = json.getString("brokerAddress");
            System.out.println("Connecting to " + brokerAdress + "...");

            //create consumer
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAdress);
            props.put(ConsumerConfig.GROUP_ID_CONFIG,
                    "groupConsumer");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    LongDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class.getName());
            Consumer<Long, String> consumer =
                    new KafkaConsumer<>(props);

            //subscribe topic
            System.out.println("Subscribe topic " + topic_name);
            consumer.subscribe(Collections.singletonList(topic_name));
            int giveUp = 50;
            int noRecordsCount = 0;
            while (true) {
                final ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
                if (consumerRecords.count() == 0) {
                    noRecordsCount++;
                    if (noRecordsCount > giveUp) break;
                    else continue;
                }
                consumerRecords.forEach(record -> {
                    System.out.printf("Received record: (key=%s value=%s) meta(partition=%d, offset=%d)\n", record.key(), record.value(), record.partition(), record.offset());
                });
                consumer.commitAsync();
            }
            consumer.close();
            System.out.println("DONE");
        } catch (Exception exception) {
            System.out.println(topicInfo);
        }
    }
}

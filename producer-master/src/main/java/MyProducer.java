import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.zookeeper.ZooKeeper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MyProducer {
    private static final String ZOOKEEPER_ADDRESS = "127.0.0.1:2181";
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        //get all available brokers
        ZooKeeper zk_client = new ZooKeeper(ZOOKEEPER_ADDRESS, 10000,null);
        List<String> brokersAddressList = zk_client.getBrokersAddressList();

        //using first broker
        JSONObject json = new JSONObject(brokersAddressList.get(0));
        String brokerAdress = json.getString("host")+":"+json.getInt("port");
        System.out.println("Connecting to "+brokerAdress+"...");

        //create producer
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAdress);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "myProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<Long, String> producer = new KafkaProducer<>(props);

        //Publish message to topic
        String enc_topic_name = MatchingSchema.Enc("topic-2",MatchingSchema.EI1);
        System.out.println("Publish to "+enc_topic_name);
        long time = System.currentTimeMillis();
        try {
            for (long index = time; index < time + 10; index++) {
                ProducerRecord<Long, String> record = new ProducerRecord<>(enc_topic_name, index,"Hello at " + index);
                RecordMetadata metadata = producer.send(record).get();
                long elapsedTime = System.currentTimeMillis() - time;
                System.out.printf("Sent record: (key=%s value=%s) meta(partition=%d, offset=%d) time=%d\n", record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);
                Thread.sleep(4000);
            }
        } finally {
            producer.flush();
            producer.close();
        }
        System.out.println("DONE");
    }
}

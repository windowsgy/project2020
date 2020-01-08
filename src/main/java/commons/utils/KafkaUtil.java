package commons.utils;

/**
 * Created by jlgaoyuan on 2017/9/8.
 */
public class KafkaUtil {
/*
    private Properties props = new Properties();

    public boolean init(String brokerSocketStr) {
        try {
            props.put("bootstrap.servers", brokerSocketStr); //设置Brokers
            props.put("group.id", "transmitAlarm");
            *//* 是否自动确认offset *//**//*
            props.put("enable.auto.commit", "true");
            /* 自动确认offset的时间间隔 *//*
            props.put("auto.commit.interval.ms", "1000");
            *//*超时时间*//*
            props.put("session.timeout.ms", "30000");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.error("Kafka Init Error" + e.getMessage());
            return false;
        }
    }

    *//**
     * 发送告警消息
     *
     * @param topic topic
     * @param list  消息列表
     *//*

    public void sendToKafka(String topic, List<String> list) {
        Log.info("To Kafka Topic:" + topic);
        KafkaProducer producer = new KafkaProducer(props);
        for (String aList : list) {
            producer.send(new ProducerRecord<String, String>(topic, aList));
        }
        Log.info("Send To Kafka Count" + list.size());
        producer.close();
    }


    *//**
     * 读取 topic
     *
     * @param topic Topic
     * @return List<String>
     *//*
    public List<String> getTopic(String topic) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        List<String> list = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(3000);
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            list.add(record.value());
        }
        Log.debug("get size :" + list.size());
        return list;
    }*/
}
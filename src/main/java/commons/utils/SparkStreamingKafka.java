package commons.utils;

public class SparkStreamingKafka {

    public static void run() {
        /*String brokers = Params.kafkaIps;
        String topics = Params.kafkaSendAlarmTopic;
        SparkConf conf = new SparkConf().setAppName("TransmitAlarm")
                .setMaster("local[2]")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        JavaSparkContext javaSparkContext = new JavaSparkContext(conf);
        JavaStreamingContext ssc = new JavaStreamingContext(javaSparkContext, Durations.seconds(30));
        Collection<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        //kafka相关参数，
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", brokers);
        kafkaParams.put("group.id", "transmitAlarm");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //Topic分区
        Map<TopicPartition, Long> offsets = new HashMap<>();
        JavaInputDStream<ConsumerRecord<String, String>> lines = KafkaUtils.createDirectStream(
                ssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaParams, offsets)
        );
        JavaDStream<String> dStream = lines.map((Function<ConsumerRecord<String, String>, String>) stringStringConsumerRecord -> stringStringConsumerRecord.value());


        dStream.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            WebService alarmMsgWebService = new WebService();
            @Override
            public void call(JavaRDD<String> stringJavaRDD) throws Exception {
                List<String> collect = stringJavaRDD.collect();
                System.out.println(collect);
            }
        });


        ssc.start();
        try {
            ssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ssc.close();*/
    }
}

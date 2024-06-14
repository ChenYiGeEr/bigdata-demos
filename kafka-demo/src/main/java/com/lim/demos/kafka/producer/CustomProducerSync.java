package com.lim.demos.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * CustomProducerSync
 * 自定义生产者-同步
 * @author lim
 * @version 1.0
 * @since 2023/7/14 11:45
 */
public class CustomProducerSync {

    /**
     * main
     * 主函数
     * @param args 参数
     * @return void
     * @since 2023/7/14 11:45
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 1. 创建配置对象 配置可从 nacos 中读取
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "Cluster-136:9092,Cluster-137:9092,Cluster-138:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 2. 创建kafka生产对象
        KafkaProducer<String, String> producer = new KafkaProducer(properties);

        // 3. 发送数据
        for (int i = 0; i < 10; i++) {
            final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                    "first",
                    "key" + i,
                    "value" + i
            );
            producer.send(producerRecord, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Topic：" + producerRecord.topic() + "\nKey：" + producerRecord.key() + "\nValue：" + producerRecord.value() + "\n分区：" + metadata.partition() + "\n偏移量：" + metadata.offset());
                    System.out.println("-----------------------------------");
                } else {
                    exception.printStackTrace();
                }
            }).get();
        }
        // 4. 关闭资源
        producer.close();
    }

}

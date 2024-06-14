package com.lim.demos.kafka.producer;

import com.lim.demos.kafka.producer.partitioner.CustomerPartitioner;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * CustomProducer
 * 自定义生产者-异步
 * @author lim
 * @version 1.0
 * @since 2023/7/14 11:45
 */
public class CustomProducer {

    /**
     * main
     * 主函数
     * @param args 参数
     * @return void
     * @since 2023/7/14 11:45
     */
    public static void main(String[] args) {

        // 1. 创建配置对象 配置可从 nacos 中读取
        Properties props = new Properties();

        /* 服务ip+port */
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "Cluster-136:9092,Cluster-137:9092,Cluster-138:9092");

        /* 序列化器 */
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /* 自定义分区器 */
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomerPartitioner.class.getName());

        /* 增加吞吐量 */
        // 批次大小 16kb
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16 * 1024);
        // 响应时间 ms
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 压缩方式
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        // 缓冲区大小 32mb
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 32 * 1024 * 1024);

        /* ack配置 */
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        /* 1.开启幂等性 */
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        /* 2.开启事务 实现精确一次 事务id赋值 */
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "customer-transactional-id");

        // 2. 创建kafka生产对象
        KafkaProducer<String, String> producer = new KafkaProducer(props);

        /* 初始化事务 */
        producer.initTransactions();

        // 3. 发送数据
        try {
            /* 开启事务 */
            producer.beginTransaction();
            for (int i = 0; i < 10000; i++) {
                final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                        "third",
                        "key" + i,
                        "Third，我是：value" + i
                );
                producer.send(producerRecord, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.println("Topic：" + producerRecord.topic() + "\nKey：" + producerRecord.key() + "\nValue：" + producerRecord.value() + "\n分区：" + metadata.partition() + "\n偏移量：" + metadata.offset());
                        System.out.println("-----------------------------------");
                    } else {
                        exception.printStackTrace();
                    }
                });
            }
            /* 提交事务 */
            producer.commitTransaction();
        } catch (KafkaException e) {
            /* 回滚事务 */
            producer.abortTransaction();
        } finally {
            // 4. 关闭资源
            producer.close();
        }
    }
}

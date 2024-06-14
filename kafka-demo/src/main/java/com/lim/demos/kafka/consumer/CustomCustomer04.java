package com.lim.demos.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CustomCustomer04
 * 自定义消费者
 * @author lim
 * @version 1.0
 * @since 2023/7/15 18:11
 */
public class CustomCustomer04 {

    public static void main(String[] args) {

        // 1. 创建配置对象，并对默认配置进行修改
        Properties props = new Properties();

        /* 服务ip+port */
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "Cluster-136:9092,Cluster-137:9092,Cluster-138:9092");
        /* key反序列化class */
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        /* value反序列化class */
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        /* 修改分区策略为粘性 */
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());
        /* groupID */
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group04");
        /* 设置consumer的offset自动提交以及提交间隔时间 */
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
        /* 指定offset进行消费 */
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 2. 创建kafka消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 3. 消费者订阅topic
        consumer.subscribe(Stream.of("first").collect(Collectors.toList()));
        // 4. 消费者拉取数据
        while (Boolean.TRUE){
            // 4.0 业务逻辑处理
            consumer.poll(Duration.ofMillis(100L)).forEach(record -> {
                System.out.println("Topic：" + record.topic() + "\nKey：" + record.key() + "\nValue：" + record.value() + "\n分区：" + record.partition() + "\n偏移量：" + record.offset());
                System.out.println("-----------------------------------");
            });
            // 4.1 处理完业务逻辑后同步提交consumer的offset
            consumer.commitSync();
            // 4.1 处理完业务逻辑后异步提交consumer的offset
            // consumer.commitAsync();
        }
    }
}

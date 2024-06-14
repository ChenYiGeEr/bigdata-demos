package com.lim.demos.kafka.producer.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * CustomerPartitioner
 * 自定义分区器
 * @author lim
 * @version 1.0
 * @since 2023/7/14 15:23
 */
public class CustomerPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if (topic == null) { throw new RuntimeException("topic cannot be null"); }
        if (key == null) { throw new RuntimeException("key cannot be null"); }
        // System.out.println("自定义分区器进行分区~~~~~~~~~~~~~~~~");
        // 1. 计算key的哈希值
        int hashCode = key.hashCode();
        // 2. 获取分区个数
        int partitionNum = cluster.partitionCountForTopic(topic);
        return Math.abs(hashCode) % partitionNum;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}

package com.lim.demos.flink.day02;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * FlinkKafkaSource
 * <p>flink程序从kafka读取文件内容</p>
 * <p>1. pom.xml中引入flink-connector-kafka的依赖jar</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/12 下午2:58
 */
public class FlinkKafkaSource {

    public static void main(String[] args) {
        // 0. 环境配置
        Map<String, String> configurationMap = new HashMap<>();
        configurationMap.put("rest.port", "10000");
        Configuration configuration = Configuration.fromMap(configurationMap);
        // 1. 根据配置获取flink运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        // 1.1 设置并行度为1
        env.setParallelism(1);
        // 2. flink程序从kafka中读取数据
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "hadoop102:9092,hadoop103:9092,hadoop104:9092");
        properties.setProperty("group.id", "test");
        properties.setProperty("auto.offset.reset", "latest");

        env.addSource(new FlinkKafkaConsumer<>("testTopic", new SimpleStringSchema(), properties))
            .print();
        try {
            // 3. 执行flink程序
            env.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

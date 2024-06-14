package com.lim.demos.flink.day02;

import com.lim.demos.flink.day02.entity.WaterSensor;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;

/**
 * FlinkCollectionSource
 * <p>flink从java集合中读取内容</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/12 下午2:55
 */
public class FlinkCollectionSource {

    public static void main(String[] args) throws Exception {
        List<WaterSensor> waterSensors = Arrays.asList(
                new WaterSensor("ws_001", 1577844001L, 45),
                new WaterSensor("ws_002", 1577844015L, 43),
                new WaterSensor("ws_003", 1577844020L, 42));

        // 1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.fromCollection(waterSensors).print();
        env.execute();
    }

}

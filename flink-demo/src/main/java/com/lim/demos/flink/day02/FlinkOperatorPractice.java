package com.lim.demos.flink.day02;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * FlinkOperatorPractice
 * <p>flink算子练习</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/12 下午4:03
 */
public class FlinkOperatorPractice {

    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromElements(1, 2, 3, 4, 5)
            .map(value -> value * value)
            .print();

        env.execute();
    }

}

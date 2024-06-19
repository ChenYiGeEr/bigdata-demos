package com.lim.demos.flink.day02;

import org.apache.flink.api.java.functions.KeySelector;
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
            .keyBy((KeySelector<Integer, String>) element -> element %2 == 0 ? "偶数" : "奇数")
            .print();

        env.execute();
        System.out.println(1<<7);
    }

}

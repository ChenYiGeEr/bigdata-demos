package com.lim.demos.flink.day01;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * FlinkStreamingWordCount2
 * <p>flink流处理无界流文本 简单的单词统计</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/7 下午4:01
 */
public class FlinkStreamingWordCount2 {

    public static void main(String[] args) throws Exception {

        // 1. 获取流处理的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 1.1 设置并行度
        env.setParallelism(1);

        // 2. 读取数据源 hadoop102:9999 命令行执行 nc -lk 9999
        DataStreamSource<String> dataStreamSource = env.socketTextStream("hadoop102", 9999);

        // 3. 处理
        dataStreamSource
            .flatMap((FlatMapFunction<String, Tuple2<String, Long>>) (words, collector) -> {
                for (String word : words.split(" ")) {
                    collector.collect(Tuple2.of(word, 1L));
                }
            })
            // 解决泛型擦除问题
            .returns(Types.TUPLE(Types.STRING, Types.LONG))
            .keyBy(tuple -> tuple.f0)
            .sum(1)
            .print();

        // 4. 执行
        env.execute();
    }

}

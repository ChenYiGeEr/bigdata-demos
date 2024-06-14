package com.lim.demos.flink.day01;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;


/**
 * FlinkStreamingWordCount
 * <p>flink流处理有界流文本 简单的单词统计</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/7 下午3:27
 */
public class FlinkStreamingWordCount {

    public static void main(String[] args) throws Exception {
        // 1. 获取flink流处理的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 1.1 设置并行度
        env.setParallelism(1);
        // 2. 读取text文件中的数据
        DataStreamSource<String> dataStreamSource = env.readTextFile("input/words.txt");

        // 3. 切分映射 根据word分组后对Long进行求和
        dataStreamSource
            .flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                @Override
                public void flatMap(String words, Collector<Tuple2<String, Long>> collector) throws Exception {
                    for (String word : words.split(" ")) {
                        collector.collect(Tuple2.of(word, 1L));
                    }
                }
            })
            .keyBy(new KeySelector<Tuple2<String, Long>, String>() {
                @Override
                public String getKey(Tuple2<String, Long> tuple2) throws Exception {
                    return tuple2.getField(0);
                }
            })
            .sum(1)
            .print();

        // 4. 执行
        env.execute();
    }

}

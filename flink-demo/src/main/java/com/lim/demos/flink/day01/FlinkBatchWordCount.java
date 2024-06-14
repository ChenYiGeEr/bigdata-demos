package com.lim.demos.flink.day01;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;


/**
 * FlinkBatchWordCount
 * <p>flink批处理 简单的单词统计</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/7 下午3:27
 */
public class FlinkBatchWordCount {

    public static void main(String[] args) throws Exception {
        // 1. 获取flink批处理的执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 2. 读取text文件中的数据
        DataSource<String> dataSource = env.readTextFile("input/words.txt");

        // 3. 切分映射 根据word分组后对Long进行求和
        dataSource
            .flatMap(new FlatMapFunction<String, String>() {
                @Override
                public void flatMap(String words, Collector<String> collector) throws Exception {
                    for (String word : words.split(" ")) {
                        collector.collect(word);
                    }
                }
            })
            .map(new MapFunction<String, Tuple2<String, Long>>() {
                @Override
                public Tuple2<String, Long> map(String word) throws Exception {
                    return Tuple2.of(word, 1L);
                }
            })
            .groupBy(0)
            .sum(1)
            .print();

    }

}

package com.lim.demos.mapreduce.partition;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * WordCountPartition
 * <p>单词数量分区器</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/24 14:54
 */
public class WordCountPartitioner extends Partitioner<Text, LongWritable> {

    @Override
    public int getPartition(Text key, LongWritable value, int i) {
        /* 当key开头首字母在a到l或A到L之间放入0号分区，其余放到1号分区 */
        if ((key.charAt(0) >= 'a' && key.charAt(0) <= 'l') || (key.charAt(0) >= 'A' && key.charAt(0) <= 'L')) {
            return 0;
        }
        return 1;
    }
}

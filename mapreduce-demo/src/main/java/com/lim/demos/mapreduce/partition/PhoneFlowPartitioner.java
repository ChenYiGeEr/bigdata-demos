package com.lim.demos.mapreduce.partition;

import com.lim.demos.mapreduce.writable.FlowBeanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * PhoneFlowPartitioner
 * <p>自定义手机流量分区器</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/24 15:06
 */
public class PhoneFlowPartitioner extends Partitioner<Text, FlowBeanWritable> {
    @Override
    public int getPartition(Text key, FlowBeanWritable value, int i) {
        // 13开头的手机号放到0号分区中，其他的放到1号分区中
        return key.toString().startsWith("13") ? 0: 1;
    }
}

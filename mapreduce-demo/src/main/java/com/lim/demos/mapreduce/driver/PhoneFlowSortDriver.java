package com.lim.demos.mapreduce.driver;

import com.lim.demos.mapreduce.mapper.PhoneFlowSortMapper;
import com.lim.demos.mapreduce.partition.PhoneFlowPartitioner;
import com.lim.demos.mapreduce.writable.FlowBeanWritable;
import com.lim.demos.mapreduce.reducer.PhoneFlowSortReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * PhoneFlowDriver
 * <p>手机流量job驱动，程序入口方法</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/24 10:52
 */
public class PhoneFlowSortDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 0.创建job所属的配置对象
        Configuration conf = new Configuration();
        // 1.创建job示例
        Job job = Job.getInstance(conf);
        // 2.给job示例赋值
        // 2.0 设置reducerTask的个数以及自定义分区器
        job.setNumReduceTasks(5);
        job.setPartitionerClass(PhoneFlowPartitioner.class);
        // 2.1 关联本程序的jar-如果是本地可以不写在集群上必须写
        job.setJarByClass(PhoneFlowSortDriver.class);
        // 2.2 设置mapper和reducer类
        job.setMapperClass(PhoneFlowSortMapper.class);
        job.setReducerClass(PhoneFlowSortReducer.class);
        // 2.3 设置mapper输出的key和value类型
        job.setMapOutputKeyClass(FlowBeanWritable.class);
        job.setMapOutputValueClass(Text.class);
        // 2.4 设置reducer输出的key和value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBeanWritable.class);
        // 2.5 设置输入和输出的路径
        FileInputFormat.setInputPaths(job, new Path("/Users/lim/代码/mine/mapreduce-demo/input2"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/lim/代码/mine/mapreduce-demo/output_sort"));
        // 3. 运行job
        boolean isSuccess = job.waitForCompletion(true);
        System.out.println(isSuccess ? "😁操作成功！" : "😭操作失败！");
    }

}

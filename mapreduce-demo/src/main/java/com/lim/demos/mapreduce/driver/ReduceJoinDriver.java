package com.lim.demos.mapreduce.driver;

import com.lim.demos.mapreduce.comparator.OrderReduceJoinGroupComparator;
import com.lim.demos.mapreduce.writable.OrderBeanWritable;
import com.lim.demos.mapreduce.mapper.ReduceJoinMapper;
import com.lim.demos.mapreduce.reducer.ReduceJoinReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * ReduceJoinDriver
 * @author lim
 * @version 1.0
 * @since 2023/7/25 10:29
 */
public class ReduceJoinDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);

        // 设置分组
        job.setGroupingComparatorClass(OrderReduceJoinGroupComparator.class);

        // 2. job对象赋值
//        job.setJobName("ReduceJoinDriver");
        // 2.1 设置启动jar、mapper、reducer的类
        job.setJarByClass(ReduceJoinDriver.class);
        job.setMapperClass(ReduceJoinMapper.class);
        job.setReducerClass(ReduceJoinReducer.class);
        // 设置mapper的key、value输出类型
        job.setMapOutputKeyClass(OrderBeanWritable.class);
        job.setMapOutputValueClass(NullWritable.class);
        // 设置mapper的key、value输出类型
        job.setOutputKeyClass(OrderBeanWritable.class);
        job.setOutputValueClass(NullWritable.class);
        // 设置输入的路径
        FileInputFormat.setInputPaths(job,  new Path("/Users/lim/代码/mine/mapreduce-demo/reduce_join_input"));
        // 设置输出的路径
        FileOutputFormat.setOutputPath(job,  new Path("/Users/lim/代码/mine/mapreduce-demo/reduce_join_output"));

        // 3. 运行job
        boolean isSuccess = job.waitForCompletion(true);
        System.out.println(isSuccess ? "😁操作成功！" : "😭操作失败！");
    }

}

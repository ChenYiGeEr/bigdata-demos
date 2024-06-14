package com.lim.demos.mapreduce.driver;

import com.lim.demos.mapreduce.combiner.WordCountCombiner;
import com.lim.demos.mapreduce.mapper.WordCountMapper;
import com.lim.demos.mapreduce.partition.WordCountPartitioner;
import com.lim.demos.mapreduce.reducer.WordCountReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * WordCountDriver
 * <p>本机执行job</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/21 18:22
 */
public class WordCountDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 0. 创建配置对象
        Configuration config = new Configuration();
        // 1. 创建mapreduce的job示例
        Job job = Job.getInstance(config);
        // 设置使用combiner
        job.setCombinerClass(WordCountCombiner.class);
        // 2. 给job赋值
        // 2.0 设置reduceTask个数
        job.setNumReduceTasks(2);
        // 2.0.1 设置自定义分区器
        job.setPartitionerClass(WordCountPartitioner.class);
        // 2.1 jobName
        job.setJobName("WordCount");
        // 2.2 启用类，入口类
        job.setJarByClass(WordCountDriver.class);
        // 2.3 mapper的类
        job.setMapperClass(WordCountMapper.class);
        // 2.3.1 设置mapper输出的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        // 2.4 reducer的类
        job.setReducerClass(WordCountReducer.class);
        // 2.4.1 设置最终输出的key、value的类型
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        // 2.5 设置输入和输出的路径
        FileInputFormat.setInputPaths(job, new Path("/Users/lim/代码/mine/mapreduce-demo/input"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/lim/代码/mine/mapreduce-demo/output"));
        // 3. 运行job
        boolean isSuccess = job.waitForCompletion(true);
        System.out.println(isSuccess ? "😁操作成功！" : "😭操作失败！");
    }

}

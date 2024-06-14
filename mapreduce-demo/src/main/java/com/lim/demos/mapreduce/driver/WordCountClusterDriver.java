package com.lim.demos.mapreduce.driver;

import com.lim.demos.mapreduce.mapper.WordCountMapper;
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
 * WordCountClusterDriver
 * <p>maven:package将jar扔到集群上运行job</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/21 18:22
 */
public class WordCountClusterDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 0. 创建配置对象
        Configuration config = new Configuration();
        // 1. 创建mapreduce的job示例
        Job jobInstance = Job.getInstance(config);
        // 2. 给job赋值
        // 2.1 jobName
        jobInstance.setJobName("WordCount");
        // 2.2 启用类，入口类
        jobInstance.setJarByClass(WordCountClusterDriver.class);
        // 2.3 mapper的类
        jobInstance.setMapperClass(WordCountMapper.class);
        // 2.3.1 设置mapper输出的key、value类型
        jobInstance.setMapOutputKeyClass(Text.class);
        jobInstance.setMapOutputValueClass(LongWritable.class);
        // 2.4 reducer的类
        jobInstance.setReducerClass(WordCountReducer.class);
        // 2.4.1 设置最终输出的key、value的类型
        jobInstance.setReducerClass(WordCountReducer.class);
        jobInstance.setOutputKeyClass(Text.class);
        jobInstance.setOutputValueClass(LongWritable.class);

        // 2.5 设置输入和输出的路径
        FileInputFormat.setInputPaths(jobInstance, new Path(args[0]));
        FileOutputFormat.setOutputPath(jobInstance, new Path(args[1]));

        // 3. 运行job
        boolean isSuccess = jobInstance.waitForCompletion(true);
        System.out.println(isSuccess ? "😁操作成功！" : "😭操作失败！");
    }

}

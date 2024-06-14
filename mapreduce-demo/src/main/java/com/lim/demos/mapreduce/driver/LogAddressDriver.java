package com.lim.demos.mapreduce.driver;

import com.lim.demos.mapreduce.mapper.LogAddressMapper;
import com.lim.demos.mapreduce.outputformat.LogAddressOutputFormat;
import com.lim.demos.mapreduce.reducer.LogAddressReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * LogAddressDriver
 * @author lim
 * @version 1.0
 * @since 2023/7/24 21:25
 */
public class LogAddressDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 0. 创建 创建job所需的config对象
        Configuration config = new Configuration();
        // 1. 创建job对象
        Job job = Job.getInstance(config);
        // 2. job对象赋值
        job.setJobName("LogAddressDriver");
        // 设置使用自定义的OutputFormat
        job.setOutputFormatClass(LogAddressOutputFormat.class);
        // 2.1 设置启动jar、mapper、reducer的类
        job.setJarByClass(LogAddressDriver.class);
        job.setMapperClass(LogAddressMapper.class);
        job.setReducerClass(LogAddressReducer.class);
        // 设置mapper的key、value输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        // 设置mapper的key、value输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        // 设置输入的路径
        FileInputFormat.setInputPaths(job,  new Path("/Users/lim/代码/mine/mapreduce-demo/log_input"));
        // 设置输出的路径
        FileOutputFormat.setOutputPath(job,  new Path("/Users/lim/代码/mine/mapreduce-demo/log_output"));

        // 3. 运行job
        boolean isSuccess = job.waitForCompletion(true);
        System.out.println(isSuccess ? "😁操作成功！" : "😭操作失败！");
    }

}

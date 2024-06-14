package com.lim.demos.mapreduce.driver;

import com.lim.demos.mapreduce.mapper.WordCountMapper;
import com.lim.demos.mapreduce.reducer.WordCountReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * WordCountCombineDriver
 * <p>使用CombineTextInoutFormat统计多个文件单词出现的次数</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/24 14:08
 */
public class WordCountCombineDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 0.创建job实例所需配置
        Configuration config = new Configuration();
        // 1.创建job实例
        Job job = Job.getInstance(config);
        // 2.设置job实例
        job.setJobName("WordCountCombineDriver");
        // 2.1 设置启用jarClass，本机执行可不指定，集群执行时需执行
        job.setJarByClass(WordCountCombineDriver.class);
        // 2.2 设置mapper、reducer执行
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        // 2.3 设置mapper输出的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        // 2.4 设置最终输出的key、value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        // 2.6 设置CombineTextInputFormat的虚拟存储分片大小为4MiB
        CombineTextInputFormat.setMaxInputSplitSize(job, 4 * 1024 * 1024);
        // 2.7 设置使用CombineTextInputFormat
        job.setInputFormatClass(CombineTextInputFormat.class);
        // 2.5 指定输入和输出的文件路径
        FileInputFormat.setInputPaths(job, new Path("/Users/lim/代码/mine/mapreduce-demo/combine_input"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/lim/代码/mine/mapreduce-demo/combine_output"));
        // 3.执行job实例
        boolean isSuccess = job.waitForCompletion(true);
        System.out.println(isSuccess ? "😁操作成功！" : "😭操作失败！");
    }

}

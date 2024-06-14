package com.lim.demos.mapreduce.driver;

import com.lim.demos.mapreduce.mapper.MapJoinMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * MapJoinDriver
 * <p></p>
 * @author lim
 * @version 1.0
 * @ClassName
 * @Description
 * @since 2023/7/24 22:11
 */
public class MapJoinDriver {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, ClassNotFoundException {
        // 0. 创建 创建job所需的config对象
        Configuration config = new Configuration();
        // 1. 创建job对象
        Job job = Job.getInstance(config);
        job.addCacheFile(new URI("file:///Users/lim/代码/mine/mapreduce-demo/map_join_input/pd.txt"));
        // 2. job对象赋值
        job.setJobName("MapJoinDriver");
        // 2.1 设置启动jar、mapper、reducer的类
        job.setJarByClass(MapJoinDriver.class);
        job.setMapperClass(MapJoinMapper.class);
        // 如果没有reducer可以将reducerTask的个数设置为0，将不再对key进行排序
        job.setNumReduceTasks(0);
        // 设置mapper的key、value输出类型
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);
        // 设置mapper的key、value输出类型
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);
        // 设置输入的路径
        FileInputFormat.setInputPaths(job,  new Path("/Users/lim/代码/mine/mapreduce-demo/map_join_input/order.txt"));
        // 设置输出的路径
        FileOutputFormat.setOutputPath(job,  new Path("/Users/lim/代码/mine/mapreduce-demo/map_join_output"));

        // 3. 运行job
        boolean isSuccess = job.waitForCompletion(true);
        System.out.println(isSuccess ? "😁操作成功！" : "😭操作失败！");
    }

}

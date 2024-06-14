package com.lim.demos.mapreduce.mapper;

import com.lim.demos.mapreduce.writable.OrderBeanWritable;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * ReduceJoinMapper
 *
 * @author lim
 * @version 1.0
 * @since 2023/7/25 10:29
 */
public class ReduceJoinMapper extends Mapper<LongWritable, Text, OrderBeanWritable, NullWritable> {

    private OrderBeanWritable orderBeanWritable = new OrderBeanWritable();

    private String fileName;

    @Override
    protected void setup(Mapper<LongWritable, Text, OrderBeanWritable, NullWritable>.Context context) throws IOException, InterruptedException {
        // 获取切片信息
        InputSplit inputSplit = context.getInputSplit();
        // 通过上下文获取map处理的文件名
        fileName = ((FileSplit) inputSplit).getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, OrderBeanWritable, NullWritable>.Context context) throws IOException, InterruptedException {
        // 1.切割数据
        String[] split = value.toString().split("\t");
        // 2.判断文件名从而封装key value
        if ("order.txt".equals(fileName)) {
            orderBeanWritable.setId(Long.parseLong(split[0]));
            orderBeanWritable.setPid(Long.parseLong(split[1]));
            orderBeanWritable.setAmount(Long.parseLong(split[2]));
            orderBeanWritable.setPname(StringUtils.EMPTY);
        } else if ("pd.txt".equals(fileName)) {
            orderBeanWritable.setPid(Long.parseLong(split[0]));
            orderBeanWritable.setPname(split[1]);
        }
        // 3. 写出key value
        context.write(orderBeanWritable, NullWritable.get());
    }
}

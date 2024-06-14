package com.lim.demos.mapreduce.mapper;

import com.lim.demos.mapreduce.writable.FlowBeanWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author lim
 * @version 1.0
 * @ClassName PhoneFlowSortMapper
 * @Description
 * @since 2023/7/24 15:40
 */
public class PhoneFlowSortMapper extends Mapper<LongWritable, Text, FlowBeanWritable, Text> {

    private FlowBeanWritable outKey = new FlowBeanWritable();

    private Text outValue = new Text();

    /**
     * 方法：map
     * <p>覆盖父类的map方法，在map方法中实现需要在MapTask中实现的业务逻辑代码 </p>
     * <p>该方法在被循环调用，每调用一次传入一行数据 </p>
     *
     * @param key 读取数据时的偏移量
     * @param value 读取的一行一行的数据
     * @param context 上下文 在这将key、value写出去
     * @throws IOException
     * @throws InterruptedException
     * @since 2023/7/21 17:03
     * @author lim
     * @version 1.0
     */
    @Override
    protected void map(LongWritable key,
                       Text value,
                       Mapper<LongWritable, Text, FlowBeanWritable, Text>.Context context) throws IOException, InterruptedException {
        // 1. 将数据切割
        String[] phoneFlowInfo = value.toString().split("\t");
        // 2. 封装key value
        outKey.setUpFlow(Long.parseLong(phoneFlowInfo[phoneFlowInfo.length - 3]));
        outKey.setDownFlow(Long.parseLong(phoneFlowInfo[phoneFlowInfo.length - 2]));
        outKey.setSumFlow(outKey.getUpFlow() + outKey.getDownFlow());
        outValue.set(phoneFlowInfo[1]);
        // 3.将key value写出去
        context.write(outKey, outValue);
    }


}

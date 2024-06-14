package com.lim.demos.mapreduce.mapper;

import com.lim.demos.mapreduce.writable.FlowBeanWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * PhoneFlowMapper
 * <p>手机流量mapper</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/24 10:50
 * KEYIN    LongWritable     读取数据的偏移量的类型
 * VALUEIN  Text             读取数据的类型
 * KEYOUT   Text             输出数据的偏移量的类型
 * VALUEOUT FlowBeanWritable 输出数据的类型
 */
public class PhoneFlowMapper extends Mapper<LongWritable, Text,
                                            Text, FlowBeanWritable> {

    private Text outKey = new Text();

    private FlowBeanWritable outValue = new FlowBeanWritable();

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
                       Mapper<LongWritable, Text, Text, FlowBeanWritable>.Context context) throws IOException, InterruptedException {
        // 1. 将数据切割
        String[] phoneFlowInfo = value.toString().split("\t");
        // 2. 封装key value
        outKey.set(phoneFlowInfo[1]);
        outValue.setUpFlow(Long.parseLong(phoneFlowInfo[phoneFlowInfo.length - 3]));
        outValue.setDownFlow(Long.parseLong(phoneFlowInfo[phoneFlowInfo.length - 2]));
        outValue.setSumFlow(outValue.getUpFlow() + outValue.getDownFlow());
        // 3.将key value写出去
        context.write(outKey, outValue);
    }
}

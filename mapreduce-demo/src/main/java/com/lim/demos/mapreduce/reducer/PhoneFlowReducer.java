package com.lim.demos.mapreduce.reducer;

import com.lim.demos.mapreduce.writable.FlowBeanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * PhoneFlowReducer
 * <p>手机流量reducer</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/24 10:51
 * KEYIN    Text                读取的KEY的类型(MapTask写出的key的数据类型)
 * VALUEIN  FlowBeanWritable    读取数据的类型(MapTask写出的value的数据类型)
 * KEYOUT   Text                写出的key的数据类型（在这儿是单词的数据类型）
 * VALUEOUT FlowBeanWritable    写出的value的数据类型（在这儿是FlowBeanWritable的数据类型）
 */
public class PhoneFlowReducer extends Reducer<Text, FlowBeanWritable, Text, FlowBeanWritable> {

    private FlowBeanWritable outValue = new FlowBeanWritable();

    /**
     * 方法：reduce
     * <p>1. 在reduce方法中实现需要在ReduceTask中实现的业务逻辑代码</p>
     * <p>2. reduce方法再被循环调用，每调用一次输出一组数据（在这儿key值相同为一组）</p>
     *
     * @param key 读取的key
     * @param values 读取所有的value值
     * @param context 上下文 在这将key、value写出去
     * @throws IOException
     * @throws InterruptedException
     * @exception
     * @since 2023/7/21 18:14
     * @author lim
     * @version 1.0
     */
    @Override
    protected void reduce(Text key, Iterable<FlowBeanWritable> values, Reducer<Text, FlowBeanWritable, Text, FlowBeanWritable>.Context context) throws IOException, InterruptedException {
        long sumUpFlow = 0,
             sumDownFlow = 0;
        for (FlowBeanWritable value : values) {
            sumUpFlow += value.getUpFlow();
            sumDownFlow += value.getDownFlow();
        }
        outValue.setUpFlow(sumUpFlow);
        outValue.setDownFlow(sumDownFlow);
        outValue.setSumFlow(outValue.getUpFlow() + outValue.getDownFlow());
        // 3. 将key，value写出去
        context.write(key, outValue);
    }

}

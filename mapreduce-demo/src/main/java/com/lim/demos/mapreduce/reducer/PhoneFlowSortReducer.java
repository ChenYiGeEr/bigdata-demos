package com.lim.demos.mapreduce.reducer;

import com.lim.demos.mapreduce.writable.FlowBeanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author lim
 * @version 1.0
 * @ClassName PhoneFlowSortReducer
 * @Description
 * @since 2023/7/24 15:42
 */
public class PhoneFlowSortReducer extends Reducer<FlowBeanWritable, Text, Text, FlowBeanWritable> {

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
    protected void reduce(FlowBeanWritable key, Iterable<Text> values, Reducer<FlowBeanWritable, Text, Text, FlowBeanWritable>.Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            // 1.将key value写出去
            context.write(value, key);
        }
    }
}

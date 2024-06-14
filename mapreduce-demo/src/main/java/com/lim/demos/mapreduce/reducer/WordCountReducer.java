package com.lim.demos.mapreduce.reducer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * WordCountReducer
 * @author lim
 * @version 1.0
 * @since 2023/7/21 16:57
 * KEYIN    Text            读取的KEY的类型(MapTask写出的key的数据类型)
 * VALUEIN  LongWritable    读取数据的类型(MapTask写出的value的数据类型)
 * KEYOUT   Text            写出的key的数据类型（在这儿是单词的数据类型）
 * VALUEOUT LongWritable    写出的value的数据类型（在这儿是单词个数的数据类型）
 */
public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    private LongWritable outValue = new LongWritable();

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
    protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
        // 0. 定义局部变量count
        long count = 0;
        // 1. 遍历所有value
        for (LongWritable value : values) {
            // 1.1. 对value个数进行累加
            count += value.get();
        }
        // 3. 封装key，value
        outValue.set(count);
        // 4. 将key，value写出去
        context.write(key, outValue);
    }


}

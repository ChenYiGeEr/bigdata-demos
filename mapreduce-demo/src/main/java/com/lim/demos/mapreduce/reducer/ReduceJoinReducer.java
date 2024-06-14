package com.lim.demos.mapreduce.reducer;

import com.lim.demos.mapreduce.writable.OrderBeanWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * ReduceJoinReducer
 *
 * @author lim
 * @version 1.0
 * @since 2023/7/25 10:29
 */
public class ReduceJoinReducer extends Reducer<OrderBeanWritable, NullWritable, OrderBeanWritable, NullWritable> {

    @Override
    protected void reduce(OrderBeanWritable key, Iterable<NullWritable> values, Reducer<OrderBeanWritable, NullWritable, OrderBeanWritable, NullWritable>.Context context) throws IOException, InterruptedException {
        // 1. 获取第一组数据的key的pname
        Iterator<NullWritable> iterator = values.iterator();
        // 指针下移（获取第一条数据）
        NullWritable next = iterator.next();
        String pName = key.getPname();
        while (iterator.hasNext()) {
            iterator.next();
            key.setPname(pName);
            context.write(key, NullWritable.get());
        }
    }

}

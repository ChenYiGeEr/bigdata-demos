package com.lim.demos.mapreduce.comparator;

import com.lim.demos.mapreduce.writable.OrderBeanWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * OrderReduceJoinGroupComparator
 * <p>自定义组合排序器</p>
 *
 * @author lim
 * @version 1.0
 * @since 2023/7/25 11:18
 */
public class OrderReduceJoinGroupComparator extends WritableComparator {

    /**
     * 方法：OrderReduceJoinGroupComparator
     * <p>调用父类的构造器 </p>
     * <p>WritableComparator </p>
     * <p>keyClass：key的运行时类</p>
     * <p>createInstances：是否创建实例（必须为true） </p>
     * @since 2023/7/25 11:20
     * @author lim
     */
    public OrderReduceJoinGroupComparator() {
        super(OrderBeanWritable.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBeanWritable o1 = (OrderBeanWritable) a;
        OrderBeanWritable o2 = (OrderBeanWritable) b;
        return Long.compare(o1.getPid(), o2.getPid());
    }
}

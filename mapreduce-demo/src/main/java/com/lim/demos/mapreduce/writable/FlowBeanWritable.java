package com.lim.demos.mapreduce.writable;

import com.sun.istack.internal.NotNull;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * FlowBeanWritable
 * <p>自定义对象序列化器</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/24 10:34
 */
public class FlowBeanWritable implements WritableComparable<FlowBeanWritable> {

    /** 上行流量 */
    private long upFlow;

    /** 下行流量 */
    private long downFlow;

    /** 上行流量 + 下行流量 */
    private long sumFlow;

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }

    /* 无参构造 */
    public FlowBeanWritable () {}

    /**
     * 方法：compareTo
     * <p>继承Comparable类时需Override的方法 </p>
     * <p>按照总流量进行排序 </p>
     *
     * @param  flowBean 另一个FlowBeanWritable对象
     * @return int 返回大小 0：相等，1：大于，-1：小于
     * @since 2023/7/24 10:41
     * @author lim
     */
    @Override
    public int compareTo(@NotNull FlowBeanWritable flowBean) {
        return Long.compare(flowBean.getSumFlow(), this.sumFlow);
    }

    /**
     * 方法：write
     * <p>序列化时调用此方法 </p>
     *
     * @param dataOutput
     * @exception IOException
     * @since 2023/7/24 10:36
     * @author lim
     */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(downFlow);
        dataOutput.writeLong(sumFlow);
    }

    /**
     * 方法：readFields
     * <p>反序列化时调用此方法 </p>
     *
     * @param dataInput
     * @exception IOException
     * @since 2023/7/24 10:39
     * @author lim
     * @version 1.0
     */
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        // 必须要求和序列化方法write的顺序一致
        upFlow = dataInput.readLong();
        downFlow = dataInput.readLong();
        sumFlow = dataInput.readLong();
    }

    /**
     * 方法：toString
     * <p>重写toString方法 </p>
     *
     * @return java.lang.String
     * @since 2023/7/24 10:42
     * @version 1.0
     */
    @Override
    public String toString () {
        return upFlow + "\t" + downFlow + "\t" + sumFlow;
    }
}

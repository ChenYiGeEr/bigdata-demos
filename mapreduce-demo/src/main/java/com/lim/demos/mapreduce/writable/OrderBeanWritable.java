package com.lim.demos.mapreduce.writable;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * OrderBeanWritable
 * @author lim
 * @version 1.0
 * @since 2023/7/25 10:25
 */
public class OrderBeanWritable implements WritableComparable<OrderBeanWritable> {

    /** order_id */
    private long id;

    /** product_id */
    private long pid;

    /** product_name */
    private String pname;

    /** product_amount */
    private long amount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public OrderBeanWritable () {}

    /** 序列化写出 */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(id);
        dataOutput.writeLong(pid);
        dataOutput.writeLong(amount);
        dataOutput.writeUTF(pname);
    }

    /** 反序列化读入 */
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        id = dataInput.readLong();
        pid = dataInput.readLong();
        amount = dataInput.readLong();
        pname = dataInput.readUTF();
    }

    /* 自定义compareTo方法 */
    @Override
    public int compareTo(OrderBeanWritable orderBean) {
        // 先按照pid进行升序排序，再按照进行pName升序排序
        int pidSortResult = Long.compare(this.getPid(), orderBean.getPid());
        if (pidSortResult == 0) {
            return orderBean.getPname().compareTo(getPname());
        }
        return pidSortResult;
    }

    @Override
    public String toString() {
        return getId() + "\t" + getPid() + "\t" + getPname() + "\t" + getAmount();
    }
}

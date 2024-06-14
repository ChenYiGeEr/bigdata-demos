package com.lim.demos.hive.udaf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.IntWritable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Max
 * <p>自定义max方法返回数组最小值</p>
 * @author lim
 * @version 1.0
 * @since 2023/8/1 10:25
 */
public class MaxFunction extends GenericUDF {
    /**
     * 初始化
     * <p>检查数据个数类型 并返回函数返回值的类型的检查器</p>
     * @param arguments 检查器
     * @return 返回函数的返回值的类型检查器
     * @throws UDFArgumentException
     */
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        // 1.判断参数个数
        if (arguments == null || arguments.length == 0) {
            throw new UDFArgumentLengthException("Arguments length must > 0");
        }
        // 2.判断数据类型
        for (int i = 0; i < arguments.length; i++) {
            if (!ObjectInspector.Category.PRIMITIVE.equals(arguments[i].getCategory())) {
                throw new UDFArgumentLengthException("Arguments[" + (i + 1) + "] type must be primitive");
            }
        }
        // 3.返回函数的返回值的类型检查器
        return PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    }

    /**
     * 计算
     * <p>函数的核心代码 获取最大值</p>
     * @param arguments 请求参数
     * @return
     * @throws HiveException
     */
    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        List<IntWritable> result = new ArrayList<>(arguments.length);
        for (DeferredObject argument : arguments) {
            result.add((IntWritable) argument.get());
        }
        // 对集合数组进行升序排序
        result = result.stream().sorted((o1, o2) -> Integer.min(o1.get(), o2.get())).collect(Collectors.toList());
        // 返回集合最后一位
        return result.get(result.size() - 1).get();
    }

    @Override
    public String getDisplayString(String[] strings) {
        return "customer_max";
    }
}

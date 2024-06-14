package com.lim.demos.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.Objects;

/**
 * Length
 * 返回字段的长度
 * @author lim
 * @version 1.0
 * @since 2023/7/31 17:45
 */
public class LengthFunction extends GenericUDF {

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
        if (arguments == null || arguments.length != 1) {
            throw new UDFArgumentLengthException("Arguments length must be 1");
        }
        // 2.判断数据类型
        if (!ObjectInspector.Category.PRIMITIVE.equals(arguments[0].getCategory())) {
            throw new UDFArgumentTypeException(0, "Arguments type must be primitive");
        }
        // 3.返回函数的返回值的类型检查器
        return PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    }

    /**
     * 计算
     * <p>函数的核心代码 计算长度</p>
     * @param arguments 请求参数
     * @return
     * @throws HiveException
     */
    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        return Objects.isNull(arguments[0]) ? 0 : arguments[0].get().toString().length();
    }


    @Override
    public String getDisplayString(String[] strings) {
        return "customer_length";
    }

}

package com.lim.demos.hive.udtf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * ExplodeJSONArray
 * <p>爆炸函数</p>
 * @author lim
 * @version 1.0
 * @since 2023/8/18 19:38
 */
public class ExplodeJSONArray extends GenericUDTF {

    private PrimitiveObjectInspector inputOI;

    @Override
    public void close() throws HiveException {

    }

    @Override
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {

        // 1 参数合法性检查
        if(argOIs.length!=1){
            throw new UDFArgumentException("explode_json_array函数只能接收1个参数");
        }

        ObjectInspector argOI = argOIs[0];

        // 2 第一个参数必须为string
        //判断参数是否为基础数据类型
        if(argOI.getCategory()!=ObjectInspector.Category.PRIMITIVE){
            throw new UDFArgumentException("explode_json_array函数只能接收基本数据类型的参数");
        }

        //将参数对象检查器强转为基础类型对象检查器
        PrimitiveObjectInspector primitiveOI  = (PrimitiveObjectInspector) argOI;
        inputOI=primitiveOI;

        //判断参数是否为String类型
        if(primitiveOI.getPrimitiveCategory()!=PrimitiveObjectInspector.PrimitiveCategory.STRING){
            throw new UDFArgumentException("explode_json_array函数只能接收STRING类型的参数");
        }

        // 3 定义返回值名称和类型
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        fieldNames.add("item");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames,
                fieldOIs);
    }

    @Override
    public void process(Object[] args) throws HiveException {

        // 1 获取传入的数据
        Object arg = args[0];
        String jsonArrayStr = PrimitiveObjectInspectorUtils.getString(arg, inputOI);

        // 2 将string转换为json数组
        JSONArray jsonArray = new JSONArray(jsonArrayStr);

        // 3 循环一次，取出数组中的一个json，并写出
        for (int i = 0; i < jsonArray.length(); i++) {
            String json = jsonArray.getString(i);

            String[] result = {json};

            forward(result);
        }
    }
}
package com.lim.demos.flink.day02;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * FlinkHdfsSource
 * <p>flink程序从hdfs读取文件内容</p>
 * <p>1. pom.xml中引入hadoop-client的依赖jar</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/12 下午2:42
 */
public class FlinkHdfsSource {

    public static void main(String[] args) {

        // 0. 构建配置信息
        Map<String, String> configurationMap = new HashMap<>();
        configurationMap.put("rest.port", "10000");
        Configuration configuration = Configuration.fromMap(configurationMap);

        // 1. 根据配置信息 获取flink执行环境
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment(configuration);

        // 2. 读取hdfs文件内容并打印输出
        streamExecutionEnvironment
                .readTextFile("hdfs://hadoop102:8020/user/hive/warehouse/practice.db/account/account.txt")
                .print();

        // 3. 执行flink程序
        try {
            streamExecutionEnvironment.execute("flink_hdfs_source");
        } catch (Exception e) {
            e.printStackTrace();;
        }

    }

}

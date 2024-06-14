package com.lim.demos.phoenix;

import org.apache.phoenix.queryserver.client.ThinClientUtil;

import java.sql.*;

/**
 * ThinClientDemo
 * <p>Phoenix瘦客户端demo</p>
 * <p>JDBC：</p>
 * @author lim
 * @version 1.0
 * @since 2023/8/14 17:23
 */
public class ThinClientDemo {

    public static void main(String[] args) throws SQLException {
        // 1. 直接从瘦客户端获取链接
        String hadoop102 = ThinClientUtil.getConnectionUrl("hadoop102", 8765);
        // 2. 获取连接
        Connection connection = DriverManager.getConnection(hadoop102);

        // 3.编译SQL语句
        PreparedStatement preparedStatement = connection.prepareStatement("select * from student");

        // 4.执行语句
        ResultSet resultSet = preparedStatement.executeQuery();

        // 5.输出结果
        while (resultSet.next()){
            System.out.println(resultSet.getString(1) + ":" + resultSet.getString(2) + ":" + resultSet.getString(3));
        }

        // 6.关闭资源
        connection.close();
    }

}

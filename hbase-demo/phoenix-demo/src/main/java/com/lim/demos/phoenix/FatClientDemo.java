package com.lim.demos.phoenix;

import java.sql.*;

/**
 * FatClientDemo
 * <p>Phoenix胖客户端demo</p>
 * <p>JDBC：</p>
 *
 * @author lim
 * @version 1.0
 * @since 2023/8/14 16:51
 */
public class FatClientDemo {

    public static void main(String[] args) throws SQLException {
        // 1.添加链接
        String url = "jdbc:phoenix:hadoop102,hadoop103,hadoop104:2181";

        // 2.获取连接
        Connection connection = DriverManager.getConnection(url);

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

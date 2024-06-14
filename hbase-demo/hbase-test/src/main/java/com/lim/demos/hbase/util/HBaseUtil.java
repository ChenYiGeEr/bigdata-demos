package com.lim.demos.hbase.util;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Objects;

/**
 * HBaseUtil
 * <p>操作HBase所需的工具类</p>
 * @author lim
 * @version 1.0
 * @since 2023/8/14 11:14
 * 如何实例化链接：ConnectionFactory
 * 谁调用，谁管理，不用连接及时关闭，释放资源
 * 从connection中获取Table（操作hbase/DML）和Admin（管理表/DDL）对象
 * connection是重量级的，不建议频繁创建，是线程安全的，可以在不同的线程中共享，一般一个app创建一次
 * Table和Admin是轻量级的，不是线程安全的，每个线程有自己的Table和Admin，不建议池化和缓存
 * Table和Admin都是继承自Closeable，使用完毕需要关闭
 *
 */
public class HBaseUtil {

    private static volatile Connection connection;

    /**
     * 方法：getConnection
     * <p>获取hbase的连接 </p>
     *
     * @since 2023/8/14 11:27
     * @author lim
     */
    private static Connection getConnection () {
        if (Objects.isNull(connection)) {
            synchronized (Connection.class) {
                if (Objects.isNull(connection)) {
                    try {
                        connection = ConnectionFactory.createConnection();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return connection;
    }

    /**
     * 方法：getTable
     * <p>根据表名获取Table对象</p>
     *
     * @param tableName 表名
     * @return org.apache.hadoop.hbase.client.Table
     * @since 2023/8/14 11:32
     * @author lim
     */
    public static Table getTable(String tableName) throws IOException {
        if (StringUtils.isBlank(tableName)) { throw new RuntimeException("Illegal table name!"); }
        return getConnection()
                .getTable(TableName.valueOf(tableName));
    }

    /***
     * 方法：getPut
     * <p>获取put对象 </p>
     *
     * @param rowKey rowKey
     * @param columnFamily 列族
     * @param columnQualifier 列名
     * @param value 列值
     * @return org.apache.hadoop.hbase.client.Put
     * @since 2023/8/14 11:37
     * @author lim
     */
    public static Put getPut(String rowKey, String columnFamily, String columnQualifier, String value){
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier), Bytes.toBytes(value));
        return put;
    }
    

    /**
     * 方法：printResult
     * <p>打印结果 </p>
     *
     * @param result 结果
     * @since 2023/8/14 11:46
     * @author lim
     */
    public static void printResult (Result result) {
        if (Objects.isNull(result)) { return; }
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            System.out.println("RowKey          => " + Bytes.toString(CellUtil.cloneRow(cell)));
            System.out.println("ColumnFamily    => " + Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println("ColumnQualifier => " + Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println("Value           => " + Bytes.toString(CellUtil.cloneValue(cell)));
            System.out.println("--------------------------------");
        }
    }

    /**
     * 方法：closeConnection
     * <p>关闭hbase的连接 </p>
     *
     * @since 2023/8/14 11:23
     * @author lim
     */
    public static void closeConnection() throws IOException {
        if (Objects.nonNull(connection)) {
            connection.close();
        }
    }

}

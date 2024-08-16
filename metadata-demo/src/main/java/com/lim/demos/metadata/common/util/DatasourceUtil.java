package com.lim.demos.metadata.common.util;

import com.lim.demos.metadata.common.entity.TableColumnMetaData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

import static com.lim.demos.metadata.common.constants.DatasourceConstants.*;

/**
 * DatasourceUtil
 * <p>数据源工具类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/5/31 上午9:21
 */
public class DatasourceUtil {

    /** 目标配置文件路径 */
    private static final String PROPERTIES_FILE_PATH = USER_DIR + FILE_SEPARATOR + PROJECT_NAME + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "db.properties";

    /** jdbc连接数据源类型 */
    public static String jdbcDbType;

    /** jdbc连接所需的驱动类 */
    private static String jdbcDriver;

    /** jdbc连接所需的url */
    private static String jdbcUrl;

    /** jdbc连接所需的用户名 */
    private static String jdbcUser;

    /** jdbc连接所需的密码 */
    private static String jdbcPassword;

    /** 需要扫描的数据库，若不需要限制则不需填入 */
    public static String jdbcDatabases;

    /** 需要扫描的表schemas，若不需要限制则不需填入 */
    public static String jdbcTableSchemas;

    /* 静态代码块，类初始化时加载 */
    static {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(PROPERTIES_FILE_PATH);
            // 加载properties文件
            prop.load(input);
            jdbcDbType = prop.getProperty(PROPERTY_JDBC_DB_TYPE);
            jdbcDriver = prop.getProperty(PROPERTY_JDBC_DRIVER);
            jdbcUrl = prop.getProperty(PROPERTY_JDBC_URL);
            jdbcUser = prop.getProperty(PROPERTY_JDBC_USERNAME);
            jdbcPassword = prop.getProperty(PROPERTY_JDBC_PASSWORD);
            jdbcDatabases = prop.getProperty(PROPERTY_JDBC_DATABASES);
            jdbcTableSchemas = prop.getProperty(PROPERTY_JDBC_TABLE_SCHEMAS);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // 关闭流
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 方法：getConnection
     * <p>获取数据库的连接 </p>
     *
     * @return java.sql.Connection
     * @since 2024/5/31 上午9:38
     * @author lim
     */
    public static Connection getConnection(){
        try {
            // 加载JDBC驱动
            Class.forName(jdbcDriver);
            // 建立连接
            return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法：getDatabases
     * <p>根据数据库连接信息获取数据库元数据信息 </p>
     *
     * @param connection 数据库连接
     * @param sql 需要执行的sql
     * @param columnNames 多个目标列列名
     * @return java.util.List<java.lang.String> 数据库元数据信息
     * @since 2024/5/31 上午10:01
     * @author lim
     */
    public static List<Map<String, String>> getDatabaseData(Connection connection, String sql, String... columnNames) {
        // 结果集构建
        List<Map<String, String>> result = new ArrayList<>(10);
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            // 处理结果
            Map<String, String> item = null;
            while (resultSet.next()) {
                item = new HashMap<>(columnNames.length);
                for (String columnName : columnNames) {
                    item.put(columnName, resultSet.getString(columnName));
                }
                result.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 方法：getTableColumnMateData
     * <p>根据数据库连接信息获取数据库中数据表的元数据信息 </p>
     *
     * @param connection 数据库连接
     * @param sql 需要执行的sql
     * @return java.util.List<com.lim.demos.kingbase.common.entity.TableColumnMetaData>
     * @since 2024/5/31 上午11:11
     * @author lim
     */
    public static List<TableColumnMetaData> getTableColumnMateData(Connection connection, String sql) {
        // 结果集构建
        TableColumnMetaData tableColumnMetaData = null;
        List<TableColumnMetaData> result = new ArrayList<>(10);
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            // 处理结果
            while (resultSet.next()) {
                tableColumnMetaData = new TableColumnMetaData();
                // 列名
                tableColumnMetaData.setColumnName(resultSet.getString("column_name"));
                // #
                tableColumnMetaData.setIndex(resultSet.getInt("ordinal_position"));
                // 数据类型
                tableColumnMetaData.setColumnType(resultSet.getString("udt_name"));
                // 长度
                tableColumnMetaData.setColumnLength(resultSet.getInt("length"));
                // 标度
                tableColumnMetaData.setColumnScale(resultSet.getInt("scale"));
                // 非空
                tableColumnMetaData.setColumnIsNotNull(!"YES".equalsIgnoreCase(resultSet.getString("is_nullable")));
                // 自动生成
                tableColumnMetaData.setColumnAutoGenerate("ALWAYS".equalsIgnoreCase(resultSet.getString("is_generated")));
                // 自动递增
                tableColumnMetaData.setColumnAutoIncrement("YES".equalsIgnoreCase(resultSet.getString("is_identity")));
                // 默认
                tableColumnMetaData.setColumnDefault(resultSet.getString("column_default"));
                // 主键
                tableColumnMetaData.setColumnPrimaryKey(resultSet.getBoolean("column_key"));
                // 描述
                tableColumnMetaData.setColumnDescribe(resultSet.getString("column_comment"));
                result.add(tableColumnMetaData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 方法：closeConnection
     * <p>关闭数据库的连接 </p>
     *
     * @param connection 关闭链接
     * @throws SQLException sqlException
     * @since 2024/5/31 上午9:39
     * @author lim
     */
    public static void closeConnection(Connection connection) throws SQLException {
        if (connection == null) {
            return;
        }
        connection.close();
    }

}

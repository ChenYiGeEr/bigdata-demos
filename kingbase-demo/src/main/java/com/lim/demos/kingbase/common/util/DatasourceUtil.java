package com.lim.demos.kingbase.common.util;

import com.lim.demos.kingbase.common.entity.TableColumnMateData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.sql.*;
import java.util.*;

import static com.lim.demos.kingbase.common.constants.DatasourceConstants.*;

/**
 * DatasourceUtil
 * <p>数据源工具类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/5/31 上午9:21
 */
public class DatasourceUtil {

    /** 工作目录 */
    public static final String USER_DIR = System.getProperty("user.dir");

    /** 文件分隔符目录 */
    public static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    /** 目标配置文件路径 */
    private static final String PROPERTIES_FILE_PATH = USER_DIR + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "kingbase_db.properties";

    /** 目标excel文件生成位置 */
    private static String getExcelFilePath(String databaseName, String tableSchemaName) {
        return USER_DIR + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "excels" + FILE_SEPARATOR + databaseName + "." + tableSchemaName + ".xlsx";
    }

    /** jdbc连接所需的url */
    private static String jdbcUrl;

    /** jdbc连接所需的用户名 */
    private static String jdbcUser;

    /** jdbc连接所需的密码 */
    private static String jdbcPassword;

    /** 需要扫描的数据库，若不需要限制则不需填入 */
    private static String jdbcDatabase;

    /** 需要扫描的表schemas，若不需要限制则不需填入 */
    private static String jdbcTableSchemas;

    /* 静态代码块，类初始化时加载 */
    static {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(PROPERTIES_FILE_PATH);
            // 加载properties文件
            prop.load(input);
            // 根据key读取value
            jdbcUrl = prop.getProperty(PROPERTY_JDBC_URL);
            jdbcUser = prop.getProperty(PROPERTY_JDBC_USERNAME);
            jdbcPassword = prop.getProperty(PROPERTY_JDBC_PASSWORD);
            jdbcDatabase = prop.getProperty(PROPERTY_JDBC_DATABASES);
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
     * <p>获取人大金仓的连接 </p>
     *
     * @return java.sql.Connection
     * @since 2024/5/31 上午9:38
     * @author lim
     */
    public static Connection getConnection(){
        try {
            // 加载Kingbase JDBC驱动
            Class.forName(KINGBASE_DRIVER);
            // 建立连接
            return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法：getDatabases
     * <p>根据人大金仓连接连接获取数据库元数据信息 </p>
     *
     * @param connection 人大金仓连接
     * @param type 查询类型
     * @param sql 需要执行的sql
     * @param columnName 目标列列名
     * @return java.util.List<java.lang.String> 数据库元数据信息
     * @since 2024/5/31 上午10:01
     * @author lim
     */
    public static List<String> getDatabaseData(Connection connection, String type, String sql, String columnName) {
        // 结果集构建
        List<String> result = new ArrayList<>(10);
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            // 处理结果
            while (resultSet.next()) {
                String name = resultSet.getString(columnName);
                result.add(name);
            }
            if (DB_DATA_BASE.equals(type) && StringUtils.isNotBlank(jdbcDatabase)) {
                result.retainAll(Arrays.asList(jdbcDatabase.split(ENG_COMMA)));
            } else if (DB_TABLE_SCHEMA.equals(type) && StringUtils.isNotBlank(jdbcTableSchemas)) {
                result.retainAll(Arrays.asList(jdbcTableSchemas.split(ENG_COMMA)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 方法：getTableColumnMateData
     * <p>根据人大金仓连接连接获取数据库中数据表的元数据信息 </p>
     *
     * @param connection 人大金仓连接
     * @param sql 需要执行的sql
     * @return java.util.List<com.unicom.kingbase.common.entity.TableColumnMateData>
     * @since 2024/5/31 上午11:11
     * @author lim
     */
    public static List<TableColumnMateData> getTableColumnMateData(Connection connection, String sql) {
        // 结果集构建
        TableColumnMateData tableColumnMateData = null;
        List<TableColumnMateData> result = new ArrayList<>(10);
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            // 处理结果
            while (resultSet.next()) {
                tableColumnMateData = new TableColumnMateData();
                // 列名
                tableColumnMateData.setColumnName(resultSet.getString("column_name"));
                // #
                tableColumnMateData.setIndex(resultSet.getInt("ordinal_position"));
                // 数据类型
                tableColumnMateData.setColumnType(resultSet.getString("udt_name"));
                // 长度
                tableColumnMateData.setColumnLength(resultSet.getInt("length"));
                // 标度
                tableColumnMateData.setColumnScale(resultSet.getInt("scale"));
                // 非空
                tableColumnMateData.setColumnIsNotNull(!"YES".equalsIgnoreCase(resultSet.getString("is_nullable")));
                // 自动生成
                tableColumnMateData.setColumnAutoGenerate("ALWAYS".equalsIgnoreCase(resultSet.getString("is_generated")));
                // 自动递增
                tableColumnMateData.setColumnAutoIncrement("YES".equalsIgnoreCase(resultSet.getString("is_identity")));
                // 默认
                tableColumnMateData.setColumnDefault(resultSet.getString("column_default"));
                // 描述
                tableColumnMateData.setColumnDescribe(resultSet.getString("column_comment"));
                result.add(tableColumnMateData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 方法：closeConnection
     * <p>关闭人大金仓的连接 </p>
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

    /**
     * 方法：exportCsv
     * <p>将表的列信息导出为csv </p>
     *
     * @param databaseName 数据库名称
     * @param tableSchemaName 数据库名称
     * @param tableColumns 表的列信息
     * @since 2024/6/3 上午10:27
     * @author lim
     */
    public static void exportCsv(String databaseName, String tableSchemaName, Map<String, List<TableColumnMateData>> tableColumns) {
        final String targetFileName = getExcelFilePath(databaseName, tableSchemaName);
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        int sheetIndex = 0;
        Set<String> tableNames = tableColumns.keySet();
        // 遍历 tableNames
        for (String tableName : tableNames) {
            sheetIndex += 1;
            // 创建工作表
            sheet = workbook.createSheet("表" + sheetIndex + "_" + tableName);
            // 创建行对象
            Row firstRow = sheet.createRow(0);
            // 第一行固定为表名
            // 创建单元格并设置值
            Cell firstRowFirstCell = firstRow.createCell(0);
            firstRowFirstCell.setCellValue(tableName);
            // 创建行对象
            Row secondRow = sheet.createRow(1);
            // 第二行固定
            // 第二行第一列
            Cell secondRowFirstCell = secondRow.createCell(0);
            secondRowFirstCell.setCellValue("列名");
            // 第二行第二列
            Cell secondRowSecondCell = secondRow.createCell(1);
            secondRowSecondCell.setCellValue("#");
            // 第二行第三列
            Cell secondRowThirdCell = secondRow.createCell(2);
            secondRowThirdCell.setCellValue("数据类型");
            // 第二行第四列
            Cell secondRowFourthCell = secondRow.createCell(3);
            secondRowFourthCell.setCellValue("长度");
            // 第二行第五列
            Cell secondRowFifthCell = secondRow.createCell(4);
            secondRowFifthCell.setCellValue("标度");
            // 第二行第六列
            Cell secondRowSixthCell = secondRow.createCell(5);
            secondRowSixthCell.setCellValue("非空");
            // 第二行第七列
            Cell secondRowSeventhCell = secondRow.createCell(6);
            secondRowSeventhCell.setCellValue("自动生成");
            // 第二行第八列
            Cell secondRowEighthCell = secondRow.createCell(7);
            secondRowEighthCell.setCellValue("自动递增");
            // 第二行第九列
            Cell secondRowNinthCell = secondRow.createCell(8);
            secondRowNinthCell.setCellValue("默认");
            // 第二行第十列
            Cell secondRowTenthCell = secondRow.createCell(9);
            secondRowTenthCell.setCellValue("描述");
            List<TableColumnMateData> tableColumnMateDatas = tableColumns.get(tableName);

            // 填充数据
            TableColumnMateData tableColumnMateData;
            Row row;
            for (int i = 0; i < tableColumnMateDatas.size(); i++) {
                tableColumnMateData = tableColumnMateDatas.get(i);
                row = sheet.createRow(2 + i);
                row.createCell(0).setCellValue(tableColumnMateData.getColumnName());
                row.createCell(1).setCellValue(tableColumnMateData.getIndex());
                row.createCell(2).setCellValue(tableColumnMateData.getColumnType());
                if (Objects.nonNull(tableColumnMateData.getColumnLength())) {
                    if ("varchar".equals(tableColumnMateData.getColumnType()) && tableColumnMateData.getColumnLength() == 0) {
                        row.createCell(3).setCellValue("");
                    } else {
                        row.createCell(3).setCellValue(tableColumnMateData.getColumnLength());
                    }
                } else {
                    row.createCell(3).setCellValue("");
                }
                if (Objects.nonNull(tableColumnMateData.getColumnScale()) && !tableColumnMateData.getColumnScale().equals(0)) {
                    row.createCell(4).setCellValue(tableColumnMateData.getColumnScale());
                } else {
                    row.createCell(4).setCellValue("[NULL]");
                }
                row.createCell(5).setCellValue(tableColumnMateData.getColumnIsNotNull());
                row.createCell(6).setCellValue(tableColumnMateData.getColumnAutoGenerate());
                row.createCell(7).setCellValue(tableColumnMateData.getColumnAutoIncrement());
                row.createCell(8).setCellValue(StringUtils.isNotBlank(tableColumnMateData.getColumnDefault()) ? tableColumnMateData.getColumnDefault() : "[NULL]");
                row.createCell(9).setCellValue(tableColumnMateData.getColumnDescribe());
            }
        }

        // 导出到文件
        try (FileOutputStream outputStream = new FileOutputStream(targetFileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** 主函数 */
    public static void main(String[] args) throws SQLException {
        // 1. 开启连接
        Connection connection = getConnection();
        if (connection == null) {
            throw new RuntimeException("connect failed");
        }
        // 2. 获取数据库中元数据信息
        List<String> databases = getDatabaseData(connection, DB_DATA_BASE, SQL_GET_ALL_DATABASES_NAME, COLUMN_DATABASE_NAME);
        Map<String, List<TableColumnMateData>> tableColumns = null;
        for (String databaseName : databases) {
            List<String> tableSchemas = getDatabaseData(connection, DB_TABLE_SCHEMA, getDatabaseTableSchemasSql(COLUMN_TABLE_SCHEMA_NAME, databaseName), COLUMN_TABLE_SCHEMA_NAME);
            tableColumns = new HashMap<>(tableSchemas.size());
            for (String tableSchemaName : tableSchemas) {
                List<String> tables = getDatabaseData(connection, DB_TABLE, getDatabaseTablesSql(COLUMN_TABLE_NAME, databaseName, tableSchemaName), COLUMN_TABLE_NAME);
                for (String tableName : tables) {
                    List<TableColumnMateData> tableColumnMateData = getTableColumnMateData(connection, getDatabaseTableColumnSchemasSql(databaseName, tableSchemaName, tableName));
                    if (tableColumnMateData == null || tableColumnMateData.isEmpty()) {
                        // 跳出此次循环
                        continue;
                    }
                    tableColumns.put(tableName, tableColumnMateData);
                }
                exportCsv(databaseName, tableSchemaName, tableColumns);
            }
        }
        // 4. 写出到excel中
        // 5. 关闭连接
        closeConnection(connection);
    }

}

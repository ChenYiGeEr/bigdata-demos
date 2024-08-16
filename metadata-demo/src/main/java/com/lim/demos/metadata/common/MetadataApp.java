package com.lim.demos.metadata.common;

import com.lim.demos.metadata.common.entity.TableColumnMetaData;
import com.lim.demos.metadata.common.entity.TableMetaData;
import com.lim.demos.metadata.common.enums.DbType;
import com.lim.demos.metadata.common.process.DatabaseSqlProcess;
import com.lim.demos.metadata.common.util.DatasourceUtil;
import com.lim.demos.metadata.common.util.ExportUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lim.demos.metadata.common.constants.DatasourceConstants.*;

/**
 * MetadataApp
 * <p>主函数</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/8/16 下午3:36
 */
public class MetadataApp {

    /** 主函数 */
    public static void main(String[] args) throws SQLException {
        // 1. 开启连接
        Connection connection = DatasourceUtil.getConnection();
        if (connection == null) {
            throw new RuntimeException("connect failed !");
        }
        // 2. 根据数据源类型获取对应的SQL处理器
        if (StringUtils.isBlank(DatasourceUtil.jdbcDbType)) {
            throw new RuntimeException("there is no jdbc.dbType in db.properties !");
        }
        DatabaseSqlProcess sqlProcess = DbType.ofName(DatasourceUtil.jdbcDbType).getDatabaseSqlProcess();
        // 3. 获取数据库中元数据信息
        List<Map<String, String>> databases = DatasourceUtil.getDatabaseData(connection, sqlProcess.getAllDatabasesName(DatasourceUtil.jdbcDatabases), COLUMN_DATABASE_NAME);
        Map<TableMetaData, List<TableColumnMetaData>> tableColumns = null;
        for (Map<String, String> databaseNameMap : databases) {
            List<Map<String, String>> tableSchemas = DatasourceUtil.getDatabaseData(connection, sqlProcess.getDatabaseTableSchemasSql(COLUMN_TABLE_SCHEMA_NAME, databaseNameMap.get(COLUMN_DATABASE_NAME), DatasourceUtil.jdbcTableSchemas), COLUMN_TABLE_SCHEMA_NAME);
            tableColumns = new HashMap<>(tableSchemas.size());
            for (Map<String, String> tableSchemaNameMap : tableSchemas) {
                List<Map<String, String>> tables = DatasourceUtil.getDatabaseData(connection, sqlProcess.getDatabaseTablesSql(COLUMN_TABLE_NAME, databaseNameMap.get(COLUMN_DATABASE_NAME), DatasourceUtil.jdbcTableSchemas, tableSchemaNameMap.get(COLUMN_TABLE_SCHEMA_NAME)), COLUMN_TABLE_NAME, COLUMN_TABLE_COMMENT);
                TableMetaData tableMetaData = null;
                for (Map<String, String> tableNameMap : tables) {
                    tableMetaData = new TableMetaData(tableNameMap.get(COLUMN_TABLE_NAME), tableNameMap.get(COLUMN_TABLE_COMMENT));
                    List<TableColumnMetaData> tableColumnMetaData = DatasourceUtil.getTableColumnMateData(connection, sqlProcess.getDatabaseTableColumnSchemasSql(databaseNameMap.get(COLUMN_DATABASE_NAME), tableSchemaNameMap.get(COLUMN_TABLE_SCHEMA_NAME), tableNameMap.get(COLUMN_TABLE_NAME)));
                    if (tableColumnMetaData == null || tableColumnMetaData.isEmpty()) {
                        // 跳出此次循环
                        continue;
                    }
                    tableColumns.put(tableMetaData, tableColumnMetaData);
                }
                // 4. 写出到excel中
                ExportUtil.exportCsv(databaseNameMap.get(COLUMN_DATABASE_NAME), tableSchemaNameMap.get(COLUMN_TABLE_SCHEMA_NAME), tableColumns);
                // 5. 写出到word中
                ExportUtil.exportWord(databaseNameMap.get(COLUMN_DATABASE_NAME), tableSchemaNameMap.get(COLUMN_TABLE_SCHEMA_NAME), tableColumns);
            }
        }
        // 5. 关闭连接
        DatasourceUtil.closeConnection(connection);
    }

}

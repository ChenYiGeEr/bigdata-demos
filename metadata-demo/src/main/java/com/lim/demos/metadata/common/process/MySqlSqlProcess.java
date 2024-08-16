package com.lim.demos.metadata.common.process;

import com.lim.demos.metadata.common.constants.SymbolConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static com.lim.demos.metadata.common.constants.DatasourceConstants.*;

/**
 * MySqlSqlProcess
 * <p>MySQL数据库SQL处理</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/8/16 下午3:58
 */
public class MySqlSqlProcess implements DatabaseSqlProcess {

    /** MySQL 获取所有的数据库名称 */
    @Override
    public String getAllDatabasesName(String jdbcDatabases) {
        String whereSql = "1 = 1";
        if (StringUtils.isNotBlank(jdbcDatabases)) {
            whereSql = "table_schema IN ('" + String.join("','", Arrays.asList(jdbcDatabases.split(SymbolConstants.ENG_COMMA))) + "')";
        }
        return String.format(
                "select distinct table_schema as %s from information_schema.tables where %s order by %s", COLUMN_DATABASE_NAME, whereSql, COLUMN_DATABASE_NAME);
    }

    /** MySQL 获取某一数据库下的表schema名称 */
    @Override
    public String getDatabaseTableSchemasSql(String columnName, String databaseName, String jdbcTableSchemas) {
        String sql = "select\n" +
                "\tdistinct tbl.table_schema as %s\n" +
                "\tfrom\n" +
                "\tinformation_schema.tables tbl\n" +
                "\twhere\n" +
                "\ttbl.table_catalog = 'def'";
        if (StringUtils.isNotBlank(jdbcTableSchemas)) {
            sql += String.format(" and tbl.%s IN ('%s')", columnName, String.join("','", Arrays.asList(jdbcTableSchemas.split(SymbolConstants.ENG_COMMA))));
        }
        return String.format(sql, columnName, databaseName);
    }

    /** MySQL 获取某一数据库下的表名称 */
    @Override
    public String getDatabaseTablesSql(String columnName, String databaseName, String jdbcTableSchemas, String tableSchemaName) {
        String sql = "select\n" +
                "\ttbl.table_name as %s,\n" +
                "\ttbl.table_comment as " + COLUMN_TABLE_COMMENT + "\n" +
                "\tfrom information_schema.tables tbl\n" +
                "\twhere\n" +
                "\ttbl.table_schema = '%s'\n";
        if (StringUtils.isNotBlank(jdbcTableSchemas)) {
            sql += String.format("\tand tbl.table_schema IN ('%s')\n", String.join("','", Arrays.asList(jdbcTableSchemas.split(SymbolConstants.ENG_COMMA))));
        }
        return String.format("%s and tbl." + COLUMN_TABLE_SCHEMA_NAME + "='%s' order by tbl.%s",
                String.format(sql, columnName, databaseName),
                tableSchemaName,
                columnName);
    }

    /** MySQL 获取某一数据库下某一张表的名称 */
    @Override
    public String getDatabaseTableColumnSchemasSql(String databaseName, String tableSchemaName, String tableName) {
        String sql = "select\n" +
                "\tcol.column_name,\n" +
                "\tcol.ordinal_position,\n" +

                "\tcol.data_type as udt_name,\n" +
                "\tcase col.data_type\n" +
                "\twhen 'bigint' then 1" +
                "\twhen 'binary' then 1" +
                "\twhen 'blob' then 1" +
                "\twhen 'char' then 5" +
                "\twhen 'datetime' then 5" +
                "\twhen 'decimal' then 10" +
                "\twhen 'double' then 10" +
                "\twhen 'enum' then 10" +
                "\twhen 'float' then 10" +
                "\twhen 'int' then 13" +
                "\twhen 'json' then 17" +
                "\twhen 'longblob' then 19" +
                "\twhen 'longtext' then 29" +
                "\twhen 'mediumblob' then 65535" +
                "\twhen 'mediumtext' then col.numeric_precision" +
                "\twhen 'set' then col.character_maximum_length\n" +
                "\twhen 'smallint' then col.character_maximum_length\n" +
                "\twhen 'text' then col.character_maximum_length\n" +
                "\twhen 'time' then col.character_maximum_length\n" +
                "\twhen 'timestamp' then col.character_maximum_length\n" +
                "\twhen 'tinyint' then col.character_maximum_length\n" +
                "\twhen 'varbinary' then col.character_maximum_length\n" +
                "\twhen 'varchar' then col.character_maximum_length\n" +
                "\telse 0\n" +
                "\tend as length,\n" +

                "\tcase col.data_type\n" +
                "\twhen 'timestamp' then col.datetime_precision\n" +
                "\telse col.numeric_scale\n" +
                "\tend as scale,\n" +

                "\tcol.is_nullable,\n" +
                "\tif(col.column_default is null, '', 'ALWAYS') as is_generated,\n" +
                "\t'NO' as is_identity,\n" +
                "\tcol.column_default,\n" +
                "\tif(col.column_key is null, 'false', 'true') as column_key,\n" +
                "\tcol.column_comment\n" +
                "from information_schema.columns col\n" +
                "where\n" +
                "\tcol.table_schema = '%s'\n" +
                "\tand col.table_name = '%s'\n" +
                "order by col.ordinal_position;";
        return String.format(sql, databaseName, tableName);
    }

}

package com.lim.demos.metadata.common.process;

import com.lim.demos.metadata.common.constants.SymbolConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static com.lim.demos.metadata.common.constants.DatasourceConstants.*;

/**
 * KingBaseSqlUtil
 * <p>人大金仓数据库SQL处理</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/8/16 下午3:28
 */
public class KingBaseSqlProcess implements DatabaseSqlProcess {

    /** 人大金仓sql 获取所有的数据库名称 */
    @Override
    public String getAllDatabasesName (String jdbcDatabases) {
        String whereSql = "1 = 1";
        if (StringUtils.isNotBlank(jdbcDatabases)) {
            whereSql = COLUMN_DATABASE_NAME + " IN ('" + String.join("','", Arrays.asList(jdbcDatabases.split(SymbolConstants.ENG_COMMA))) + "')";
        }
        return String.format(
                "select %s from sys_database where %s order by %s", COLUMN_DATABASE_NAME, whereSql, COLUMN_DATABASE_NAME);
    }

    /** 人大金仓sql 获取某一数据库下的表schema名称 */
    @Override
    public String getDatabaseTableSchemasSql(String columnName, String databaseName, String jdbcTableSchemas) {
        String sql = "select\n" +
                "\tdistinct tbl.%s\n" +
                "\tfrom\n" +
                "\tinformation_schema.tables tbl\n" +
                "\twhere\n" +
                "\ttbl.table_catalog = '%s'";
        if (StringUtils.isNotBlank(jdbcTableSchemas)) {
            sql += String.format(" and tbl.%s IN ('%s')", columnName, String.join("','", Arrays.asList(jdbcTableSchemas.split(SymbolConstants.ENG_COMMA))));
        }
        return String.format(sql, columnName, databaseName);
    }

    /** 人大金仓sql 获取某一数据库下的表名称 */
    @Override
    public String getDatabaseTablesSql(String columnName, String databaseName, String jdbcTableSchemas, String tableSchemaName) {
        String sql = "select\n" +
                "\ttbl.%s,\n" +
                "\tobj_description(c.relfilenode,'pg_class') as " + COLUMN_TABLE_COMMENT + "\n" +
                "\tfrom\n" +
                "\tinformation_schema.tables tbl\n" +
                "\tleft join pg_catalog.pg_class c on c.relname = tbl.table_name\n" +
                "\twhere\n" +
                "\ttbl.table_catalog = '%s'\n";
        if (StringUtils.isNotBlank(jdbcTableSchemas)) {
            sql += String.format("\tand tbl.table_schema IN ('%s')\n", String.join("','", Arrays.asList(jdbcTableSchemas.split(SymbolConstants.ENG_COMMA))));
        }
        return String.format("%s and tbl." + COLUMN_TABLE_SCHEMA_NAME + "='%s' order by tbl.%s",
                String.format(sql, columnName, databaseName),
                tableSchemaName,
                columnName);
    }

    /** 人大金仓sql 获取某一数据库下某一张表的名称 */
    @Override
    public String getDatabaseTableColumnSchemasSql(String databaseName, String tableSchemaName, String tableName) {
        String sql = "select\n" +
                "\tcol.column_name,\n" +
                "\tcol.ordinal_position,\n" +
                "\tcol.udt_name,\n" +
                "\tcase col.udt_name\n" +
                "\twhen 'bit' then 1" +
                "\twhen 'char' then 1" +
                "\twhen 'bool' then 1" +
                "\twhen 'smallint' then 5" +
                "\twhen 'int2' then 5" +
                "\twhen 'int' then 10" +
                "\twhen 'int4' then 10" +
                "\twhen 'oid' then 10" +
                "\twhen 'bpchar' then 10" +
                "\twhen 'date' then 13" +
                "\twhen 'float8' then 17" +
                "\twhen 'int8' then 19" +
                "\twhen 'timestamp' then 29" +
                "\twhen 'txt' then 65535" +
                "\twhen 'numeric' then col.numeric_precision" +
                "\twhen 'varchar' then col.character_maximum_length\n" +
                "\telse 0\n" +
                "\tend as length,\n" +
                "\tcase col.udt_name\n" +
                "\twhen 'timestamp' then col.datetime_precision\n" +
                "\telse col.numeric_scale\n" +
                "\tend as scale,\n" +
                "\tcol.is_nullable,\n" +
                "\tcol.is_generated,\n" +
                "\tcol.is_identity,\n" +
                "\tcol.column_default,\n" +
                "\t'false' as column_key,\n" +
                "\tcol_description(a.attrelid, a.attnum) as column_comment\n" +
                "from\n" +
                "\tinformation_schema.columns col\n" +
                "left join pg_catalog.pg_class c on c.relname = col.table_name\n" +
                "left join pg_catalog.pg_attribute a on c.oid = a.attrelid and col.column_name = a.attname\n" +
                "where\n" +
                "\tcol.table_catalog = '%s'\n" +
                "\tand col.table_schema = '%s'\n" +
                "\tand col.table_name = '%s'\n" +
                "order by col.ordinal_position;";
        return String.format(sql, databaseName, tableSchemaName, tableName);
    }

}

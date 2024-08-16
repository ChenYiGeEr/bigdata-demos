package com.lim.demos.kingbase.common.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * DatasourceConstants
 * <p>数据源常量</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/5/31 上午9:19
 */
public class DatasourceConstants {

    public static final String ENG_COMMA = ",";

    public static final String PROPERTY_JDBC_URL = "jdbc.url";

    public static final String PROPERTY_JDBC_USERNAME = "jdbc.username";

    public static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";

    public static final String PROPERTY_JDBC_DATABASES = "jdbc.databases";

    public static final String PROPERTY_JDBC_TABLE_SCHEMAS = "jdbc.tableSchemas";

    /** 人大金仓启动类全限定名 */
    public static final String KINGBASE_DRIVER = "com.kingbase8.Driver";

    /** 数据库名称列 */
    public static final String COLUMN_DATABASE_NAME = "datname";

    /** 数据表schema名称列 */
    public static final String COLUMN_TABLE_SCHEMA_NAME = "table_schema";

    /** 数据表名称列 */
    public static final String COLUMN_TABLE_NAME = "table_name";

    /** 数据表备注列 */
    public static final String COLUMN_TABLE_COMMENT = "table_comment";

    /** 人大金仓sql 获取所有的数据库名称 */
    public static String getAllDatabasesName (String jdbcDatabases) {
        String whereSql = "1 = 1";
        if (StringUtils.isNotBlank(jdbcDatabases)) {
            whereSql = COLUMN_DATABASE_NAME + " IN ('" + String.join("','", Arrays.asList(jdbcDatabases.split(ENG_COMMA))) + "')";
        }
        return String.format(
                "select %s from sys_database where %s order by %s", COLUMN_DATABASE_NAME, whereSql, COLUMN_DATABASE_NAME);
    }

    /** 人大金仓sql 获取某一数据库下的表schema名称 */
    public static String getDatabaseTableSchemasSql(String columnName, String databaseName, String jdbcTableSchemas){
        String sql = "select\n" +
                "\tdistinct tbl.%s\n" +
                "\tfrom\n" +
                "\tinformation_schema.tables tbl\n" +
                "\twhere\n" +
                "\ttbl.table_catalog = '%s'";
        if (StringUtils.isNotBlank(jdbcTableSchemas)) {
            sql += String.format(" and tbl.%s IN ('%s')", columnName, String.join("','", Arrays.asList(jdbcTableSchemas.split(ENG_COMMA))));
        }
        return String.format(sql, columnName, databaseName);
    }

    /** 人大金仓sql 获取某一数据库下的表名称 */
    public static String getDatabaseTablesSql(String columnName, String databaseName, String jdbcTableSchemas, String tableSchemaName) {
        String sql = "select\n" +
                "\ttbl.%s,\n" +
                "\tobj_description(c.relfilenode,'pg_class') as " + COLUMN_TABLE_COMMENT + "\n" +
                "\tfrom\n" +
                "\tinformation_schema.tables tbl\n" +
                "\tleft join pg_catalog.pg_class c on c.relname = tbl.table_name\n" +
                "\twhere\n" +
                "\ttbl.table_catalog = '%s'\n";
        if (StringUtils.isNotBlank(jdbcTableSchemas)) {
            sql += String.format("\tand tbl.table_schema IN ('%s')\n", String.join("','", Arrays.asList(jdbcTableSchemas.split(ENG_COMMA))));
        }
        return String.format("%s and tbl." + COLUMN_TABLE_SCHEMA_NAME + "='%s' order by tbl.%s",
                String.format(sql, columnName, databaseName),
                tableSchemaName,
                columnName);
    }

    /** 人大金仓sql 获取某一数据库下某一张表的名称 */
    public static String getDatabaseTableColumnSchemasSql(String databaseName, String tableSchemaName, String tableName) {
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

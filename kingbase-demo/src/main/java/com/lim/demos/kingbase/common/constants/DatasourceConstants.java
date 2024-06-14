package com.lim.demos.kingbase.common.constants;

/**
 * DatasourceConstants
 * <p>数据源常量</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/5/31 上午9:19
 */
public class DatasourceConstants {

    public static final String DB_DATA_BASE = "database";

    public static final String DB_TABLE_SCHEMA = "tableSchema";

    public static final String DB_TABLE = "table";

    public static final String ENG_COMMA = ",";

    public static final String PROPERTY_JDBC_URL = "jdbc.url";

    public static final String PROPERTY_JDBC_USERNAME = "jdbc.username";

    public static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";

    public static final String PROPERTY_JDBC_DATABASES = "jdbc.databases";

    public static final String PROPERTY_JDBC_TABLE_SCHEMAS = "jdbc.tableSchemas";

    /** 人大金仓url前缀 */
    public static final String KINGBASE_URL_PREFIX = "jdbc:kingbase8://";

    /** 人大金仓启动类全限定名 */
    public static final String KINGBASE_DRIVER = "com.kingbase8.Driver";

    /** 数据库名称列 */
    public static final String COLUMN_DATABASE_NAME = "datname";

    /** 人大金仓sql 获取所有的数据库名称 */
    public static final String SQL_GET_ALL_DATABASES_NAME = String.format("select %s from sys_database order by %s", COLUMN_DATABASE_NAME, COLUMN_DATABASE_NAME);

    /** 数据表schema名称列 */
    public static final String COLUMN_TABLE_SCHEMA_NAME = "table_schema";

    /** 人大金仓sql 获取某一数据库下的表schema名称 */
    public static String getDatabaseTableSchemasSql(String columnName, String databaseName){
        return String.format("select distinct %s from information_schema.tables where table_catalog='%s'", columnName, databaseName);
    }

    /** 数据表名称列 */
    public static final String COLUMN_TABLE_NAME = "table_name";

    /** 人大金仓sql 获取某一数据库下的表名称 */
    public static String getDatabaseTablesSql(String columnName, String databaseName, String tableSchemaName) {
        return String.format("%s and " + COLUMN_TABLE_SCHEMA_NAME + "='%s' order by %s", getDatabaseTableSchemasSql(columnName, databaseName), tableSchemaName, columnName);
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

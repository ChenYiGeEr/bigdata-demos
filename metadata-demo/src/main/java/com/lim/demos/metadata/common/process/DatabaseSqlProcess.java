package com.lim.demos.metadata.common.process;

/**
 * DatabaseSqlProcess
 * <p>数据库SQL处理</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/8/16 下午3:54
 */
public interface DatabaseSqlProcess {

    /** 获取所有的数据库名称 */
    String getAllDatabasesName (String jdbcDatabases);

    /** 获取某一数据库下的表schema名称 */
    String getDatabaseTableSchemasSql(String columnName, String databaseName, String jdbcTableSchemas);

    /** 获取某一数据库下的表名称 */
    String getDatabaseTablesSql(String columnName, String databaseName, String jdbcTableSchemas, String tableSchemaName);

    /** 获取某一数据库下某一张表的名称 */
    String getDatabaseTableColumnSchemasSql(String databaseName, String tableSchemaName, String tableName);

}

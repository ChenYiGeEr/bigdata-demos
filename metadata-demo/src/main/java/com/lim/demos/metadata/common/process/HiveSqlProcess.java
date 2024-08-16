package com.lim.demos.metadata.common.process;

/**
 * HiveSqlProcess
 * <p>Hive数据库SQL处理</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/8/16 下午4:40
 */
public class HiveSqlProcess implements DatabaseSqlProcess {

    /** hql 获取所有的数据库名称 */
    @Override
    public String getAllDatabasesName(String jdbcDatabases) {
        return "";
    }

    /** hql 获取某一数据库下的表schema名称 */
    @Override
    public String getDatabaseTableSchemasSql(String columnName, String databaseName, String jdbcTableSchemas) {
        return "";
    }

    /** hql 获取某一数据库下的表名称 */
    @Override
    public String getDatabaseTablesSql(String columnName, String databaseName, String jdbcTableSchemas, String tableSchemaName) {
        return "";
    }

    /** hql 获取某一数据库下某一张表的名称 */
    @Override
    public String getDatabaseTableColumnSchemasSql(String databaseName, String tableSchemaName, String tableName) {
        return "";
    }

}

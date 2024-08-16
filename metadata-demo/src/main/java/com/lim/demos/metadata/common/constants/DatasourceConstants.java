package com.lim.demos.metadata.common.constants;

import java.nio.file.FileSystems;

/**
 * DatasourceConstants
 * <p>数据源常量</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/5/31 上午9:19
 */
public class DatasourceConstants {

    /** 工作目录 */
    public static final String USER_DIR = System.getProperty("user.dir");

    /** 项目目录名 */
    public static final String PROJECT_NAME = "metadata-demo";

    /** 文件分隔符目录 */
    public static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    /** 数据库类型 */
    public static final String PROPERTY_JDBC_DB_TYPE = "jdbc.dbType";

    public static final String PROPERTY_JDBC_DRIVER = "jdbc.driver";

    public static final String PROPERTY_JDBC_URL = "jdbc.url";

    public static final String PROPERTY_JDBC_USERNAME = "jdbc.username";

    public static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";

    public static final String PROPERTY_JDBC_DATABASES = "jdbc.databases";

    public static final String PROPERTY_JDBC_TABLE_SCHEMAS = "jdbc.tableSchemas";

    /** 数据库名称列 */
    public static final String COLUMN_DATABASE_NAME = "datname";

    /** 数据表schema名称列 */
    public static final String COLUMN_TABLE_SCHEMA_NAME = "table_schema";

    /** 数据表名称列 */
    public static final String COLUMN_TABLE_NAME = "table_name";

    /** 数据表备注列 */
    public static final String COLUMN_TABLE_COMMENT = "table_comment";

}

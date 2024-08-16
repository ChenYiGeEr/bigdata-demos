package com.lim.demos.metadata.common.enums;

import com.lim.demos.metadata.common.process.DatabaseSqlProcess;
import com.lim.demos.metadata.common.process.HiveSqlProcess;
import com.lim.demos.metadata.common.process.KingBaseSqlProcess;
import com.lim.demos.metadata.common.process.MySqlSqlProcess;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * DbType
 * <p>数据库类型</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/8/16 下午3:45
 */
public enum DbType {

    /** MySQL */
    MYSQL(0, "mysql", new MySqlSqlProcess()),

    /** 人大金仓 kingbase */
    KINGBASE(1, "kingbase", new KingBaseSqlProcess()),

    /** HIVE */
    HIVE(2, "hive", new HiveSqlProcess());

    private final int code;

    private final String name;

    private final DatabaseSqlProcess databaseSqlProcess;

    DbType(int code, String name, DatabaseSqlProcess databaseSqlProcess) {
        this.code = code;
        this.name = name;
        this.databaseSqlProcess = databaseSqlProcess;
    }

    public static DbType ofName(String name) {
        return Arrays.stream(DbType.values()).filter(e -> e.getName().equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("no such db type"));
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public DatabaseSqlProcess getDatabaseSqlProcess() {
        return databaseSqlProcess;
    }

}

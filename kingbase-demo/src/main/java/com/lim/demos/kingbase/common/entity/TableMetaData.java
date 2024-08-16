package com.lim.demos.kingbase.common.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * TableMetaData
 * <p>表格的元数据信息</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/8/15 下午3:37
 */
public class TableMetaData implements Serializable {

    /** 表名 */
    private String tableName;

    /** 表备注 */
    private String tableComment;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public TableMetaData(String tableName, String tableComment) {
        this.tableName = tableName;
        this.tableComment = tableComment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("tableName", getTableName())
                .append("tableComment", getTableComment())
                .toString();
    }
}

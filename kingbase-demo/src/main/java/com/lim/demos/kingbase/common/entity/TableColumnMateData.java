package com.lim.demos.kingbase.common.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * TableColumnMateData
 * <p>表格列的元数据</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/5/31 上午11:05
 */
public class TableColumnMateData implements Serializable {

    /** 列名 */
    private String columnName;

    /** # */
    private Integer index;

    /** 数据类型 */
    private String columnType;

    /** 长度 */
    private Integer columnLength;

    /** 标度 */
    private Integer columnScale;

    /** 非空 */
    private Boolean columnIsNotNull;

    /** 自动生成 */
    private Boolean columnAutoGenerate;

    /** 自动递增 */
    private Boolean columnAutoIncrement;

    /** 默认 */
    private String columnDefault;

    /** 描述 */
    private String columnDescribe;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Integer getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(Integer columnLength) {
        this.columnLength = columnLength;
    }

    public Integer getColumnScale() {
        return columnScale;
    }

    public void setColumnScale(Integer columnScale) {
        this.columnScale = columnScale;
    }

    public Boolean getColumnIsNotNull() {
        return columnIsNotNull;
    }

    public void setColumnIsNotNull(Boolean columnIsNotNull) {
        this.columnIsNotNull = columnIsNotNull;
    }

    public Boolean getColumnAutoGenerate() {
        return columnAutoGenerate;
    }

    public void setColumnAutoGenerate(Boolean columnAutoGenerate) {
        this.columnAutoGenerate = columnAutoGenerate;
    }

    public Boolean getColumnAutoIncrement() {
        return columnAutoIncrement;
    }

    public void setColumnAutoIncrement(Boolean columnAutoIncrement) {
        this.columnAutoIncrement = columnAutoIncrement;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getColumnDescribe() {
        return columnDescribe;
    }

    public void setColumnDescribe(String columnDescribe) {
        this.columnDescribe = columnDescribe;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("columnName", getColumnName())
                .append("index", getIndex())
                .append("columnType", getColumnType())
                .append("columnLength", getColumnLength())
                .append("columnScale", getColumnScale())
                .append("columnIsNotNull", getColumnIsNotNull())
                .append("columnAutoGenerate", getColumnAutoGenerate())
                .append("columnAutoIncrement", getColumnAutoIncrement())
                .append("columnDefault", getColumnDefault())
                .append("columnDescribe", getColumnDescribe())
                .toString();
    }
}
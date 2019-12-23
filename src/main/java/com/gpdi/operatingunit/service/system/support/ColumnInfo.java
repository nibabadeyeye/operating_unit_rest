package com.gpdi.operatingunit.service.system.support;

import java.io.Serializable;

/**
 * @author Zhb
 * @date 2019/10/30 10:52
 **/
public class ColumnInfo implements Serializable{

    private String columnName;
    private String columnComment;
    private String dataType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "columnName='" + columnName + '\'' +
                ", columnComment='" + columnComment + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}

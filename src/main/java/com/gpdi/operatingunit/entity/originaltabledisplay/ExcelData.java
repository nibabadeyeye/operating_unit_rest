package com.gpdi.operatingunit.entity.originaltabledisplay;

import lombok.Data;

@Data
public class ExcelData {

    //主键
    private int id;
    //ExcelId
    private int excelId;
    //表格数据
    private String coreData;
    //跨列 默认为0，1为两列，2为跨三列
    private int crossColumn;
    //跨行，默认为0,1为跨两行，2为跨三行
    private int crossRow;
    //所在行
    private int rowNumber;
    //所在列
    private int columnNumber;
    //是否表头,1标识表头，二表示非表头数据
    private int isHead;

    //行高
    private int height;
    //行宽
    private int width;

    public ExcelData(int id, int excelId, String coreData, int crossColumn, int crossRow, int rowNumber, int columnNumber, int isHead, int height, int width) {
        this.id = id;
        this.excelId = excelId;
        this.coreData = coreData;
        this.crossColumn = crossColumn;
        this.crossRow = crossRow;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.isHead = isHead;
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExcelId() {
        return excelId;
    }

    public void setExcelId(int excelId) {
        this.excelId = excelId;
    }

    public String getCoreData() {
        return coreData;
    }

    public void setCoreData(String coreData) {
        this.coreData = coreData;
    }

    public int getCrossColumn() {
        return crossColumn;
    }

    public void setCrossColumn(int crossColumn) {
        this.crossColumn = crossColumn;
    }

    public int getCrossRow() {
        return crossRow;
    }

    public void setCrossRow(int crossRow) {
        this.crossRow = crossRow;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public int getIsHead() {
        return isHead;
    }

    public void setIsHead(int isHead) {
        this.isHead = isHead;
    }

    public ExcelData() {
    }


}

package com.gpdi.operatingunit.controller.originaltabledisplay;
/*
@desc:删除Excel多余的单元格
*/
public class DeletePara {
    private int rowNumber;
    private int columnNumber;
    private int excelId;

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

    public int getExcelId() {
        return excelId;
    }

    public void setExcelId(int excelId) {
        this.excelId = excelId;
    }

    public DeletePara() {
    }

    public DeletePara(int rowNumber, int columnNumber, int excelId) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.excelId = excelId;
    }
}

package com.gpdi.operatingunit.controller.originaltabledisplay;

public class PoiUtil {

    private int firstRow;

    private int lastRow;

    private int firsColumn;

    private int lastColumn;

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getFirsColumn() {
        return firsColumn;
    }

    public void setFirsColumn(int firsColumn) {
        this.firsColumn = firsColumn;
    }

    public int getLastColumn() {
        return lastColumn;
    }

    public void setLastColumn(int lastColumn) {
        this.lastColumn = lastColumn;
    }

    public PoiUtil() {
    }

    public PoiUtil(int firstRow, int lastRow, int firsColumn, int lastColumn) {
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firsColumn = firsColumn;
        this.lastColumn = lastColumn;
    }
}

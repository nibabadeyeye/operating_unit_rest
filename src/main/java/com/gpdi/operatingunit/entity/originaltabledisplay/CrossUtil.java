package com.gpdi.operatingunit.entity.originaltabledisplay;

import lombok.Data;

@Data
public class CrossUtil {

    //拼接的行+列
    private String  rowAddLine;

    private int crossRowNumber;

    private int crossColumnNumber;

    public String getRowAddLine() {
        return rowAddLine;
    }

    public void setRowAddLine(String rowAddLine) {
        this.rowAddLine = rowAddLine;
    }

    public int getCrossRowNumber() {
        return crossRowNumber;
    }

    public void setCrossRowNumber(int crossRowNumber) {
        this.crossRowNumber = crossRowNumber;
    }

    public int getCrossColumnNumber() {
        return crossColumnNumber;
    }

    public void setCrossColumnNumber(int crossColumnNumber) {
        this.crossColumnNumber = crossColumnNumber;
    }

    public CrossUtil(String rowAddLine, int crossRowNumber, int crossColumnNumber) {
        this.rowAddLine = rowAddLine;
        this.crossRowNumber = crossRowNumber;
        this.crossColumnNumber = crossColumnNumber;
    }
}

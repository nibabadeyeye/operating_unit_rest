package com.gpdi.operatingunit.entity.originaltabledisplay;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelUtil {


    //获取Excel单元格中的数值
    public String getCellValue(Cell cell) {
        String strCell = "";
        if (cell != null) {
            //字符串类型
            if (cell.getCellTypeEnum().equals(CellType.STRING)) {
                strCell = cell.getStringCellValue();
                if (strCell.equals("")) {
                    int aa = (int) cell.getNumericCellValue();
                    strCell = "" + aa + " ";
                }
                //空类型
            } else if (cell.getCellTypeEnum().equals(CellType.BLANK)) {
                strCell = "";
                //数字类型
            } else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                int temp = (int) cell.getNumericCellValue();
                strCell = "" + temp + "";
                //空
            } else if (cell.getCellTypeEnum().equals(CellType._NONE)) {
                strCell = "";
                //公式
            } else if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    strCell = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                } else {
                    strCell = String.valueOf(cell.getNumericCellValue());
                }
                //错误
            } else if (cell.getCellTypeEnum().equals(CellType.ERROR)) {
                strCell = cell.getStringCellValue();
                System.out.println("有数值吗？？？" + strCell);
                //Boolean类型
            } else if (cell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
                strCell = "" + cell.getBooleanCellValue() + "";
                System.out.println("有数值吗？？？" + strCell);
            } else {
                strCell = cell.getStringCellValue();
                System.out.println("有数值222吗？？？" + strCell);
            }
        }

        return strCell.trim();
    }
}

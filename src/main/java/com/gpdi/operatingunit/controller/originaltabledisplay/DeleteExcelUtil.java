package com.gpdi.operatingunit.controller.originaltabledisplay;


import java.util.ArrayList;
import java.util.List;

public class DeleteExcelUtil {
    public static void main(String[] args) {
        List<PoiUtil> list = new ArrayList<>();
        list.add(new PoiUtil(5, 5, 1, 2));
        list.add(new PoiUtil(2, 4, 2, 2));
        list.add(new PoiUtil(6, 8, 1, 2));
        List<TableUtil> tableUtilList = new ArrayList<>();
        list.forEach((a) -> {
            // System.out.println(a.getFirstRow());
            //只跨行不跨列
            if (a.getFirstRow() < a.getLastRow() && a.getFirsColumn() == a.getLastColumn()) {
                System.out.println("1");
                for (int i = 0; i < (a.getLastRow() - a.getFirstRow()); i++) {
                    TableUtil t = new TableUtil();
                    t.setRowNumber(a.getFirstRow() + (i + 1));
                    t.setColumnNumber(a.getFirsColumn());
                    tableUtilList.add(t);
                }
            }
            //只跨列不跨行
            if (a.getFirstRow() == a.getLastRow() && a.getFirsColumn() < a.getLastColumn()) {
                int column = a.getLastColumn() - a.getFirsColumn();
                for (int i = 0; i < column; i++) {
                    TableUtil t = new TableUtil();
                    t.setRowNumber(a.getFirstRow());
                    t.setColumnNumber(a.getFirsColumn() + (i + 1));
                    tableUtilList.add(t);
                }
            }
            //既跨列也跨行
            if (a.getFirstRow() < a.getLastRow() && a.getFirsColumn() < a.getLastColumn()) {
                System.out.println("3");
                int crossRow = a.getLastRow() - a.getFirstRow();
                int crossColumn = a.getLastColumn() - a.getFirsColumn();
                for (int i = 0; i <= crossRow; i++) {
                    for (int j = 0; j <= crossColumn; j++) {
                        if (i == 0 && j == 0) {
                            continue;
                        }
                        TableUtil t = new TableUtil();
                        t.setRowNumber(a.getFirstRow() + (i));
                        t.setColumnNumber(a.getFirsColumn() + (j));
                        tableUtilList.add(t);
                    }
                }
            }


        });


    }

    public List<TableUtil> getDeleteData(List<PoiUtil> list) {

        List<TableUtil> tableUtilList = new ArrayList<>();
        list.forEach((a) -> {
            // System.out.println(a.getFirstRow());
            //只跨行不跨列
            if (a.getFirstRow() < a.getLastRow() && a.getFirsColumn() == a.getLastColumn()) {
                System.out.println("1");
                for (int i = 0; i < (a.getLastRow() - a.getFirstRow()); i++) {
                    TableUtil t = new TableUtil();
                    t.setRowNumber(a.getFirstRow() + (i + 1));
                    t.setColumnNumber(a.getFirsColumn());
                    tableUtilList.add(t);
                }
            }
            //只跨列不跨行
            if (a.getFirstRow() == a.getLastRow() && a.getFirsColumn() < a.getLastColumn()) {

                int column = a.getLastColumn() - a.getFirsColumn();
                for (int i = 0; i < column; i++) {
                    TableUtil t = new TableUtil();
                    t.setRowNumber(a.getFirstRow());
                    t.setColumnNumber(a.getFirsColumn() + (i + 1));
                    tableUtilList.add(t);
                }
            }
            //既跨列也跨行
            if (a.getFirstRow() < a.getLastRow() && a.getFirsColumn() < a.getLastColumn()) {
                System.out.println("3");
                int crossRow = a.getLastRow() - a.getFirstRow();
                int crossColumn = a.getLastColumn() - a.getFirsColumn();
                for (int i = 0; i <= crossRow; i++) {
                    for (int j = 0; j <= crossColumn; j++) {
                        if (i == 0 && j == 0) {
                            continue;
                        }
                        TableUtil t = new TableUtil();
                        t.setRowNumber(a.getFirstRow() + (i));
                        t.setColumnNumber(a.getFirsColumn() + (j));
                        //    System.out.println(a.getFirstRow()+(i)+" 列"+(a.getFirsColumn()+(j)));
                        tableUtilList.add(t);
                    }
                }
            }


        });

        tableUtilList.forEach((a -> {
            System.out.println("行号" + a.getRowNumber() + " 列号" + a.getColumnNumber());
        }));


        return tableUtilList;
    }
}

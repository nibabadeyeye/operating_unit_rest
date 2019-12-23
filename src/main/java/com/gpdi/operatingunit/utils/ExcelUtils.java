package com.gpdi.operatingunit.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Font;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.parameter.ExcelWriteParam;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description: esayExcel工具类
 * @Author: Lxq
 * @Date: 2019/10/22 9:12
 */
public class ExcelUtils {

    private static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private static Sheet initSheet;

    static {
        initSheet = new Sheet(1, 0);
        initSheet.setSheetName("sheet");
    }


    /**
     * 读取小于1000行数据，不带sheet
     *
     * @param filePath 文件的绝对路径
     * @return
     */
    public static List<Object> readLessThan1000Row(String filePath) {
        return readLessThan1000RowBySheet(filePath, null);
    }

    /**
     * 读小于1000行数据, 带sheet
     * filePath 文件绝对路径
     * initSheet ：
     * sheetNo: sheet页码，默认为1
     * headLineMun: 从第几行开始读取数据，默认为0, 表示从第一行开始读取
     * clazz: 返回数据List<Object> 中Object的类名
     */
    public static List<Object> readLessThan1000RowBySheet(String filePath, Sheet sheet) {
        if (!StringUtils.hasText(filePath)) {
            return null;
        }
        sheet = sheet != null ? sheet : initSheet;
        InputStream fileStream = null;
        try {
            fileStream = new FileInputStream(filePath);
            return EasyExcelFactory.read(fileStream, sheet);
        } catch (FileNotFoundException e) {
            log.info("找不到文件或文件路径错误, 文件：{}", filePath);
        } finally {
            try {
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                log.info("excel文件读取失败, 失败原因：{}", e);
            }
        }
        return null;
    }


    /**
     *  读小于1000行数据
     * @param inputStream 输入流
     * @param sheet
     * @return
     */
    public static List<Object> readLessThan1000RowBySheetAndStream(InputStream inputStream, Sheet sheet) {
        sheet = sheet != null ? sheet : initSheet;
        try {
            return EasyExcelFactory.read(inputStream, sheet);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.info("excel文件读取失败, 失败原因：{}", e);
            }
        }
    }

    /**
     * 读大于1000行数据,不带sheet
     *
     * @param filePath 文件绝对路径
     * @return
     */
    public static List<Object> readMoreThan1000Row(String filePath) {
        return readMoreThan1000RowBySheet(filePath, null);
    }


    /**
     * 读大于1000行数据, 带sheet
     *
     * @param filePath 文件绝对路径
     * @return
     */
    public static List<Object> readMoreThan1000RowBySheet(String filePath, Sheet sheet) {
        if (!StringUtils.hasText(filePath)) {
            return null;
        }

        sheet = sheet != null ? sheet : initSheet;

        InputStream fileStream = null;
        try {
            fileStream = new FileInputStream(filePath);
            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(fileStream, sheet, excelListener);
            return excelListener.getDatas();
        } catch (FileNotFoundException e) {
            log.error("找不到文件或文件路径错误, 文件：{}", filePath);
        } finally {
            try {
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                log.error("excel文件读取失败, 失败原因：{}", e);
            }
        }
        return null;
    }

    public static List<Object> readMoreThan1000RowBySheet(InputStream fileStream, Sheet sheet) {
        sheet = sheet != null ? sheet : initSheet;
        ExcelListener excelListener = new ExcelListener();
        EasyExcelFactory.readBySax(fileStream, sheet, excelListener);
        try {
            if (fileStream != null) {
                fileStream.close();
            }
        } catch (IOException e) {
            log.error("excel文件读取失败, 失败原因：{}", e);
        }
        return excelListener.getDatas();
    }


    /**
     * 生成excle
     *
     * @param data 数据源
     * @param head 表头
     */
    public static void writeBySimple(List<List<Object>> data, List<List<String>> head, HttpServletResponse response) {
        writeSimpleBySheetMoreTitle(data, head, null, response);
    }


    /**
     * 自定义多级表头导出
     *
     * @param data     数据源
     * @param head     表名
     * @param sheet    sheet名
     * @param response 响应流
     */
    public static void writeSimpleBySheetMoreTitle(List<List<Object>> data, List<List<String>> head, Sheet sheet, HttpServletResponse response) {
        sheet = (sheet != null) ? sheet : initSheet;
        if (head != null) {
            sheet.setHead(head);
            sheet.setTableStyle(createTableStyle());
        }
        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            setResponseHeader(response);
            outputStream = response.getOutputStream();
            writer = EasyExcelFactory.getWriter(outputStream);
            writer.write1(data, sheet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(writer, outputStream);
        }

    }

    /**
     * 生成excle
     *
     * @param data  数据源
     * @param sheet excle页面样式
     * @param head  表头
     */
    public static void writeSimpleBySheet(List<List<Object>> data, List<String> head, Sheet sheet, HttpServletResponse response) {
        sheet = (sheet != null) ? sheet : initSheet;
        if (head != null) {
            List<List<String>> list = new ArrayList<>();
            head.forEach(h -> list.add(Collections.singletonList(h)));
            sheet.setHead(list);
        }
        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            setResponseHeader(response);
            outputStream = response.getOutputStream();
            writer = EasyExcelFactory.getWriter(outputStream);
            writer.write1(data, sheet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(writer, outputStream);
        }

    }


    /**
     * 生成excle
     *
     * @param filePath 绝对路径, 如：/home/liuxiaoqiang/Downloads/aaa.xlsx
     * @param data     数据源
     * @param sheet    excle页面样式
     * @param head     表头
     */
    public static void writeSimpleBySheet(String filePath, List<List<Object>> data, List<List<String>> head, Sheet sheet) {
        sheet = (sheet != null) ? sheet : initSheet;
        if (head != null) {
            sheet.setHead(head);
            sheet.setTableStyle(createTableStyle());
        }
        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            outputStream = new FileOutputStream(filePath);
            writer = EasyExcelFactory.getWriter(outputStream);
            writer.write1(data, sheet);
        } catch (FileNotFoundException e) {
            log.error("找不到文件或文件路径错误, 文件：{}", filePath);
        } finally {
            closeStream(writer, outputStream);
        }

    }

    public static void writeSimpleBySheets(String filePath, List data, List<List<String>> head, Sheet sheet, List<SysCostItem> byOrgCodeAndReportIdColumn) {
        sheet = (sheet != null) ? sheet : initSheet;
        if (head != null) {
            sheet.setHead(head);
            sheet.setTableStyle(createTableStyle());
        }
        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            outputStream = new FileOutputStream(filePath);
            writer = EasyExcelFactory.getWriter(outputStream);
            //writer.write1(data, sheet);
            writer.write(data, sheet);
            // firstRow 第几行开始
            // lastRow 合并几行
            int count = 0;
            for(int i = 0;i<byOrgCodeAndReportIdColumn.size();i++){
                if(count == 0){
                    writer.merge(2,byOrgCodeAndReportIdColumn.get(i).getCount() + 1,0,0);
                    count += byOrgCodeAndReportIdColumn.get(i).getCount() + 2;
                }else{
                    int firstCol = count + byOrgCodeAndReportIdColumn.get(i).getCount() - 1;
                    writer.merge(count, firstCol,0,0);
                    count = firstCol + 1;
                }

            }

            //writer.merge(2,3,1,1);
        } catch (FileNotFoundException e) {
            log.error("找不到文件或文件路径错误, 文件：{}", filePath);
        } finally {
            closeStream(writer, outputStream);
        }

    }




    /**
     * 生成excle
     *
     * @param filePath 绝对路径, 如：/home/liuxiaoqiang/Downloads/aaa.xlsx
     * @param data     数据源
     */
    public static void writeWithTemplate(String filePath, List<? extends BaseRowModel> data) {
        writeWithTemplateAndSheet(filePath, data, null);
    }

    /**
     * 生成excle
     *
     * @param filePath 绝对路径, 如：/home/liuxiaoqiang/Downloads/aaa.xlsx
     * @param data     数据源
     * @param sheet    excle页面样式
     */
    public static void writeWithTemplateAndSheet(String filePath, List<? extends BaseRowModel> data, Sheet sheet) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        sheet = (sheet != null) ? sheet : initSheet;
        sheet.setClazz(data.get(0).getClass());

        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            outputStream = new FileOutputStream(filePath);
            writer = EasyExcelFactory.getWriter(outputStream);
            writer.write(data, sheet);
        } catch (FileNotFoundException e) {
            log.error("找不到文件或文件路径错误, 文件：{}", filePath);
        } finally {
            closeStream(writer, outputStream);
        }

    }


    /**
     * 数据流
     *
     * @param response
     */
    public static void setResponseHeader(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置表格样式
     *
     * @return
     */
    public static TableStyle createTableStyle() {
        TableStyle tableStyle = new TableStyle();
        // 设置表头样式
        Font headFont = new Font();
        // 字体是否加粗
        headFont.setBold(true);
        // 字体大小
        headFont.setFontHeightInPoints((short) 12);
        // 字体
        headFont.setFontName("楷体");
        tableStyle.setTableHeadFont(headFont);
        // 背景色
        // tableStyle.setTableHeadBackGroundColor(IndexedColors.BLUE);

        // 设置表格主体样式
        Font contentFont = new Font();
        contentFont.setBold(true);
        contentFont.setFontHeightInPoints((short) 12);
        contentFont.setFontName("黑体");
        tableStyle.setTableContentFont(contentFont);
        tableStyle.setTableContentBackGroundColor(IndexedColors.WHITE);
        return tableStyle;

    }


    public static void closeStream(ExcelWriter writer, OutputStream outputStream) {
        try {
            if (writer != null) {
                writer.finish();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            log.error("excel文件导出失败, 失败原因：{}", e);
        }
    }

}

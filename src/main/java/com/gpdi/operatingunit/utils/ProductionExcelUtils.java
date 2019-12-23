package com.gpdi.operatingunit.utils;

import com.gpdi.operatingunit.entity.reportconfig.RelOrgReportColumnEntity;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysReportColumn;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 生成excel工具类
 * @author: Lxq
 * @date: 2019/11/21 10:32
 */
public class ProductionExcelUtils {


    /**
     * @param list     表头数据
     * @param mapList  装配到excel数据
     * @param report   excel文件夹
     * @param fileName excel文件名称
     */
    public static void generalProductionExcelByMap(List<SysReportColumn> list, List<Map<Object, Object>> mapList, File report, String fileName) {
        // 获取字段属性名称
        List<String> column = getPropColumn(list);
        // excel表格数据
        List<List<Object>> data = new ArrayList<>();
        for (int i = 0; i < mapList.size(); i++) {
            List<Object> row = new ArrayList<>();
            for (String str : column) {
                Object ob = getMapValue(mapList.get(i), str);
                row.add(ob);
            }
            data.add(row);
            List<Map<Object, Object>> children = (List<Map<Object, Object>>) mapList.get(i).get("children");
            if (children != null) {
                if (children.size() > 0) {
                    for (int j = 0; j < children.size(); j++) {
                        List<Object> childRow = new ArrayList<>();
                        for (String childStr : column) {
                            Object childOb = getMapValue(children.get(j), childStr);
                            childRow.add(childOb);
                        }
                        data.add(childRow);
                    }
                }
            }
        }
        String url = report + File.separator + fileName;
        ExcelUtils.writeSimpleBySheet(url, data, createListStringHead(list), null);
    }

    /**
     *
     * @param titleData 表头源数据
     * @param typeAndCost 分类成本项目源数据
     * @param tableData 表格源数据
     * @param report excel文件夹
     * @param fileName excel文件名称
     */
    public static void countyProductExcel(List<Map<Object, Object>> titleData, List<SysCostItem> typeAndCost, List<SysOrganization> tableData ,File report, String fileName) {

        // 组装完成的导出表头数据
        List<List<String>> exportTitle = new ArrayList<>();
        // 数据对应的key
        List<String> dataKey = new ArrayList<>();
        // 分类和成本项目表头
        List<String> type = new ArrayList<>();
        type.add("分类");
        type.add("分类");
        List<String> cost = new ArrayList<>();
        cost.add("成本项目");
        cost.add("成本项目");
        exportTitle.add(type);
        exportTitle.add(cost);
        for (int i = 0; i < titleData.size(); i++) {
            List<Map<Object, Object>> tableData1 = (List<Map<Object, Object>>) titleData.get(i).get("tableData");
            for (int j = 0; j < tableData1.size(); j++) {
                List<String> list = new ArrayList<>();
                list.add((String) titleData.get(i).get("dataName"));
                list.add((String) tableData1.get(j).get("dataName"));
                exportTitle.add(list);
                if (i == 0) {
                    dataKey.add((String) tableData1.get(j).get("dataItem"));
                }
            }
        }

        List<List<Object>> exportTableData = new ArrayList<>();
        for (int i = 0; i < typeAndCost.size(); i++) {
            List<Object> list = new ArrayList<>();
            //分类
            list.add(typeAndCost.get(i).getCostType());
            //成本项目
            list.add(typeAndCost.get(i).getName());
            //表中的其他数据
            for (int j = 0; j < tableData.size(); j++) {
                List<RsCostReportEntity> childrensMonth = tableData.get(j).getChildrensMonth();
                for (int k = 0; k < dataKey.size(); k++) {
                    String value = getFieldValueByFieldName(dataKey.get(k), childrensMonth.get(i));
                    list.add(value);
                }
            }
            exportTableData.add(list);
        }

        String url = report + File.separator + fileName;
        ExcelUtils.writeSimpleBySheet(url, exportTableData, exportTitle, null);
    }


    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    private static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return  (String)field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 成本报表导出
     * @param list     表头数据
     * @param mapList  装配到excel数据
     * @param report   excel文件夹
     * @param fileName excel文件名称
     */
    public static void generalCostProductionExcelByMap(List<SysCostItem> list, List<Map<Object, Object>> mapList, File report, String fileName) {
        // 获取字段属性名称
        List<String> column = getPropColumns(list);
        // excel表格数据
        List<List<Object>> data = new ArrayList<>();
        for (int i = 0; i < mapList.size(); i++) {
            List<Object> row = new ArrayList<>();
            for (String str : column) {
                Object ob = getMapValues(mapList.get(i), str);
                row.add(ob);
            }
            data.add(row);
            List<Map<Object, Object>> children = (List<Map<Object, Object>>) mapList.get(i).get("children");
            if (children != null) {
                if (children.size() > 0) {

                    for (int j = 0; j < children.size(); j++) {
                        List<Object> childRow = new ArrayList<>();
                        for (String childStr : column) {
                            Object childOb = getMapValues(children.get(j), childStr);
                            childRow.add(childOb);
                        }
                        data.add(childRow);
                    }
                }
            }
        }
        String url = report + File.separator + fileName;
        ExcelUtils.writeSimpleBySheet(url, data, createListStringHeads(list), null);
    }

    /**
     * description: 划小报表导出
     *
     * @param list                          表头
     * @param mapList                       数据
     * @param byOrgCodeAndReportIdColumn    需要合并的列数据
     * @param report                        excel文件夹
     * @param fileName                      excel文件名称
     * @return void
     */
    public static void exportExcelOrganization(List<RelOrgReportColumnEntity> list, List<SysCostItem> mapList, List<SysCostItem> byOrgCodeAndReportIdColumn, File report, String fileName) {
        String url = report + File.separator + fileName;
        for(RelOrgReportColumnEntity relOrgReportColumnEntity : list){
            relOrgReportColumnEntity.getName();
            relOrgReportColumnEntity.getProp();
        }
        List<List<String>> head = new ArrayList<>();
        List<Object> datas = new ArrayList<>();
        for(SysCostItem sysCostItem1 : mapList) {
            List<Object> tempList = new ArrayList<>();
            RsCostReportEntity rsCostReportEntity = sysCostItem1.getItemsChildrens().get(0);
            tempList.add(sysCostItem1.getName());
            tempList.add(sysCostItem1.getCname());
            for(RelOrgReportColumnEntity relOrgReportColumnEntity : list){
                String prop = relOrgReportColumnEntity.getProp();
                if("currMonthValue".equals(prop)){
                    tempList.add(rsCostReportEntity.getCurrMonthValue());
                }else if ("budget".equals(prop)){
                    tempList.add(rsCostReportEntity.getBudget());
                }
                else if ("lastMonthValue".equals(prop)){
                    tempList.add(rsCostReportEntity.getLastMonthValue());
                }
                else if ("percLastYear".equals(prop)){
                    tempList.add(rsCostReportEntity.getPercLastYear());
                }
                else if ("diffLastMonth".equals(prop)){
                    tempList.add(rsCostReportEntity.getDiffLastMonth());
                }
                else if ("progress".equals(prop)){
                    tempList.add(rsCostReportEntity.getProgress());
                }
                else if ("currYearValue".equals(prop)){
                    tempList.add(rsCostReportEntity.getCurrYearValue());
                }
                else if ("percLastMonth".equals(prop)){
                    tempList.add(rsCostReportEntity.getPercLastMonth());
                }
                else if ("lastYearValue".equals(prop)){
                    tempList.add(rsCostReportEntity.getLastYearValue());
                }
                else if ("diffLastYear".equals(prop)){
                    tempList.add(rsCostReportEntity.getDiffLastYear());
                }
                else if ("remainingBudget".equals(prop)){
                    tempList.add(rsCostReportEntity.getRemainingBudget());
                }
            }
            datas.add(tempList);
        }

        for (int i = 0; i < list.size(); i++) {
            List<String> headCoulumn = new ArrayList<>();
            headCoulumn.add(list.get(i).getName());
            headCoulumn.add(list.get(i).getName());
            head.add(headCoulumn);
        }
        ExcelUtils.writeSimpleBySheets(url, datas, head, null,byOrgCodeAndReportIdColumn);
    }

    /**
     * 区成本报表导出
     * @param list     表头数据
     * @param mapList  装配到excel数据
     * @param report   excel文件夹
     * @param fileName excel文件名称
     */
    public static void generalCostReportProductionExcelByMap(List<SysCostItem> list, List<Map<Object, Object>> mapList, File report, String fileName) {
        // 获取字段属性名称
        List<String> column = getPropColumns(list);

        /*for(Map<Object,Object> map : mapList){
            Set<Object> objects = map.keySet();
            for(Object obj : objects){
                Object key = 5529L;
                System.out.println(map.get(key));
                System.out.println(map.get(obj));
            }
            break;
        }*/

        // excel表格数据
        List<List<Object>> data = new ArrayList<>();
        for (int i = 0; i < mapList.size(); i++) {
            List<Object> childRow = new ArrayList<>();
            for (String childStr : column) {
                Map<Object, Object> map = mapList.get(i);

                Set<Object> objects = map.keySet();
                System.out.println(map.get(childStr));
                for(Object obj : objects){
                    if(obj.toString().equals(childStr)){
                        childRow.add(map.get(obj));
                    }
                }
            }
            data.add(childRow);
        }
        String url = report + File.separator + fileName;
        ExcelUtils.writeSimpleBySheet(url, data, createListStringHeads(list), null);
    }


    public static RsCostReportEntity getRsCostReport(RsCostReportEntity rsCostReportEntity) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (rsCostReportEntity.getCurrMonthValue() == null) {
            rsCostReportEntity.setCurrMonthValue("0.0");
        }
        if (rsCostReportEntity.getLastMonthValue() == null) {
            rsCostReportEntity.setLastMonthValue("0.0");
        }
        if (rsCostReportEntity.getDiffLastMonth() == null) {
            rsCostReportEntity.setDiffLastMonth("0.0");
        }
        if (rsCostReportEntity.getCurrYearValue() == null) {
            rsCostReportEntity.setCurrYearValue("0.0");
        }

        if (rsCostReportEntity.getLastYearValue() == null) {
            rsCostReportEntity.setLastYearValue("0.0");
        }

        if (rsCostReportEntity.getDiffLastYear() == null) {
            rsCostReportEntity.setDiffLastYear("0.0");
        }

        if (rsCostReportEntity.getBudget() == null) {
            rsCostReportEntity.setBudget("0.0");
        }

        if (rsCostReportEntity.getProgress() == null) {
            rsCostReportEntity.setProgress("0.0");
        }

        if (rsCostReportEntity.getRemainingBudget() == null) {
            rsCostReportEntity.setRemainingBudget("0.0");
        }

        if (rsCostReportEntity.getPercLastMonth() == null) {
            rsCostReportEntity.setPercLastMonth("--");
        }
        if (rsCostReportEntity.getPercLastYear() == null) {
            rsCostReportEntity.setPercLastYear("--");
        }
        return rsCostReportEntity;
    }

    /**
     * 获取报表中有效的prop
     *
     * @param result
     * @return
     */
    private static List<String> getPropColumn(List<SysReportColumn> result) {
        // 获取字段属性名称
        List<String> column = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getChildren() == null) {
                column.add(result.get(i).getProp());
            } else {
                for (SysReportColumn reportColumns : result.get(i).getChildren()) {
                    column.add(reportColumns.getProp());
                }
            }
        }
        return column;
    }


    /**
     * 获取报表中有效的prop
     * cost模块
     * @param result
     * @return
     */
    private static List<String> getPropColumns(List<SysCostItem> result) {
        // 获取字段属性名称
        List<String> column = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getItemsChildren() == null) {
                if (result.get(i).getName().equals("营服中心")){
                    column.add("center");
                }else{
                    column.add(result.get(i).getId()+"");
                }
            } else {
                for (SysCostItem reportColumns : result.get(i).getItemsChildren()) {
                    column.add(reportColumns.getId()+"");
                }
            }
        }
        return column;
    }


    /**
     * @param map
     * @param key
     * @return
     */
    private static Object getMapValue(Map<Object, Object> map, String key) {
        return map.get(key);
    }

    /**
     * 成本相关的方法
     * @param map
     * @param key
     * @return
     */
    private static Object getMapValues(Map<Object, Object> map, String key) {
        if (key.equals("center")){
            return map.get(key);
        }else{
            return map.get(Long.valueOf(key));
        }
    }


    /**
     * 通用excel导出表头
     *
     * @param sysReportColumns
     * @return
     */
    public static List<List<String>> createListStringHead(List<SysReportColumn> sysReportColumns) {
        // 模型上没有注解，表头数据动态传入
        List<List<String>> head = new ArrayList<>();
        for (int i = 0; i < sysReportColumns.size(); i++) {
            if (sysReportColumns.get(i).getChildren() == null) {
                List<String> headCoulumn = new ArrayList<>();
                headCoulumn.add(sysReportColumns.get(i).getName());
                headCoulumn.add(sysReportColumns.get(i).getName());
                head.add(headCoulumn);
            } else {
                for (SysReportColumn reportColumns : sysReportColumns.get(i).getChildren()) {
                    List<String> headCoulumn = new ArrayList<>();
                    headCoulumn.add(sysReportColumns.get(i).getName());
                    headCoulumn.add(reportColumns.getName());
                    head.add(headCoulumn);
                }
            }
        }
        return head;
    }


    /**
     * 通用excel导出表头
     * cost模块
     * @param sysReportColumns
     * @return
     */
    public static List<List<String>> createListStringHeads(List<SysCostItem> sysReportColumns) {
        // 模型上没有注解，表头数据动态传入
        List<List<String>> head = new ArrayList<>();
        for (int i = 0; i < sysReportColumns.size(); i++) {
            if (sysReportColumns.get(i).getItemsChildren() == null) {
                List<String> headCoulumn = new ArrayList<>();
                headCoulumn.add(sysReportColumns.get(i).getName());
                headCoulumn.add(sysReportColumns.get(i).getName());
                head.add(headCoulumn);
            } else {
                for (SysCostItem reportColumns : sysReportColumns.get(i).getItemsChildren()) {
                    List<String> headCoulumn = new ArrayList<>();
                    headCoulumn.add(sysReportColumns.get(i).getName());
                    headCoulumn.add(reportColumns.getName());
                    head.add(headCoulumn);
                }
            }
        }
        return head;
    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @param filePath
     */
    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, String filePath) {
        ServletOutputStream out;
        // 要下载的文件绝对路径  
        File file = new File(filePath);
        try {
            setResponseHeader(request, response, file);
            FileInputStream inputStream = new FileInputStream(file);
            out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[1024];
            while (b != -1) {
                b = inputStream.read(buffer);
                //写到输出流(out)中
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置响应头
     *
     * @param request
     * @param response
     * @param file
     * @throws UnsupportedEncodingException
     */
    private static void setResponseHeader(HttpServletRequest request, HttpServletResponse response, File file) throws UnsupportedEncodingException {
        //设置响应头，控制浏览器下载该文件
        response.setContentType("multipart/form-data");
        //获得浏览器信息,并处理文件名
        String headerType = request.getHeader("User-Agent").toUpperCase();
        String fileName = null;
        if (headerType.indexOf("EDGE") > 0 || headerType.indexOf("MSIE") > 0 || headerType.indexOf("GECKO") > 0) {
            fileName = URLEncoder.encode(file.getName(), "UTF-8");
        } else {
            fileName = new String(file.getName().replaceAll(" ", "").getBytes("utf-8"), "iso8859-1");
        }
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + file.length());
    }

}

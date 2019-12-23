package com.gpdi.operatingunit.dao.originaltabledisplay;

import com.gpdi.operatingunit.controller.originaltabledisplay.DeletePara;
import com.gpdi.operatingunit.entity.originaltabledisplay.ExcelData;
import com.gpdi.operatingunit.entity.originaltabledisplay.ExcelMessage;


import java.util.List;

/**
 * @desc: created by whs on 2019/11/13
 *
 *    Excel操作工具类
 *
 */
public interface ExcelMapper {
    //获取Excel信息
    public List<ExcelData> getExcelData(int excelId);
    //查询表格信息
    public List<ExcelData> getExcelHeadData(int excelId);

    //查询表格标体信息
    public List<ExcelData> getExcelBodyData(int excelId);

    //批量插入数据
    public void addExcelList(List<ExcelData> list);

    //多条件查询Excel报表信息(月份、营服)
    public  List<ExcelMessage> getAListOfExcelMessage(String sql);

    //删除表格数据为空的记录
    public void deleteTableRecordWhichCoreDataIsNull();

    //插入一条ExcelMessage饰信息
    public void addExcelMessage(ExcelMessage excelMessage);


    //通过UUID查询ExcelID
    public int getExcelIdByUUID(String uuid);

    //获取分页的记录数
    public int getExcelNumber(String sql);
    //删除sql
    public void delExcelData(DeletePara deletePara);
}

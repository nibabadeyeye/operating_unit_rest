package com.gpdi.operatingunit.service.originaltabledisplay;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.controller.originaltabledisplay.DeletePara;
import com.gpdi.operatingunit.entity.originaltabledisplay.ExcelData;
import com.gpdi.operatingunit.entity.originaltabledisplay.ExcelMessage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @desc: created by whs on 2019/11/13
 * <p>
 * Excel操作工具类
 */

public interface ExcelService {
//    //获取Excel信息
//    public List<ExcelData> getExcelData(int excelId);
//
//    //查询表格信息
//    public List<ExcelData> getExcelHeadData(int excelId);
//
//    //查询表格标体信息
//    public List<ExcelData> getExcelBodyData(int excelId);
//
//    //批量插入数据
//    public void addExcelList(List<ExcelData> list);
//
//    //多条件查询Excel报表信息(月份、营服)
//    public List<ExcelMessage> getAListOfExcelMessage(String sql);
//
//    //删除表格数据为空的记录
//    public void deleteTableRecordWhichCoreDataIsNull();
//
//    //插入一条ExcelMessage饰信息
//    public void addExcelMessage(ExcelMessage excelMessage);
//
//    //通过UUID查询ExcelID
//    public int getExcelIdByUUID(String uuid);
//
//    //获取分页的记录数
//    public int getExcelNumber(String sql);
//
//    //删除sql
//    public void delExcelData(DeletePara deletePara);

    //文件上传（市、区、营服）
    public R uploadFile(MultipartFile file, HttpServletRequest request);

    //根据文件名称从服务器中下载Excel文件
    public R downLoadExcelFileFromServer(HttpServletRequest request, HttpServletResponse httpServletResponse, String fileName);

    //获取市的个性化报表数据
    public R getCityCostReport();

    //查询Excel表信息
    public R getUploadExcelMessage(int organizedCode,int level, int pageNumber, int pageSize);

    //查询Excel信息
    public R getListOfExcelMessage(int pageNumber, int pageSize);

    //通过id查询Excel数据
    public R getExcelDataById(int excelId);


}

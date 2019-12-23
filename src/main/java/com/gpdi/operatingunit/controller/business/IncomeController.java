package com.gpdi.operatingunit.controller.business;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.service.business.IncomeService;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ProductionExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
/**
 * @description: 收入展示报表
 * @author: Lxq
 * @date: 2019/11/19 10:36
 */
@RestController
@Api(description = "收入展示报表管理控制器类")
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @GetMapping("/getColumn")
    @ApiOperation("收入展示报表表头数据")
    public R getColumn() {
        List<SysReportColumn> sysReportColumns = incomeService.queryReportColumn();
        return R.ok(sysReportColumns);
    }

    /**
     * 获取最新月份
     *
     * @return
     */
    @GetMapping("/getNewMonth")
    public R getNewMonth() {
        List<Integer> list = incomeService.monthList();
        Object[] objects = list.stream().toArray();
        return R.ok(objects);
    }

    @PostMapping("/getTableData")
    @ApiOperation("收入报表数据")
    public R getTableData(Integer time) {
        List<Map<Object, Object>> mapList = incomeService.queryIncomeData(time);
        return R.ok(mapList);
    }

    @PostMapping("downLoadExcel")
    public R downLoadExcel(HttpServletRequest request, HttpServletResponse response, Integer month){
        try {
            File report = new File("report");
            //如果文件夹不存在
            if (!report.exists()) {
                //创建文件夹
                report.mkdir();
            }
            List<Map<Object, Object>> excelData = incomeService.queryIncomeData(month);
            List<SysReportColumn> reportTitleColumn = incomeService.queryReportColumn();
            // 生成excel
            ProductionExcelUtils.generalProductionExcelByMap(reportTitleColumn, excelData,report,"收入展示报表" + month + ".xlsx");
            File file = new File(report + File.separator + "收入展示报表" + month + ".xlsx");
            ProductionExcelUtils.downLoadFile(request,response, String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

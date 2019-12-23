package com.gpdi.operatingunit.controller.evaluation;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.service.data.QueryData;
import com.gpdi.operatingunit.service.evaluation.BenefitsService;
import com.gpdi.operatingunit.utils.ProductionExcelUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zyb
 * @Date: 2019/11/15 9:37
 */
@RestController
@RequestMapping("/benefits")
public class BenefitsController {
    @Autowired
    private BenefitsService benefitsService;


    @RequestMapping(value = "/getMonth",method = {RequestMethod.POST})
    @ApiOperation("获取当前数据库数据最大月份")
    public R getMonth(QueryData queryData){
        Map<String,Object> map = new HashMap<>();
        List<SysOrganization> citys = benefitsService.getCitys();
        map.put("cityList",citys);
        map.put("monthList",benefitsService.getMonth(queryData.getReportId(),citys.get(0)));
        return R.ok(map);
    }

    @RequestMapping(value = "/getBenefitsList",method = {RequestMethod.POST})
    @ApiOperation("获取渠道效益报表数据")
    public R getBenefitsList(QueryData queryData,Long topOrgCode){
        String str = benefitsService.ifExist(queryData,topOrgCode);
        if (str.equals("fail")){
            return R.ok(str);
        }
        String code = ShiroUtils.getUser().getOrgCode().toString();
        Map<String,Object> map = new HashMap<>();
        map.put("column",benefitsService.getColumns(queryData.getReportId(),topOrgCode));
        map.put("tableData",benefitsService.getTableData(queryData,code,topOrgCode));
        return R.ok(map);
    }

    @RequestMapping(value = "/exportExcel",method = {RequestMethod.POST})
    @ApiOperation("导出excel")
    public R exportExcel(HttpServletRequest request, HttpServletResponse response,QueryData queryData,Long topOrgCode){
        File file = null;
        try {
            File report = new File("report");
            //如果文件夹不存在
            if (!report.exists()) {
                //创建文件夹
                report.mkdir();
            }
            String code = ShiroUtils.getUser().getOrgCode().toString();
            List<Map<Object, Object>> excelData = benefitsService.getTableData(queryData,code,topOrgCode);
            List<SysReportColumn> reportTitleColumn = benefitsService.getColumns(queryData.getReportId(),topOrgCode);
            // 生成excel
            ProductionExcelUtils.generalProductionExcelByMap(reportTitleColumn, excelData,report,"渠道效益报表" + queryData.getMonth() + ".xlsx");
            file = new File(report + File.separator + "渠道效益报表" + queryData.getMonth() + ".xlsx");
            ProductionExcelUtils.downLoadFile(request,response, String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok(file);
    }
}

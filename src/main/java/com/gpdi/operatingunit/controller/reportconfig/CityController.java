package com.gpdi.operatingunit.controller.reportconfig;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.service.data.QueryData;
import com.gpdi.operatingunit.service.reportconfig.CityService;
import com.gpdi.operatingunit.utils.DataGenerateUtils;
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
 * @Date: 2019/11/19 17:55
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;
    @Autowired
    private DataGenerateUtils dataGenerateUtils;

    @RequestMapping(value = "/getMonth",method = {RequestMethod.POST})
    @ApiOperation("获取市报表的月份及城市")
    public R getMonth(QueryData queryData){
        Map<String,Object> map = new HashMap<>();
        List<SysOrganization> citys = cityService.getCitys();
        map.put("cityList",citys);
        Integer maxMonth = cityService.getMaxMonth(queryData.getReportId(), citys.get(0).getTopOrgCode());
        map.put("maxMonth",maxMonth);
        map.put("minMonth",cityService.getMinMonth(queryData.getReportId(),citys.get(0).getTopOrgCode(),(maxMonth/100)+"%"));
        map.put("monthList",cityService.getMonthList(queryData.getReportId(), citys.get(0).getTopOrgCode()));
        return R.ok(map);
    }

    @RequestMapping(value = "/getData",method = {RequestMethod.POST})
    @ApiOperation("获取市报表的数据")
    public R getData(QueryData queryData,Long topOrgCode){
        Map<String,Object> map = new HashMap<>();
        map.put("column",cityService.getColumns(queryData.getReportId(),topOrgCode));
        map.put("data",cityService.getData(queryData,topOrgCode));
        return R.ok(map);
    }

    @RequestMapping(value = "/getSapList",method = {RequestMethod.POST})
    @ApiOperation("获取sap详情清单")
    public R getSapList(Long itemId,Integer start,Integer end,Long orgCode,Long topOrgCode,Integer reportId){
        return R.ok(dataGenerateUtils.getSapList(itemId,start,end,orgCode,topOrgCode,reportId));
    }

    @RequestMapping(value = "/exportExcel",method = {RequestMethod.POST})
    @ApiOperation("导出excel")
    public R exportExcel(HttpServletRequest request, HttpServletResponse response, QueryData queryData, Long topOrgCode){
        File file = null;
        try {
            File report = new File("report");
            //如果文件夹不存在
            if (!report.exists()) {
                //创建文件夹
                report.mkdir();
            }
            List<Map<Object, Object>> excelData = cityService.getData(queryData,topOrgCode);
            List<SysCostItem> reportTitleColumn = cityService.getColumns(queryData.getReportId(),topOrgCode);
            SysCostItem item = new SysCostItem();
            item.setName("营服中心");
            reportTitleColumn.add(0,item);
            // 生成excel
            ProductionExcelUtils.generalCostProductionExcelByMap(reportTitleColumn, excelData,report,"市成本报表" + queryData.getFromMonth()+" - "+queryData.getToMonth() + ".xlsx");
            file = new File(report + File.separator + "市成本报表" + queryData.getFromMonth()+" - "+queryData.getToMonth() + ".xlsx");
            ProductionExcelUtils.downLoadFile(request,response, String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok(file);
    }
}

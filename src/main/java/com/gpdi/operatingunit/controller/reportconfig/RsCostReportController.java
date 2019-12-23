package com.gpdi.operatingunit.controller.reportconfig;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.reportconfig.RelOrgReportColumnEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysRole;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.data.QueryData;
import com.gpdi.operatingunit.service.reportconfig.*;
import com.gpdi.operatingunit.utils.ProductionExcelUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
 * 成本报表数据结果表
 *
 * @author long
 * @date 2019-10-29 21:57:40
 */
@RestController
@RequestMapping("rsCostReportController")
public class RsCostReportController {

    @Autowired
    private RsCostReportService rsCostReportService;

    @Autowired
    private SysCostItemService sysCostItemService;

    @Autowired
    private CostConfigService configService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RelOrgReportColumnService relOrgReportColumnService;

    /**
     * description: 划小
     *
     * @param level
     * @param parentCode
     * @param reportId
     * @return com.gpdi.operatingunit.config.R
     */
    @PostMapping("/getOrganization")
    @ApiOperation("成本报表下拉联动")
    public R getOrganization(String level, Long parentCode, Long reportId) {
        Map<String, Object> map = rsCostReportService.getOrganization(level, parentCode, reportId);
        return R.ok(map);
    }

    /**
     * description: 区成本
     *
     * @param level
     * @param parentCode
     * @param reportId
     * @return com.gpdi.operatingunit.config.R
     */
    @PostMapping("/getCountyOrganization")
    @ApiOperation("成本报表下拉联动")
    public R getCountyOrganization(String level, Long parentCode, Long reportId) {
        Map<String, Object> map = rsCostReportService.getCountyOrganization(level, parentCode, reportId);
        return R.ok(map);
    }

    /**
     * description: 区县
     *
     * @param level
     * @param parentCode
     * @param reportId
     * @return com.gpdi.operatingunit.config.R
     */
    @PostMapping("/getCounty")
    @ApiOperation("成本报表下拉联动")
    public R getCounty(String level, Long parentCode, Long reportId) {
        Map<String, Object> map = rsCostReportService.getCounty(level, parentCode, reportId);
        return R.ok(map);
    }

    /**
     * description: 获取区成本数据
     *
     * @param
     * @return com.gpdi.operatingunit.config.R
     */
    @PostMapping("/getCountyData")
    public R getCountyData(QueryData queryData, Long topOrgCode, Long code) {
        Map<String, Object> map = new HashMap<>();
        List<Map<Object, Object>> data = rsCostReportService.getData(queryData, topOrgCode, code);
        map.put("data", data);

        List<SysCostItem> columns = cityService.getColumns(queryData.getReportId(), topOrgCode);
        SysCostItem costItems = new SysCostItem();
        costItems.setName("营服中心");
        costItems.setId(-0L);
        costItems.setLevel(1);
        columns.add(0,costItems);
        map.put("tableMap",columns);
        // 营服中心下的数据
        Map<String, Object> colmap = rsCostReportService.getCountyOrganization("3", topOrgCode, Long.valueOf(queryData.getReportId().toString()));
        map.put("colmap",colmap);
        return R.ok(map);
    }

    @RequestMapping(value = "/exportCountyExcel", method = {RequestMethod.POST})
    @ApiOperation("导出excel（区成本）")
    public R exportCountyExcel(HttpServletRequest request, HttpServletResponse response, QueryData queryData, Long topOrgCode, Long code) {
        File file = null;
        try {
            File report = new File("report");
            //如果文件夹不存在
            if (!report.exists()) {
                //创建文件夹
                report.mkdir();
            }
            List<Map<Object, Object>> excelData = rsCostReportService.getData(queryData, topOrgCode, code);
            List<SysCostItem> reportTitleColumn = cityService.getColumns(queryData.getReportId(), topOrgCode);
            SysCostItem costItems = new SysCostItem();
            costItems.setName("营服中心");
            costItems.setId(-0L);
            costItems.setLevel(1);
            reportTitleColumn.add(0, costItems);


            /*SysCostItem item = new SysCostItem();
            item.setName("合计");
            reportTitleColumn.add(0,item);*/
            // 生成excel
            ProductionExcelUtils.generalCostReportProductionExcelByMap(reportTitleColumn, excelData, report, "区成本报表" + queryData.getFromMonth() + " - " + queryData.getToMonth() + ".xlsx");
            file = new File(report + File.separator + "区成本报表" + queryData.getFromMonth() + " - " + queryData.getToMonth() + ".xlsx");
            ProductionExcelUtils.downLoadFile(request, response, String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok(file);
    }


    /*public R getCountyData(Long reportId,Long month,Long endMonth,Long topOrgCode, Long code){
        //Long userTopOrgCode = ShiroUtils.getUser().getTopOrgCode();

        List<SysOrganization> countyData = rsCostReportService.getCountyData(reportId,month,endMonth,topOrgCode,code);
        return R.ok(countyData);
    }*/

    @PostMapping("/getCostItem")
    @ApiOperation("获取划小一级分类和成本项目")
    public R getCostItem(Long reportId) {

        List<SysCostItem> list = sysCostItemService.getCostItem(reportId, 0L);
        return R.ok("list", list);
    }

    @PostMapping("/getCostItemData")
    @ApiOperation("获取划小对应成本项目下的数据")
    public R getCostItemData(Long reportId, Long code, Long beginMonth, Long endMonth, Long orgCode) {

        List<SysCostItem> list = sysCostItemService.getCostItemData(reportId, code, beginMonth, endMonth, orgCode);
        return R.ok("list", list);
    }

    @PostMapping("/getCostItemCountyData")
    @ApiOperation("获取区县对应成本项目下的数据")
    public R getCostItemCountyData(Long reportId, Long code, Long month, Long orgCode) {
        List<SysCostItem> list = sysCostItemService.getCostItemCountyData(reportId, code, month, orgCode);
        return R.ok("list", list);
    }

    @PostMapping("/getCity")
    @ApiOperation("获取城市")
    public R getCity() {
        List<SysOrganization> city = sysCostItemService.getCity();
        return R.ok(city);
    }

    @PostMapping("/getAllCostItemCountyData")
    @ApiOperation("获取所有区县对应成本项目下的数据")
    public R getAllCostItemCountyData(Long reportId, Long month, Long topOrgCode, Long orgCode) {
        List<SysOrganization> list = sysCostItemService.getAllCostItemCountyData(reportId, month, topOrgCode, orgCode);
        List<SysCostItem> table = sysCostItemService.getCostItemCountyData(reportId, orgCode, month, topOrgCode);
        List<Map<Object, Object>> maps = relOrgReportColumnService.queryAllByReportIdAndTopOrgCode("3", reportId, topOrgCode, orgCode);

        Map<Object, Object> map = new HashMap<>();
        // 表格数据
        map.put("list", list);
        // 需要显示的成本项目
        map.put("table", table);
        // 表头
        map.put("maps", maps);
        return R.ok(map);
    }

    @PostMapping("/countyExportExcel")
    public R countyExportExcel(HttpServletRequest request, HttpServletResponse response, Long reportId, Long month, Long topOrgCode, Long orgCode) {
        try {
            File report = new File("report");
            //如果文件夹不存在
            if (!report.exists()) {
                //创建文件夹
                report.mkdir();
            }
            //表头数据
            List<Map<Object, Object>> titleData = relOrgReportColumnService.queryAllByReportIdAndTopOrgCode("3", reportId, topOrgCode, orgCode);
            // 分类和成本的数据
            List<SysCostItem> typeAndCost = sysCostItemService.getCostItemCountyData(reportId, orgCode, month, topOrgCode);
            // 表中的数据
            List<SysOrganization> tableData = sysCostItemService.getAllCostItemCountyData(reportId, month, topOrgCode, orgCode);
            // 生成excel
            ProductionExcelUtils.countyProductExcel(titleData, typeAndCost, tableData, report, "区县报表" + month + ".xlsx");
            File file = new File(report + File.separator + "区县报表" + month + ".xlsx");
            ProductionExcelUtils.downLoadFile(request, response, String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;

    }


    @PostMapping("/getCityCostItem")
    @ApiOperation("获取市分类")
    public R getCityCostItem(Long reportId) {
        List<SysCostItem> list = sysCostItemService.getCityCostItem(reportId);
        return R.ok("list", list);
    }

    @PostMapping("/getCityCostItemData")
    @ApiOperation("获取对应营服中兴下的数据")
    public R getCityCostItemData(String level, Long parentCode, Long reportId, Long beginMonth, Long endMonth, Long orgCode) {
        Map<String, Object> list = sysCostItemService.getCityCostItemData(level, parentCode, reportId, beginMonth, endMonth, orgCode);
        return R.ok(list);
    }

    /**
     * description: 区
     *
     * @param level
     * @param parentCode
     * @return com.gpdi.operatingunit.config.R
     */
    @PostMapping("/getDistrictOrganization")
    @ApiOperation("成本报表下拉")
    public R getDistrictOrganization(String level, Long parentCode) {
        List<SysOrganization> list = rsCostReportService.getDistrictOrganization(level, parentCode);
        return R.ok(list);
    }

    @RequestMapping(value = "/getCountryItem", method = {RequestMethod.POST})
    @ApiOperation("获取区县报表")
    public R getCountyItem(Integer reportId) {
        SysUser user = ShiroUtils.getUser();
        return R.ok(rsCostReportService.getCountyItem(reportId, user.getTopOrgCode()));
    }

    @RequestMapping(value = "/getTopOrgCode", method = {RequestMethod.POST})
    @ApiOperation("获取当前用户编码")
    public R getTopOrgCode() {
        SysUser user = ShiroUtils.getUser();
        List<SysRole> sysRoles = relOrgReportColumnService.queryById(user.getId());
        return R.ok("list", sysRoles);
    }

    @RequestMapping(value = "/getByReportIdAndTopOrgCode", method = {RequestMethod.POST})
    @ApiOperation("获取划小与区县需要展示的字段")
    public R getByReportIdAndTopOrgCode(Long reportId, Long topOrgCode) {
        List<RelOrgReportColumnEntity> relOrgReportColumnEntities = relOrgReportColumnService.queryByReportIdAndTopOrgCode(reportId, topOrgCode);
        return R.ok(relOrgReportColumnEntities);
    }

    @RequestMapping(value = "/getAllByReportIdAndTopOrgCode", method = {RequestMethod.POST})
    @ApiOperation("获取区县表头")
    public R getAllByReportIdAndTopOrgCode(Long reportId, Long topOrgCode, Long orgCode) {
        List<Map<Object, Object>> maps = relOrgReportColumnService.queryAllByReportIdAndTopOrgCode("3", reportId, topOrgCode, orgCode);
        return R.ok(maps);
    }

    @RequestMapping(value = "/getCountyDataTable", method = {RequestMethod.POST})
    public R getCountyDataTable(QueryData queryData, Long topOrgCode) {
        Map<String, Object> map = new HashMap<>();
        List<SysCostItem> columns = cityService.getColumns(queryData.getReportId(), topOrgCode);
        SysCostItem costItems = new SysCostItem();
        costItems.setName("营服中心");
        costItems.setId(-0L);
        costItems.setLevel(1);
        columns.add(0, costItems);
        return R.ok(columns);
    }


    @RequestMapping(value = "/exportExcel", method = {RequestMethod.POST})
    @ApiOperation("导出excel")
    public R exportExcel(HttpServletRequest request, HttpServletResponse response, Long reportId, Long orgCode, Long time, Long topOrgCode) {
        File file = null;
        try {
            File report = new File("report");
            //如果文件夹不存在
            if (!report.exists()) {
                //创建文件夹
                report.mkdir();
            }
            List<SysCostItem> excelData = sysCostItemService.getCostItemData(reportId, orgCode, time, time, topOrgCode);
            List<SysCostItem> byOrgCodeAndReportIdColumn = sysCostItemService.getByOrgCodeAndReportIdColumn(reportId, topOrgCode);
            // 字段
            List<RelOrgReportColumnEntity> reportTitleColumn = relOrgReportColumnService.queryByReportIdAndTopOrgCode(reportId, topOrgCode);
            RelOrgReportColumnEntity relOrgReportColumnEntity = new RelOrgReportColumnEntity();
            relOrgReportColumnEntity.setProp("fenlei");
            relOrgReportColumnEntity.setName("分类");
            reportTitleColumn.add(0, relOrgReportColumnEntity);
            RelOrgReportColumnEntity relOrgReportColumnEntity2 = new RelOrgReportColumnEntity();
            relOrgReportColumnEntity2.setProp("chengben");
            relOrgReportColumnEntity2.setName("成本项目");
            reportTitleColumn.add(1, relOrgReportColumnEntity2);
            // 生成excel
            ProductionExcelUtils.exportExcelOrganization(reportTitleColumn, excelData, byOrgCodeAndReportIdColumn, report, time + "营服成本报表" + ".xlsx");
            file = new File(report + File.separator + time + "营服成本报表" + ".xlsx");
            ProductionExcelUtils.downLoadFile(request, response, String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok(file);
    }

}

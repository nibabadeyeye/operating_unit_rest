package com.gpdi.operatingunit.controller.costreport;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.service.costreport.ReportShowService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/reportShow")//导入成本收入数据:reportShow/uploadCostIncome
@ApiOperation("/个性化报表展示")
public class ReportShowController {

    @ApiOperation("/成本环形表(图一)")
    @RequestMapping("/costRingTable")
    public R getCostRingTable(int code, int month) {
        return reportShowService.getCostRingTable(code, month);
    }


    @ApiOperation("/成本额度使用(图二)")
    @RequestMapping("/getCostTimeSchedule")
    public R getCostLimit(String code, String month) {
        return reportShowService.getCostLimit(code, month);

    }

    @ApiOperation("/成本占比柱状表（图三）")
    @RequestMapping("/getCostBarChat")
    public R getCostBarChat(int code, int month) {
        return reportShowService.getCostBarChat(code, month);

    }

    @ApiOperation("/成本时间安排（图四）")
    @RequestMapping("/getCostTimeScheduleFour")
    public R getCostTimeScheduleFour(String code, String month) {
        return reportShowService.getCostTimeSchedule(code, month);

    }

    @ApiOperation("/导入成本收入数据")
    @RequestMapping("/uploadCostIncome")
    @Transactional
    public R uploadCostIncome(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return reportShowService.importCostIncomeList(file.getInputStream());
    }


    @ApiOperation("/导入成本时间进度数据")
    @RequestMapping("/uploadCostIndex")
    @Transactional
    public R uploadCostIndex(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return reportShowService.importCostIndexList(file.getInputStream());
    }

    @ApiOperation("/导入坏账数据")
    @RequestMapping("/uploadBadCost")
    @Transactional
    public R uploadBadCost(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return reportShowService.importBadCostList(file.getInputStream());
    }


    @ApiOperation("/查询阳江的营服信息")
    @RequestMapping("/initYangJiangInput")
    public R initYangJiangInput() {
        return reportShowService.initYangJiangOrganizedData();
    }


    @Autowired
    private ReportShowService reportShowService;


}

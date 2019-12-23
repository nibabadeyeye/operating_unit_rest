package com.gpdi.operatingunit.service.costreport;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.costreport.BadCost;
import com.gpdi.operatingunit.entity.costreport.CostIndex;
import com.gpdi.operatingunit.entity.costreport.MonthIncome;
import com.gpdi.operatingunit.entity.costreport.ReportShow;
import com.gpdi.operatingunit.entity.originaltabledisplay.ExcelData;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysRole;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Desc :个性化报表展示
 * @author: whs
 */
public interface ReportShowService {

    //图一（13项成本环形图）
    public R getCostRingTable(int code, int month);


    //图二（成本额度使用）
    public R getCostLimit(String code, String month);

    //图三（13项成本柱形图）
    public R getCostBarChat(int code, int month);

    //图四（成本时间进度）
    public R getCostTimeSchedule(String code, String month);

    //导入坏账
    public R importBadCostList(InputStream inputStream);

    //导入月收入进度
    public R importCostIndexList(InputStream inputStream);

    //导入成本数据
    public R importCostIncomeList(InputStream inputStream);

    //初始化阳江组织结构数据
    public R initYangJiangOrganizedData();


}

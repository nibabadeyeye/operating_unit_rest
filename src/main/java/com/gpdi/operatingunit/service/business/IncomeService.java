package com.gpdi.operatingunit.service.business;

import com.gpdi.operatingunit.entity.system.SysReportColumn;

import java.util.List;
import java.util.Map;

/**
 * @description: 收入展示报表
 * @author: Lxq
 * @date: 2019/11/19 10:54
 */
public interface IncomeService {

    /**
     * 收入展示报表表头数据
     *
     * @return
     */
    List<SysReportColumn> queryReportColumn();

    /**
     * 获取收入展示报表最新月份数据
     * @return
     */
    int maxMonth();

    /**
     * 获取收入展示报表表数据
     * @param month
     * @return
     */
    List<Map<Object,Object>> queryIncomeData(Integer month);

    /**
     * 获取收入展示报表月份倒序集合
     * @return
     */
    List<Integer> monthList();
}

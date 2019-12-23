package com.gpdi.operatingunit.service.business;

import com.gpdi.operatingunit.entity.business.DataDevelopment;
import com.gpdi.operatingunit.entity.system.SysReportColumn;

import java.util.List;
import java.util.Map;

/**
 * @description: 业务量展示报表
 * @author: Lxq
 * @date: 2019/11/18 14:38
 */
public interface DevelopmentService {


    /**
     * 导入Excel List集合
     *
     * @param list
     */
    void insertExcelList(List<DataDevelopment> list);


    /**
     * 业务量展示报表表头数据
     *
     * @return
     */
    List<SysReportColumn> queryReportColumn();

    /**
     * 获取业务量展示报表最新月份数据
     *
     * @return
     */
    int maxMonth();

    /**
     * 获取业务量展示报表表数据
     *
     * @param month
     * @return
     */
    List<Map<Object, Object>> queryDevelopmentData(Integer month);

    /**
     * 获取业务量发展报表月份倒序集合
     * @return
     */
    List<Integer> monthList();

}

package com.gpdi.operatingunit.service.reportconfig;

import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.service.data.QueryData;

import java.util.List;
import java.util.Map;

/**
 * @Author: zyb
 * @Date: 2019/11/19 17:56
 */
public interface CityService {

    /**
     * 获取城市集合
     * @return
     */
    List<SysOrganization> getCitys();

    /**
     * 获取表头
     * @param reportId
     * @param topOrgCode
     * @return
     */
    List<SysCostItem> getColumns(Integer reportId, Long topOrgCode);

    /**
     * 获取市报表数据
     * @param queryData
     * @param topOrgCode
     * @return
     */
    List<Map<Object,Object>> getData(QueryData queryData, Long topOrgCode);

    /**
     * 获取市报表的最大月份
     * @param reportId
     * @param topOrgCode
     * @return
     */
    Integer getMaxMonth(Integer reportId, Long topOrgCode);

    /**
     * 获取最小月份
     * @param reportId
     * @param topOrgCode
     * @param mon
     * @return
     */
    Integer getMinMonth(Integer reportId, Long topOrgCode, String mon);

    /**
     * 获取月份集合
     * @param reportId
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getMonthList(Integer reportId, Long topOrgCode);
}

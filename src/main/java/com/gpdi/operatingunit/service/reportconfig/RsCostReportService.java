package com.gpdi.operatingunit.service.reportconfig;

import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.service.data.QueryData;

import java.util.List;
import java.util.Map;

/**
 * @Author：long
 * @Date：2019/10/30 10:03
 * @Description：
 */
public interface RsCostReportService {

    /**
     * description: 三级联动（营服，片区）
     *
     * @param level
     * @param parentCode
     * @param reportId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> getOrganization(String level, Long parentCode, Long reportId);

    /**
     * description: 区成本报表
     *
     * @param level
     * @param parentCode
     * @param reportId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> getCountyOrganization(String level, Long parentCode, Long reportId);

    /**
     * description: 区县
     *
     * @param level
     * @param parentCode
     * @param reportId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> getCounty(String level, Long parentCode, Long reportId);


    /**
     * description: 获取区成本数据
     *
     * @param reportId
     * @param month
     * @param endMonth
     * @param orgCode
     * @param code
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysOrganization>
     */
    List<SysOrganization> getCountyData(Long reportId,Long month,Long endMonth,Long orgCode, Long code);

    List<Map<Object, Object>> getData(QueryData queryData, Long topOrgCode, Long code);

    /**
     * description: 区下拉框获取
     *
     * @param level
     * @param parentCode
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysOrganization>
     */
    List<SysOrganization> getDistrictOrganization(String level, Long parentCode);

    List<SysCostItem> getCountyItem(Integer reportId, Long orgCode);
}

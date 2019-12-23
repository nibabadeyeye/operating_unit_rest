package com.gpdi.operatingunit.service.reportconfig;

import com.gpdi.operatingunit.config.R;

import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/10/25 14:55
 */
public interface SysCostItemService {

    /**
     * 区县报表成本数据导入
     *
     * @param list     excel数据
     * @param reportId 报表id
     * @return R 结果
     */
    R countyUpload(List<Object> list, Integer reportId);

    /**
     * 成本项目导入
     *
     * @param list     excel数据
     * @param reportId 报表id
     * @return R 结果
     */
    R costItemUpload(List<Object> list, Integer reportId);

    /**
     * description: 获取一级分类和成本项目
     *
     * @param reportId  报表id
     * @param orgCode  组织编码
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getCostItem(Long reportId, Long orgCode);

    /**
     * description: 获取一级分类和成本项目（市）
     *
     * @param reportId  报表id
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getCityCostItem(Long reportId);

    /**
     * description: 获取对应营服中兴下的数据
     *
     * @param level         层级
     * @param parentCode    父级编码
     * @param reportId      报表id
     * @param beginMonth    开始月份
     * @param endMonth      结束月份
     * @param orgCode      组织编码
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> getCityCostItemData(String level, Long parentCode, Long reportId, Long beginMonth, Long endMonth, Long orgCode);

    /**
     * description: 获取划小对应成本项目下的数据
     *
     * @param reportId      报表id
     * @param code          组织编码
     * @param beginMonth    开始月份
     * @param endMonth      结束月份
     * @param orgCode       组织编码
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */

    List<SysCostItem> getCostItemData(Long reportId, Long code, Long beginMonth, Long endMonth, Long orgCode);

    /**
     * description: 获取分类下的子项数量
     *
     * @param reportId
     * @param topOrgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getByOrgCodeAndReportIdColumn(Long reportId, Long topOrgCode);

    /**
     * description: 获取对应成本项目下的数据（区县）
     *
     * @param reportId  报表id
     * @param code     组织编码
     * @param orgCode 顶级组织编码
     * @param month   时间
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getCostItemCountyData(Long reportId, Long code, Long month, Long orgCode);

    /**
     * description: 获取所有区县对应成本项目下的数据
     *
     * @param reportId
     * @param month
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysOrganization>
     */
    List<SysOrganization> getAllCostItemCountyData(Long reportId, Long month,Long topOrgCode, Long orgCode);

    /**
     * description: 获取城市
     *
     * @param
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysOrganization>
     */
    List<SysOrganization> getCity();
}

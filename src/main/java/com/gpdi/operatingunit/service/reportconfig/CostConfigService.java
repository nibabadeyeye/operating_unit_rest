package com.gpdi.operatingunit.service.reportconfig;

import com.gpdi.operatingunit.entity.common.DataSapList;
import com.gpdi.operatingunit.entity.common.SapList;
import com.gpdi.operatingunit.entity.relation.RelOrgReportColumn;
import com.gpdi.operatingunit.entity.reportconfig.DataSapListEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.RelSapCostItemSubitem;
import com.gpdi.operatingunit.entity.system.SysDict;
import com.gpdi.operatingunit.entity.system.SysSapSubject;
import com.gpdi.operatingunit.entity.system.SysUser;

import java.util.List;

/**
 * @Author: zyb
 * @Date: 2019/10/25 11:34
 */
public interface CostConfigService {

    /**
     * 获取成本表的费用类别
     *
     * @param reportId
     * @param orgCode
     * @return
     */
    List<SysCostItem> getCostItem(Integer reportId, Long orgCode);

    /**
     * 获取sap的字段名
     *
     * @return
     */
    List<SapList> getSapNameList();

    /**
     * 获取选择条件
     *
     * @return
     */
    List<SysDict> getSelections();

    /**
     * 获取提供绑定的成本项
     *
     * @param id
     * @param reportId
     * @return
     */
    List<SysSapSubject> getSapSubject(Integer id, Integer reportId);

    /**
     * 获取已经绑定的成本项
     *
     * @param sid
     * @param orgCode
     * @param year
     * @param sapCode
     * @param reportId
     * @return
     */
    List<RelSapCostItemSubitem> getRelSapCostItemSubmitemList(Long sid, Long orgCode, Integer year, Long sapCode, Integer reportId);

    /**
     * 插入绑定的数据
     *
     * @param sid
     * @param orgCode
     * @param year
     * @param sapCode
     * @param submitemList
     * @param reportId
     * @return
     */
    List<RelSapCostItemSubitem> insertSubmitem(Long sid, Long orgCode, Integer year, Long sapCode, List<RelSapCostItemSubitem> submitemList, Integer reportId);

    /**
     * 获取完整的sap提供选择
     *
     * @return
     */
    List<SysSapSubject> getAllSubject();

    /**
     * 关联新的成本项
     *
     * @param user
     * @param id
     * @param item
     * @param reportId
     * @param relSapCostItems
     * @return
     */
    Object updateRelSapCostItem(SysUser user, Integer id, String item, Integer reportId, List<SysSapSubject> relSapCostItems);

    /**
     * 删除匹配好的成本项费用
     *
     * @param sid
     * @return
     */
    Integer deleteRelSapCostItem(Long sid);

    /**
     * 删除匹配好的sap科目
     *
     * @param id
     * @return
     */
    Integer deleteSubmitem(Integer id);

    /**
     * 获取区县成本表的费用类别
     *
     * @param reportId
     * @param orgCode
     * @return
     */
    List<SysCostItem> getCountyItem(Integer reportId, Long orgCode);

    /**
     * 查询化小成本表费用用于处理区县配置
     *
     * @param reportId
     * @param orgCode
     * @return
     */
    List<SysCostItem> getCostItemByReportId(Integer reportId, Long orgCode);

    /**
     * 给区县表配置划小成本的成本项
     *
     * @param cid
     * @param item
     * @param sid
     * @param reportId
     * @param orgCode
     * @return
     */
    String setCountrySapSubject(Integer cid, String item, Integer sid, Integer reportId, Long orgCode);

    /**
     * 选择要新增的费用的父级
     *
     * @param reportId
     * @return
     */
    List<SysCostItem> choseItem(Integer reportId);

    /**
     * 获取数据来源
     *
     * @return
     */
    List<SysDict> getDataFrom();

    /**
     * 保存费用到化小成本表
     *
     * @param sysCostItem
     * @return
     */
    Integer saveItemByCost(SysCostItem sysCostItem);

    /**
     * 保存费用到区县报表
     *
     * @param sysCostItem
     * @return
     */
    Integer saveItemByCounty(SysCostItem sysCostItem);

    /**
     * 修改费用名
     *
     * @param id
     * @param name
     * @return
     */
    Integer updateItemName(Integer id, String name);

    /**
     * 回显树状费用列表
     *
     * @param reportId
     * @return
     */
    List<SysCostItem> choseItemByReportId(Integer reportId);

    /**
     * 删除费用
     *
     * @param id
     * @return
     */
    Integer deleteItemById(Integer id);

    /**
     * 查询表头配置的数据
     *
     * @param reportId
     * @return
     */
    List<RelOrgReportColumn> queryTitleSettingData(Integer reportId);

    /**
     * 查询表头配置可用的数据（回显）
     * @param reportId
     * @return
     */
    List<RelOrgReportColumn> queryTreeEnableData(Integer reportId);

    /**
     * 修改表头配置可用和排序
     * @param ids
     * @return
     */
    int updateSeqAndStatus(Integer reportId, Integer[] ids, Long userTopOrgCode);

    /**
     * 删除所有匹配好的sap成本项
     * @param id
     * @return
     */
    Integer deleteAllSubitem(Integer id);

    List<DataSapListEntity> getMonthList(Long topOrgCode);
}

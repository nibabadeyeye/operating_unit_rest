package com.gpdi.operatingunit.dao.reportconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Zhb
 * @date 2019/11/5 11:37
 */
@Repository
public interface SysCostItemMapper extends BaseMapper<SysCostItem> {

    /**
     * 从sys_cost_item获取成本表的费用类别
     *
     * @param reportId 报表id
     * @param orgCode  组织编码
     * @param year     年份
     * @return 结果集
     */
    List<SysCostItem> getSysCostItemList(@Param("reportId") Integer reportId, @Param("orgCode") Long orgCode, @Param("year") Integer year);

    /**
     * 获取区县表的费用清单
     *
     * @param reportId 报表id
     * @param orgCode  组织编码
     * @param year     年份
     * @return 结果集
     */
    List<SysCostItem> getCountyItem(@Param("reportId") Integer reportId, @Param("orgCode") Long orgCode, @Param("year") Integer year);


    /**
     * description: 查询区县成本数据
     *
     * @param reportId
     * @param orgCode
     * @param year
     * @param parentOrgCode
     * @param month
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getOrganizationByOrgCode(@Param("reportId") Long reportId, @Param("orgCode") Long orgCode,
                                               @Param("year") Integer year, @Param("parentOrgCode") Long parentOrgCode, @Param("month") Long month);

    List<RsCostReportEntity> getOrganizationByOrgCodeData(@Param("reportId") Long reportId, @Param("orgCode") Long orgCode,
                                               @Param("year") Integer year, @Param("parentOrgCode") Long parentOrgCode, @Param("month") Long month);

    /**
     * 删除多余成本项目
     *
     * @param reportId   报表id
     * @param topOrgCode 顶级组织编码
     * @return 影响行数
     */
    List<Long> getDeleteIds(@Param("reportId") Integer reportId,
                            @Param("topOrgCode") Long topOrgCode,
                            @Param("outerReportId") Integer outerReportId,
                            @Param("outerTopOrgCode") Long outerTopOrgCode);

    /**
     * 查询更新后的成本项目数据
     *
     * @param reportId   报表id
     * @param topOrgCode 顶级组织编码
     * @return 成本项目列表
     */
    List<SysCostItem> selectUpdates(@Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode);

    /**
     * 更新成本项目parent_id
     *
     * @param reportId   报表id
     * @param topOrgCode 顶级组织编码
     */
    void updateItemsParentId(@Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode);

    /**
     * description: 划小获取一级分类和成本项目
     *
     * @param reportId 报表id
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getCostItem(@Param("reportId") Long reportId);


    /**
     * description: 获取分类下的子项数量
     *
     * @param reportId
     * @param topOrgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getByOrgCodeAndReportIdColumn(@Param("reportId") Long reportId, @Param("topOrgCode") Long topOrgCode);
    /**
     * description: 获取一级分类
     *
     * @param parentId
     * @param reportId
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getCostItemName(@Param("parentId") int parentId, @Param("reportId") Long reportId, @Param("orgCode") Long orgCode);

    /**
     * description: 根据一级分类获取成本项
     *
     * @param
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<SysCostItem> getCostItemMenu(@Param("name") String name, @Param("reportId") Long reportId, @Param("orgCode") Long orgCode);

    /**
     * description: 获取对应成本项目下的数据（划小）
     *
     * @param reportId      报表id
     * @param orgCode       组织编码
     * @param month         月份，yyyyMM格式
     * @param endMonth      月份，yyyyMM格式
     * @param costItemId
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCostItemDatas(@Param("reportId") Long reportId, @Param("orgCode") Long orgCode,
                                      @Param("month") Long month, @Param("endMonth") Long endMonth, @Param("costItemId") Long costItemId);

    RsCostReportEntity getBudgetOrProgress(@Param("reportId") Long reportId, @Param("list") List<SysOrganization> list,
                                      @Param("beginMonth") Long month, @Param("endMonth") Long endMonth, @Param("costItemId") Long costItemId);



    /**
     * description: 获取对应成本项目下的数据（划小）
     *
     * @param reportId      报表id
     * @param orgCode       组织编码
     * @param month         月份，yyyyMM格式
     * @param endMonth      月份，yyyyMM格式
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyCostItemDatas(@Param("reportId") Long reportId, @Param("orgCode") Long orgCode,
                                      @Param("month") Long month, @Param("endMonth") Long endMonth);

    /**
     * description: 获取对应成本项目下的数据（区县）
     *
     * @param reportId      报表id
     * @param orgCode       组织编码
     * @param month         月份，yyyyMM格式
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCostItemCountDatas(@Param("reportId") Long reportId, @Param("orgCode") Long orgCode, @Param("month") Long month, @Param("costItemId") Long costItemId);

    /**
     * description: 获取对应成本项目下的数据（区县）
     *
     * @param reportId 报表id
     * @param orgCode  组织编码
     * @param month    月份，yyyyMM格式
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    List<RsCostReportEntity> getCostItemCountyData(@Param("reportId") Long reportId, @Param("orgCode") Long orgCode, @Param("month") Long month, @Param("id") Long id);

    /**
     * 查询化小成本表费用用于处理区县配置
     *
     * @param reportId
     * @param orgCode
     * @return
     */
    List<SysCostItem> getCostItemByReportId(@Param("reportId") Integer reportId, @Param("orgCode") Long orgCode);

    /**
     * 更新成本项目full_name（区县报表）
     *
     * @param reportId   报表id
     * @param topOrgCode 顶级组织编码
     */
    void updateCountyItemFullName(@Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取parent_id的最大seq
     * @param parentId
     * @return
     */
    Integer getMaxSeqByParentId(Long parentId);

    /**
     * 根据父ID的子项的最大的seq去查找跟父级同级的最小的seq
     *
     * @param integer
     * @param orgCode
     * @param min
     * @return
     */
    Integer getSeqForCounty(@Param("min") Integer min,@Param("orgCode") Long orgCode, @Param("reportId") Integer reportId);

    /**
     * 根据id获取子级
     * @param reportId
     * @param orgCode
     * @param id
     * @return
     */
    List<SysCostItem> getChildren(@Param("reportId") Integer reportId, @Param("orgCode") Long orgCode, @Param("id") Long id);


    /**
     * 根据id获取子级id
     * @param id
     * @return
     */
    List<Integer> getIdsByParentId(@Param("id") Integer id);
}

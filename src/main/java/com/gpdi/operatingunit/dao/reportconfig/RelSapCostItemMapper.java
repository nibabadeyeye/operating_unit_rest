package com.gpdi.operatingunit.dao.reportconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.reportconfig.RelSapCostItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/10/25 16:53
 */
@Repository
public interface RelSapCostItemMapper extends BaseMapper<RelSapCostItem> {

    /**
     * 执行sql,配置SAP科目与成本项目关系表和成本项目表关联
     *
     * @param reportId   报表id
     * @param topOrgCode 顶级组织编码
     * @param year       年份
     * @return 影响行数
     */
    int relConfig(@Param("reportId") Integer reportId,
                  @Param("topOrgCode") Long topOrgCode,
                  @Param("year") Integer year);

    /**
     * 删除匹配好的成本项
     *
     * @param sid
     * @return
     */
    int deleteItem(@Param("sid") Long sid);

    /**
     * 筛选有条件的费用的sap_code
     * @param year
     * @param reportId
     * @param topOrgCode
     * @return
     */
    List<RelSapCostItem> getSubitemByItemId(@Param("year") Integer year, @Param("reportId") Integer reportId,@Param("orgCode") Long topOrgCode);

    /**
     * 过去费用id修改费用名称
     * @param id
     * @param name
     */
    void updateByItemId(@Param("costItemId") Integer id, @Param("costItem") String name);

    /**
     * 根据cost_item_id获取id
     * @param itemList
     * @return
     */
    List<Integer> getIdsByCostItemId(@Param("list") List<Integer> itemList);

    /**
     * 根据费用获取有条件限制的清单
     * @param itemId
     * @param endYear
     * @param reportId
     * @param topOrgCode
     * @return
     */
    List<RelSapCostItem> getSubitemByItem(@Param("itemId") Long itemId, @Param("endYear") Integer endYear,
                                          @Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode);

    List<RelSapCostItem> getCountySubitemByItemId(@Param("year") Integer year, @Param("reportId") Integer reportId,@Param("orgCode") Long topOrgCode);
}

package com.gpdi.operatingunit.dao.reportconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 成本报表数据结果表
 * 
 * @author long
 * @date 2019-10-29 21:57:40
 */
@Repository
public interface RsCostReportDao extends BaseMapper<RsCostReportEntity> {

    List<RsCostReportEntity> getCostReportTime(@Param("code") Long code, @Param("reportId") Long reportId);

    /**
     * 从sap表获取本月数（无条件限制）
     * @param year
     * @param per
     * @param reportId
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getCurrentMonthValue1(@Param("year") Integer year,@Param("per") Integer per,
                                                   @Param("reportId") Integer reportId,@Param("topOrgCode") Long topOrgCode);

    /**
     * 根据条件查询本月数
     * @param year
     * @param per
     * @param reportId
     * @param topOrgCode
     * @param costItemId
     * @param sb
     * @return
     */
    List<RsCostReportEntity> getCurrentMonthValue2(@Param("year") Integer year, @Param("per") Integer per, @Param("reportId")
            Integer reportId, @Param("topOrgCode") Long topOrgCode,@Param("costItemId") Long costItemId,@Param("params") StringBuilder sb);

    /**
     * 批量插入
     * @param rsList
     */
    void insertByList(@Param("list") List<RsCostReportEntity> rsList);

    /**
     * 读取刚插入的本年数
     * @param topOrgCode
     * @param month
     * @param reportId
     * @return
     */
    List<RsCostReportEntity> selectReportList(@Param("topOrgCode") Long topOrgCode,@Param("month") Integer month,@Param("reportId") Integer reportId);

    /**
     * 获取上月数
     * @param reportId
     * @param month
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getLastMonthValue(@Param("reportId") Integer reportId, @Param("month") Integer month,
                                               @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取年累计
     * @param reportId
     * @param start
     * @param end
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getYearValue(@Param("reportId") Integer reportId, @Param("start") Integer start,
                                                 @Param("end") Integer end, @Param("topOrgCode") Long topOrgCode);


    /**
     * 获取各个区的本月数
     * @param month
     * @param reportId
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getCurrentMonthValue3(@Param("month") Integer month, @Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取市报表的值
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getCityValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                          @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取市报表的成本合计
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getCityCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                               @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取市报表的全部合计
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getAllCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                              @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取最大月份
     * @param reportId
     * @param topOrgCode
     * @return
     */
    Integer getMaxMonth(@Param("reportId") Integer reportId,@Param("topOrgCode") Long topOrgCode);

    /**
     * 获取最小月份
     * @param reportId
     * @param topOrgCode
     * @param mon
     * @return
     */
    Integer getMinMonth(@Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode, @Param("mon") String mon);


    void deleteByMonth(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode, @Param("reportId") Integer reportId);


    /**
     * description: 区成本数据
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                          @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode, @Param("parentOrgCode") Long parentOrgCode);

    /**
     * description: 区成本分类合计数据
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                               @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode, @Param("parentOrgCode") Long parentOrgCode);

    /**
     * description: 区成总合计
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getAllCountyCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                              @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode);

    /**
     * description: 营服数据
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyCostReportValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                              @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);

    /**
     * description: 营服分类合计
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyCostReportCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                                           @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);

    /**
     * description: 营服总合计
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    RsCostReportEntity getAllCountyCostReportCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                              @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);




    /**
     * description: 区划小进度
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyProgressValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                                      @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);

    /**
     * description: 区划小进度分类合计
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyProgressCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                                      @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);

    /**
     * description: 区划小进度总合计
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity
     */
    RsCostReportEntity getAllCountyProgressCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                                        @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);




    /**
     * description: 区划小预算
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyBudgetValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                                      @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);

    /**
     * description: 区划小预算分类合计
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    List<RsCostReportEntity> getCountyBudgetCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                                      @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);

    /**
     * description: 区划小预算总合计
     *
     * @param reportId
     * @param fromMonth
     * @param toMonth
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity
     */
    RsCostReportEntity getAllCountyBudgetCountValue(@Param("reportId") Integer reportId, @Param("fromMonth") Integer fromMonth,
                                                        @Param("toMonth") Integer toMonth, @Param("topOrgCode") Long topOrgCode,  @Param("parentOrgCode") Long parentOrgCode, @Param("orgCode") Long orgCode);

    /**
     * 查询区县费用中没有父子级以及没有条件限制的本月数
     * @param year
     * @param per
     * @param reportId
     * @param topOrgCode
     * @return
     */
    List<RsCostReportEntity> getCountyCurrentMonthValue1(@Param("year") Integer year,@Param("per") Integer per,
                                                         @Param("reportId") Integer reportId,@Param("topOrgCode") Long topOrgCode);
}

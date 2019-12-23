package com.gpdi.operatingunit.dao.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.relation.RelReportColumnValue;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/11/14 17:41
 */
@Repository
public interface RelReportColumnValueMapper extends BaseMapper<RelReportColumnValue> {

    /**
     * 获取当前报表的月份
     *
     * @param reportId
     * @return
     */
    List<RelReportColumnValue> selectMonth(@Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取渠道效益报表数据
     *
     * @param month
     * @param reportId
     * @param districtName
     * @param centerName
     * @param topOrgCode
     * @return
     */
    List<RelReportColumnValue> getBenefitsList(@Param("month") Integer month, @Param("reportId") Integer reportId,
                                               @Param("district") String districtName, @Param("center") String centerName,
                                               @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取成本对标报表数据
     *
     * @param month
     * @param reportId
     * @param districtName
     * @param centerName
     * @param topOrgCode
     * @return
     */
    List<RelReportColumnValue> getCostList(@Param("month") Integer month, @Param("reportId") Integer reportId,
                                           @Param("district") String districtName, @Param("center") String centerName,
                                           @Param("topOrgCode") Long topOrgCode);

    /**
     * 获取业务量展示报表最新月份数据
     *
     * @return
     */
    int developMaxMonth();


    /**
     * 业务量发展报表市层级数据
     *
     * @param month
     * @param topOrgCode
     * @return
     */
    List<Map<Object, Object>> queryDevelopCityCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode);

    /**
     * 业务量发展报表市级对应区对应营服数据
     *
     * @param month
     * @param topOrgCode
     * @return
     */
    List<Map<Object, Object>> innerToDevelopCityCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode);

    /**
     * 业务量发展报表区层级数据
     *
     * @param month
     * @param topOrgCode
     * @param orgCode
     * @return
     */
    List<Map<Object, Object>> queryDevelopDistrictCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode,
                                                      @Param("orgCode") Long orgCode);

    /**
     * 业务量发展报表区对应营服数据
     *
     * @param month
     * @param topOrgCode
     * @param orgCode
     * @return
     */
    List<Map<Object, Object>> innerToDevelopDistrictCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode,
                                                        @Param("orgCode") Long orgCode);

    /**
     * 业务量发展报表营服以及以下层级数据
     *
     * @param month
     * @param topOrgCode
     * @param orgCode
     * @return
     */
    List<Map<Object, Object>> queryDevelopServiceCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode,
                                                     @Param("orgCode") Long orgCode);


    /**
     * 获取收入展示报表最新月份数据
     *
     * @return
     */
    int incomeMaxMonth();


    /**
     * 收入展示报表市层级数据
     *
     * @param month
     * @param topOrgCode
     * @return
     */
    List<Map<Object, Object>> queryIncomeCityCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode);

    /**
     * 收入展示报表市级对应区对应营服数据
     *
     * @param month
     * @param topOrgCode
     * @return
     */
    List<Map<Object, Object>> innerToIncomeCityCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode);

    /**
     * 收入展示报表区层级数据
     *
     * @param month
     * @param topOrgCode
     * @param orgCode
     * @return
     */
    List<Map<Object, Object>> queryIncomeDistrictCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode,
                                                             @Param("orgCode") Long orgCode);

    /**
     * 收入展示报表区对应营服数据
     *
     * @param month
     * @param topOrgCode
     * @param orgCode
     * @return
     */
    List<Map<Object, Object>> innerToIncomeDistrictCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode,
                                                               @Param("orgCode") Long orgCode);

    /**
     * 收入展示报表营服以及以下层级数据
     *
     * @param month
     * @param topOrgCode
     * @param orgCode
     * @return
     */
    List<Map<Object, Object>> queryIncomeServiceCenterList(@Param("month") Integer month, @Param("topOrgCode") Long topOrgCode,
                                                            @Param("orgCode") Long orgCode);


    /**
     * 判断该月份数据是否存在
     * @param month
     * @param reportId
     * @param topOrgCode
     * @return
     */
    Integer getIfExist(@Param("month") Integer month, @Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode);
}

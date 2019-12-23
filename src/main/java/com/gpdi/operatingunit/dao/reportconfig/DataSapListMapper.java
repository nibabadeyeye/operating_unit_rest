package com.gpdi.operatingunit.dao.reportconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.common.DataSapList;
import com.gpdi.operatingunit.entity.reportconfig.DataSapListEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SAP表
 * 
 * @author long
 * @date 2019-10-25 04:00:12
 */
@Repository
public interface DataSapListMapper extends BaseMapper<DataSapListEntity> {

    int addDataSapList(@Param("list") List<DataSapListEntity> list);

    Long getUserOrgCode(@Param("id") int id);

    /**
     * 获取营服级的没有特殊条件的sap清单
     * @param itemId
     * @param startYear
     * @param endYear
     * @param startPer
     * @param endPer
     * @param orgCode
     * @param topOrgCode
     * @param reportId
     * @return
     */
    List<DataSapList> getSapList1(@Param("itemId") Long itemId, @Param("startYear") Integer startYear, @Param("endYear") Integer endYear,
                                  @Param("startPer") Integer startPer, @Param("endPer") Integer endPer, @Param("orgCode") Long orgCode,
                                  @Param("topOrgCode") Long topOrgCode, @Param("reportId") Integer reportId);

    /**
     * 获取区级的没有特殊条件的sap清单
     * @param itemId
     * @param startYear
     * @param endYear
     * @param startPer
     * @param endPer
     * @param orgCode
     * @param topOrgCode
     * @param reportId
     * @return
     */
    List<DataSapList> getSapList3(@Param("itemId") Long itemId, @Param("startYear") Integer startYear, @Param("endYear") Integer endYear,
                                  @Param("startPer") Integer startPer, @Param("endPer") Integer endPer, @Param("orgCode") Long orgCode,
                                  @Param("topOrgCode") Long topOrgCode, @Param("reportId") Integer reportId);

    /**
     * 获取营服级的有条件的sap清单
     * @param startYear
     * @param endYear
     * @param startPer
     * @param endPer
     * @param orgCode
     * @param sb
     * @return
     */
    List<DataSapList> getSapList2(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear,
                                  @Param("startPer") Integer startPer, @Param("endPer") Integer endPer,
                                  @Param("orgCode") Long orgCode, @Param("param") StringBuilder sb);

    /**
     * 获取区级的有条件的sap清单
     * @param startYear
     * @param endYear
     * @param startPer
     * @param endPer
     * @param orgCode
     * @param sb
     * @return
     */
    List<DataSapList> getSapList4(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear,
                                  @Param("startPer") Integer startPer, @Param("endPer") Integer endPer,
                                  @Param("orgCode") Long orgCode, @Param("param") StringBuilder sb);
}

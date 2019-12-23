package com.gpdi.operatingunit.dao.reportconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.reportconfig.RelOrgReportColumnEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 
 * 
 * @author long
 * @email
 * @date 2019-11-14 02:36:44
 */
@Repository
public interface RelOrgReportColumnDao extends BaseMapper<RelOrgReportColumnEntity> {


    /**
     * description: 根据报表id查询划小与区县需要展示的字段
     *
     * @param reportId      报表id
     * @param topOrgCode    顶级组织编码
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RelOrgReportColumnEntity>
     */
    List<RelOrgReportColumnEntity> queryByReportIdAndTopOrgCode(@Param("reportId") Long reportId, @Param("topOrgCode") Long topOrgCode);


	
}

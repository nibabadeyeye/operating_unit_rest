package com.gpdi.operatingunit.dao.reportconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitReportDetailsEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitsReportEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author：long
 * @Date：2019/12/3 13:15
 * @Description：
 */
@Repository
public interface SimpleProfitsReportMapper extends BaseMapper<SimpleProfitsReportEntity> {

    /**
     * description: 查看用户可显示的数据
     *
     * @param topOrgCode    地市编码
     * @param pageNumber    页数
     * @param pageSize      显示行数
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SimpleProfitsReportEntity>
     */
    List<SimpleProfitsReportEntity> findByTopOrgCode(@Param("topOrgCode") Long topOrgCode,@Param("pageNumber") int pageNumber,@Param("pageSize") int pageSize);

    /**
     * description: 获取总数
     *
     * @param topOrgCode    地市编码
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SimpleProfitsReportEntity>
     */
    List<SimpleProfitsReportEntity> findByTopOrgCodeCount(@Param("topOrgCode") Long topOrgCode);

    /**
     * description: 数据删除
     *
     * @param list
     * @return void
     */
    void delSimpleProfitsReportAndId(@Param("list") List<SimpleProfitsReportEntity> list);

    /**
     * description: 获取需要展示的行
     *
     * @param
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SimpleProfitEntity>
     */
    List<SimpleProfitEntity> findSimpleProfitAll();

    /**
     * description: 获取详细数据
     *
     * @param topOrgCode    地市编码
     * @param id            利润id
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SimpleProfitsReportEntity>
     */
    List<SimpleProfitsReportEntity> findByTopOrgCodeAndId(@Param("topOrgCode") Long topOrgCode,@Param("id") Integer id);

    /**
     * description: 数据添加
     *
     * @param list
     * @return int
     */
    int insertSimpleProfitReportDetails(@Param("list") List<SimpleProfitReportDetailsEntity> list);

    /**
     * description: 数据添加
     *
     * @param emp
     * @return int
     */
    int insertTimpleProfitsReport(@Param("emp") SimpleProfitsReportEntity emp);
}

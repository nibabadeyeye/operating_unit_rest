package com.gpdi.operatingunit.service.reportconfig;

import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitReportDetailsEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitsReportEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author：long
 * @Date：2019/12/3 13:21
 * @Description：
 */
public interface SimpleProfitService {

    /**
     * description: 获取表头
     *
     * @param
     * @return java.util.List<java.util.Map<java.lang.Object,java.lang.Object>>
     */
    List<Map<Object,Object>> findAll();

    /**
     * description: 查看用户可显示的数据
     *
     * @param topOrgCode
     * @param pageNumber
     * @param pageSize
     * @return java.util.Map
     */
    Map findByTopOrgCode(Long topOrgCode,int pageNumber,int pageSize);

    /**
     * description: 数据删除
     *
     * @param list
     * @return void
     */
    void delSimpleProfitsReportAndId(List<SimpleProfitsReportEntity> list);

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
     * @param topOrgCode
     * @param id
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SimpleProfitsReportEntity>
     */
    List<SimpleProfitsReportEntity> findByTopOrgCodeAndId(Long topOrgCode,Integer id);

    /**
     * description: 数据添加
     *
     * @param list
     * @return int
     */
    int insertSimpleProfitReportDetails(List<SimpleProfitReportDetailsEntity> list);
}

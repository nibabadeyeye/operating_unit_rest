package com.gpdi.operatingunit.service.home;

import com.gpdi.operatingunit.entity.home.RsHomeNumberData;

import java.util.List;
import java.util.Map;

/**
 * @author Zhb
 * @date 2019/12/16 10:45
 **/
public interface MainService {

    /**
     * 获取首页数据
     *
     * @param month     月份
     * @param firstTime 是否第一次查询
     * @return map
     */
    Map<String, Object> getData(Integer month, boolean firstTime);

    /**
     * 获取首页收多少、花多少等数据
     *
     * @param topOrgCode 顶级组织编码
     * @param orgCode    组织编码
     * @param month      月份
     * @return 数据对象
     */
    RsHomeNumberData getNumberDataByMonth(Long topOrgCode, Long orgCode, Integer month);

    /**
     * 查询月份列表
     *
     * @param topOrgCode 顶级组织编码
     * @return 月份列表
     */
    List<Integer> getMonths(Long topOrgCode);
}

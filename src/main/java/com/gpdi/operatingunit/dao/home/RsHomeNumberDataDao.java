package com.gpdi.operatingunit.dao.home;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.home.RsHomeNumberData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhb
 * @date 2019-12-15 21:36:36
 */
@Repository
public interface RsHomeNumberDataDao extends BaseMapper<RsHomeNumberData> {

    /**
     * 获取首页收多少、花多少等数据
     *
     * @param topOrgCode 顶级组织编码
     * @param orgCode    组织编码
     * @param month      月份
     * @return 数据对象
     */
    RsHomeNumberData getNumberDataByMonth(@Param("topOrgCode") Long topOrgCode,
                                          @Param("orgCode") Long orgCode,
                                          @Param("month") Integer month);

    /**
     * 查询月份列表
     *
     * @param topOrgCode 顶级组织编码
     * @return 月份列表
     */
    List<Integer> getMonths(@Param("topOrgCode") Long topOrgCode);

}

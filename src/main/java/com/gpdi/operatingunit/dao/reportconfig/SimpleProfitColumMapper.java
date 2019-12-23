package com.gpdi.operatingunit.dao.reportconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitColumEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author：long
 * @Date：2019/12/3 11:39
 * @Description：
 */
@Repository
public interface SimpleProfitColumMapper extends BaseMapper<SimpleProfitColumEntity> {

    /**
     * description: 获取表头
     *
     * @param
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SimpleProfitColumEntity>
     */
    List<SimpleProfitColumEntity> findAll();
}

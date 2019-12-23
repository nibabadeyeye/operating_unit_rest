package com.gpdi.operatingunit.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.system.SysCostCenter;
import org.springframework.stereotype.Repository;

/**
 * @author Zhb
 * @date 2019/10/29 9:22
 **/
@Repository
public interface SysCostCenterMapper extends BaseMapper<SysCostCenter> {

    /**
     * 保存成本中心
     *
     * @param sysCostCenter 成本中心实体
     * @return 主键
     */
    Long saveCostCenter(SysCostCenter sysCostCenter);
}

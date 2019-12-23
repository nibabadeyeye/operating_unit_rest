package com.gpdi.operatingunit.dao.reportconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItemHandle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 成本项目数据处理表
 *
 * @author Zhb
 * @date 2019-11-03 20:11:52
 */
@Repository
public interface SysCostItemHandleMapper extends BaseMapper<SysCostItemHandle> {

    /**
     * 更新成本项目full_name（区县报表）
     *
     * @param reportId   报表id
     * @param topOrgCode 顶级组织编码
     */
    void updateCountyItemFullName(@Param("reportId") Integer reportId, @Param("topOrgCode") Long topOrgCode);
}

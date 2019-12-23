package com.gpdi.operatingunit.service.reportconfig;

import com.gpdi.operatingunit.entity.reportconfig.RelOrgReportColumnEntity;
import com.gpdi.operatingunit.entity.system.SysRole;

import java.util.List;
import java.util.Map;

/**
 * @Author：long
 * @Date：2019/11/14 15:44
 * @Description：
 */
public interface RelOrgReportColumnService {

    /**
     * description: 根据报表id查询划小与区县需要展示的字段
     *
     * @param reportId      报表id
     * @param topOrgCode    顶级组织编码
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.RelOrgReportColumnEntity>
     */
    List<RelOrgReportColumnEntity> queryByReportIdAndTopOrgCode(Long reportId, Long topOrgCode);

    /**
     * description: 根据用户id查询用户角色
     *
     * @param userId 用户id
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysRole>
     */
    List<SysRole> queryById(int userId);


    /**
     * description: test
     *
     * @param
     * @return java.util.List<Map<Object,Object>>
     */
    List<Map<Object, Object>> queryAllByReportIdAndTopOrgCode(String level,Long reportId, Long topOrgCode, Long orgCode);
}

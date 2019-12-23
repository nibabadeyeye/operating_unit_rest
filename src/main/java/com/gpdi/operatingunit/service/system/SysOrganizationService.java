package com.gpdi.operatingunit.service.system;

import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.entity.system.SysOrganization;

import java.util.List;

/**
 * @author Zhb
 * @date 2019/10/30 18:57
 **/
public interface SysOrganizationService {

    /**
     * 查询组织
     *
     * @return 组织列表
     */
    List<SysOrganization> querySysOrganization();

    /**
     * 根据登录用户查询level：2 地市组织
     *
     * @return 地市组织编码
     */
    List<SysOrganization> queryLocalCity();


    /**
     * 查询所有地级市
     *
     * @return
     */
    List<SysOrganization> queryAllLocalCity();

    /**
     * 查询所有的组织
     *
     * @return
     */
    List<SysOrganization> queryAllSysOrganization();

    /**
     * 获取全部组织(含子项)
     *
     * @param queryParams 查询参数
     * @return 组织列表
     */
    List<SysOrganization> queryList(QueryParams queryParams);

    /**
     * 根据id获取权限数据
     *
     * @param code 主键
     * @return 权限对象
     */
    SysOrganization getByCode(Long code);

    /**
     * 保存组织对象
     *
     * @param sysOrganization 组织对象
     * @return 组织编码
     */
    Long save(SysOrganization sysOrganization);

    /**
     * 根据编码列表修改组织启用状态
     *
     * @param codes  编码列表
     * @param enable 是否启用
     */
    void updateEnableByCodes(Long[] codes, Integer enable);

    /**
     * 根据编码列表删除组织数据
     *
     * @param codes 编码列表
     * @return 影响行数
     */
    int deleteByCodes(Long[] codes);

    /**
     * 获取新增项排序值
     *
     * @param level      组织等级
     * @param parentCode 父级编码
     * @return 排序值
     */
    int getNextSeq(Integer level, Long parentCode);

    /**
     * 查询地市组织
     *
     * @return 组织列表
     */
    List<SysOrganization> queryCityOrgs();
}

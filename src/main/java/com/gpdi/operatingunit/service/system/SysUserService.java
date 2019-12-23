package com.gpdi.operatingunit.service.system;

import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户管理
 * @Author: Lxq
 * @Date: 2019/9/6 10:47
 */
public interface SysUserService {

    /**
     * 根据id获取用户信息
     */
    SysUser queryUserById(Integer id);

    /**
     * 根据userName获取用户信息
     */
    SysUser queryUserByUserName(String userName);


    /**
     * 分页查询成本中心数据
     *
     * @param queryParams 多个参数
     * @return 分页查询结果
     */
    Map<String, Object> pageQuery(QueryParams queryParams);

    /**
     * 删除用户信息
     *
     * @param ids 主键id集合
     * @return 影响条数
     */
    int deleteByIds(List<Integer> ids);

    /**
     * 保存用户信息
     *
     * @param sysUser 用户对象
     * @return 影响条数
     */
    int save(SysUser sysUser);

    /**
     * 根据用户名称返回对象
     *
     * @param userName 用户名
     * @return
     */
    SysUser uniqueUserName(String userName);

    /**
     * 获取当前用户以及以下的组织关系
     *
     * @return 关系组织数据
     */
    List<SysOrganization> getOrgCodeData();

    /**
     * 获取当前用户的角色以及以下角色信息数据
     *
     * @return
     */
    List getRoleOptionData();

    /**
     * 获取用户角色数据（修改回显角色数据）
     *
     * @param userId
     * @return
     */
    Object[] getReShowRoleData(Integer userId);

}

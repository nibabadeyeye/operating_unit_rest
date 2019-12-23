package com.gpdi.operatingunit.service.system;


import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.entity.system.RelRolePermission;
import com.gpdi.operatingunit.entity.system.SysPermission;
import com.gpdi.operatingunit.entity.system.SysRole;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @Author: Lxq
 * @Date: 2019/9/7 23:23
 */
public interface SysRoleService {

    /**
     * 分页查询角色信息
     *
     * @param queryParams
     * @return
     */
    Map<String, Object> pageQuery(QueryParams queryParams);

    /**
     * 删除角色信息
     *
     * @param ids 主键id集合
     * @return 影响条数
     */
    int deleteByIds(List<Integer> ids);


    /**
     * 获取用户的角色级联数据
     *
     * @return
     */
    List<SysRole> getUserRoleCascadeData();

    /**
     * 保存/修改角色权限
     *
     * @param roleId       角色id
     * @param perminssions 权限ids集合
     * @return
     */
    int savePermisson(Integer roleId, Integer[] perminssions);

    /**
     * 获取角色权限
     *
     * @param roleId 角色id
     * @return
     */
    Object[] getRolePermisson(Integer roleId);

    /**
     * 父级下面的角色
     *
     * @param parentId
     * @return 角色名称集合
     */
    Object[] queryRoleByParentId(Integer parentId);

    /**
     * 保存角色信息
     *
     * @param sysRole 角色对象
     * @return 影响条数
     */
    int save(SysRole sysRole);

    /**
     * 角色管理父级角色数据
     *
     * @return
     */
    List<SysRole> getparentIdRoleCascadeData();

    /**
     * 角色管理修改回显父级数据
     *
     * @param id
     * @return
     */
    Object[] reshowParentId(Integer id);


}

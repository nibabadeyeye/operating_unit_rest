package com.gpdi.operatingunit.service.system;

import com.gpdi.operatingunit.entity.system.SysPermission;

import java.util.List;
import java.util.Map;

/**
 * @Author Lxq
 * @Date 2019/10/23 16:47
 **/
public interface SysPermissionService {

    /**
     * 通过用户id获取用户权限菜单
     *
     * @param userId
     * @return
     */
    List<SysPermission> getUserPermission(Integer userId);

    /**
     * 获取所有权限菜单
     *
     * @return
     */
    List<SysPermission> getPermissionData();

    /**
     * 获取全部菜单(含子项)
     *
     * @return 菜单列表
     */
    List<SysPermission> queryAllPermission();

    /**
     * 根据id获取权限数据
     *
     * @param id 主键
     * @return 权限对象
     */
    SysPermission getById(Integer id);

    /**
     * 保存权限对象
     *
     * @param sysPermission 权限对象
     * @return 主键
     */
    Integer save(SysPermission sysPermission);

    /**
     * 根据id列表修改权限启用状态
     *
     * @param ids    主键列表
     * @param enable 是否启用
     */
    void updateEnableByIds(Integer[] ids, Integer enable);

    /**
     * 根据id列表删除权限数据
     *
     * @param ids 主键列表
     * @return 影响行数
     */
    int deleteByIds(Integer[] ids);

    /**
     * 获取新增项排序值
     *
     * @param level    权限等级
     * @param parentId 父级ID
     * @return 排序值
     */
    int getNextSeq(Integer level, Integer parentId);

    /**
     * 根据用户id筛选权限数据
     *
     * @param userId 用户id
     * @return 权限列表
     */
    List<SysPermission> queryPermissionByUserId(Integer userId);

    /**
     * 根据用户id和权限等级筛选权限数据
     *
     * @param userId 用户id
     * @param level  权限等级
     * @return 权限列表
     */
    List<SysPermission> queryPermissionByUserIdAndLevel(Integer userId, Integer level);

    /**
     * 获取当前用户拥有的权限
     *
     * @return 权限数据
     */
    Map<String, Object> getCurrentUserPerms();
}

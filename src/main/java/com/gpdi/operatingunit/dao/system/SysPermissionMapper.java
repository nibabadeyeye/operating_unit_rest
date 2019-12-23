package com.gpdi.operatingunit.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.system.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Lxq
 * @Date 2019/9/7 17:47
 **/
@Repository
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 获取用户权限菜单
     *
     * @param userId
     * @return
     */
    List<SysPermission> queryUserPermission(Integer userId);

    /**
     * 获取所有的菜单
     *
     * @return
     */
    List<SysPermission> queryPermissionData();

    /**
     * 获取全部菜单
     *
     * @return 菜单列表
     */
    List<SysPermission> queryAllPermission();

    /**
     * 根据id修改enable
     *
     * @param ids          主键列表
     * @param updateOperId 操作人id
     * @param enable       启用状态
     */
    void updateEnableByIds(@Param("ids") List<Integer> ids,
                           @Param("updateOperId") Integer updateOperId,
                           @Param("enable") Integer enable);

    /**
     * 根据id获取权限对象
     *
     * @param id 主键
     * @return 权限对象
     */
    SysPermission getById(@Param("id") Integer id);

    /**
     * 根据权限等级和父级ID获取最大排序值
     *
     * @param level    权限等级
     * @param parentId 父级ID
     * @return 排序值
     */
    Integer getMaxSeqByLevelAndParentId(@Param("level") Integer level, @Param("parentId") Integer parentId);

    /**
     * 查询所有关联的子项
     *
     * @param id 主键
     * @return 所有子项id列表
     */
    List<Integer> queryAllChildIds(@Param("id") Integer id);

    /**
     * 根据用户id和权限等级筛选权限数据
     *
     * @param userId 用户id
     * @param level  权限等级
     * @return 权限列表
     */
    List<SysPermission> queryPermissionByUserIdAndLevel(@Param("userId") Integer userId,
                                                        @Param("level") Integer level);
}

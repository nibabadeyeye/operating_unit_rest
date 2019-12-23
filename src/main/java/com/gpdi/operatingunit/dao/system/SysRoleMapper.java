package com.gpdi.operatingunit.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.system.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Lxq
 * @Date 2019/9/7 23:26
 **/
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * description: 获取登录用户角色
     *
     * @param userId 用户id
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysRole>
     */
    List<SysRole> queryById(@Param("userId") int userId);
}

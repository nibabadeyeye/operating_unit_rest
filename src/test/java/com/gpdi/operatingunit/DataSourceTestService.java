package com.gpdi.operatingunit;

import com.gpdi.operatingunit.datasources.DataSourcesNames;
import com.gpdi.operatingunit.datasources.annotation.DataSource;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.system.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Lxq
 * @Date: 2019/10/21 10:49
 */
@Service
public class DataSourceTestService {

    @Autowired
    private SysUserService sysUserService;

    public SysUser queryObject(Integer userId){
        return sysUserService.queryUserById(userId);
    }

    @DataSource(name = DataSourcesNames.SECOND)
    public SysUser queryObject2(Integer userId){
        return sysUserService.queryUserById(userId);
    }

}

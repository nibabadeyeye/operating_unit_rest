package com.gpdi.operatingunit;

import com.gpdi.operatingunit.entity.system.SysUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author: Lxq
 * @Date: 2019/10/21 10:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDataSourceTest {

    @Autowired
    private DataSourceTestService dataSourceTestService;

    @Test
    public void test(){
        //数据源1
        SysUser user = dataSourceTestService.queryObject(1);
        System.out.println(user);

        //数据源2
        SysUser user2 = dataSourceTestService.queryObject2(1);
        System.out.println(user2);

    }
}

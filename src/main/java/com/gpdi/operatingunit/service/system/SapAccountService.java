package com.gpdi.operatingunit.service.system;

import com.gpdi.operatingunit.entity.system.SysDict;
import com.gpdi.operatingunit.entity.system.SysSapAccount;

import java.util.List;

/**
 * @Author: zyb
 * @Date: 2019/11/28 11:06
 */
public interface SapAccountService {

    /**
     * 获取账号集合
     * @return
     */
    List<SysSapAccount> getAccountList(String type);

    /**
     * 添加一个账号
     * @param sysSapAccount
     * @return
     */
    Integer addAccount(SysSapAccount sysSapAccount);

    /**
     * 修改账户信息
     * @param sysSapAccount
     * @return
     */
    Integer updateAccount(SysSapAccount sysSapAccount);

    /**
     * 删除账号信息
     * @param id
     * @return
     */
    Integer deleteAccount(Integer id);

    /**
     * 获取账号类型
     * @return
     */
    List<SysDict> getTypeList();

    /**
     * 点击触发下载sap数据
     * @return
     */
    Integer startGetSapValue();
}

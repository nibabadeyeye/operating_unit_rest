package com.gpdi.operatingunit.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.system.GdtelHxksbxzMapper;
import com.gpdi.operatingunit.dao.system.SysDictMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.dao.system.SysSapAccountMapper;
import com.gpdi.operatingunit.entity.system.*;
import com.gpdi.operatingunit.service.system.SapAccountService;
import com.gpdi.operatingunit.utils.Base64Utils;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: zyb
 * @Date: 2019/11/28 11:06
 */
@Service
@Transactional
public class SapAccountServiceImpl implements SapAccountService{

    @Autowired
    private SysSapAccountMapper sysSapAccountMapper;
    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;
    @Autowired
    private GdtelHxksbxzMapper gdtelHxksbxzMapper;

    /**
     * 触发点击下载sap数据
     * @return
     */
    @Override
    public Integer startGetSapValue() {
        Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();
        String email = ShiroUtils.getUser().getEmail();
        //查出是哪个城市
        QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
        qw.select("alias").eq("code",topOrgCode);
        SysOrganization sysOrganization = sysOrganizationMapper.selectOne(qw);
        String city = sysOrganization.getAlias().substring(0, sysOrganization.getAlias().indexOf("分公司"));

        //查出相关的几个账号
        QueryWrapper<SysSapAccount> wq = new QueryWrapper<>();
        wq.select("top_org_code,account,password,account_type").eq("top_org_code",topOrgCode);
        List<SysSapAccount> accountList = sysSapAccountMapper.selectList(wq);

        //新生成一个对象留备插入机器人的表
        GdtelHxksbxz gdtelHxksbxz = new GdtelHxksbxz();
        gdtelHxksbxz.setCity(city);
        gdtelHxksbxz.setEmail(email);

        for (SysSapAccount account : accountList) {
            switch(account.getAccountType()){
                case "sap系统":
                    gdtelHxksbxz.setSapAccount(account.getAccount());
                    gdtelHxksbxz.setSapPassword(account.getPassword());
                    break;
                case "OA系统":
                    gdtelHxksbxz.setOaAccount(account.getAccount());
                    gdtelHxksbxz.setOaPassword(account.getPassword());
                    break;
                case "analyzer系统":
                    gdtelHxksbxz.setAnalyzerAccount(account.getAccount());
                    gdtelHxksbxz.setAnalyzerPassword(account.getPassword());
                    break;
            }
        }

        //插入对象
        int insert = gdtelHxksbxzMapper.insert(gdtelHxksbxz);
        return insert;
    }

    /**
     * 删除账号信息
     * @param id
     * @return
     */
    @Override
    public Integer deleteAccount(Integer id) {
        return sysSapAccountMapper.deleteById(id);
    }

    /**
     * 修改账号信息
     * @param sysSapAccount
     * @return
     */
    @Override
    public Integer updateAccount(SysSapAccount sysSapAccount) {
        SysUser user = ShiroUtils.getUser();
        QueryWrapper<SysSapAccount> qw = new QueryWrapper<>();
        qw.select("*").eq("id",sysSapAccount.getId());
        SysSapAccount account = sysSapAccountMapper.selectOne(qw);
        String oldPwd = Base64Utils.decode(account.getPassword());
        //判断旧密码是否正确
        if (!sysSapAccount.getOldPwd().equals(oldPwd)){
            return -1;
            //旧密码正确则判断新密码跟旧密码是否一样
        }else if(sysSapAccount.getPassword().equals(oldPwd)){
            return -2;
        }
        //加密密码
        sysSapAccount.setPassword(Base64Utils.encode(sysSapAccount.getPassword()));
        sysSapAccount.setUpdateId(user.getId());
        sysSapAccount.setUpdateTime(DateUtils.getCurrentDate());
        int update = sysSapAccountMapper.updateById(sysSapAccount);
        return update;
    }

    /**
     * 添加一个账号
     * @param sysSapAccount
     * @return
     */
    @Override
    public Integer addAccount(SysSapAccount sysSapAccount) {
        SysUser user = ShiroUtils.getUser();
        QueryWrapper<SysSapAccount> qw = new QueryWrapper<>();
        qw.select("*").eq("account",sysSapAccount.getAccount()).eq("top_org_code",user.getTopOrgCode()).
                eq("account_type",sysSapAccount.getAccountType());
        SysSapAccount account = sysSapAccountMapper.selectOne(qw);
        if (account != null){
            return -1;
        }
        //加密密码
        sysSapAccount.setPassword(Base64Utils.encode(sysSapAccount.getPassword()));
        sysSapAccount.setTopOrgCode(user.getTopOrgCode());
        sysSapAccount.setCreateId(user.getId());
        sysSapAccount.setCreateTime(DateUtils.getCurrentDate());
        sysSapAccount.setUpdateId(user.getId());
        sysSapAccount.setUpdateTime(DateUtils.getCurrentDate());
        //插入一个对象到数据库
        int insert = sysSapAccountMapper.insert(sysSapAccount);
        return insert;
    }

    /**
     * 获取账号类型
     * @return
     */
    @Override
    public List<SysDict> getTypeList() {
        QueryWrapper<SysDict> qw = new QueryWrapper<>();
        qw.select("*").eq("cat","sys.account_type").orderByAsc("seq");
        List<SysDict> result = sysDictMapper.selectList(qw);
        return result;
    }

    /**
     * 获取账号集合
     * @return
     */
    @Override
    public List<SysSapAccount> getAccountList(String type) {
        Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();
        List<SysSapAccount> result = sysSapAccountMapper.getAccountList(type,topOrgCode);
        return result;
    }
}

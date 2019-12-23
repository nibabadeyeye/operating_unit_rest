package com.gpdi.operatingunit.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.system.RelUserOrganizationMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.entity.system.RelUserOrganization;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description: 系统组织工具类
 * @author: Lxq
 * @date: 2019/10/30 15:26
 */
@Component
public class SysOrganizationUtils {

    @Autowired
    private RelUserOrganizationMapper relUserOrganizationMapper;

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    /**
     * 使用静态方式
     */
    private static SysOrganizationUtils sysOrganizationUtils;

    @PostConstruct
    public void init() {
        sysOrganizationUtils = this;
    }

    /**
     * 获取用户的组织数据
     */
    public static String getOrganizationCode(){
        SysUser user = ShiroUtils.getUser();
        String orgCode;
        QueryWrapper<RelUserOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        RelUserOrganization relUserOrganization = sysOrganizationUtils.relUserOrganizationMapper.selectOne(queryWrapper);
        QueryWrapper<SysOrganization> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("code",relUserOrganization.getOrgCode());
        SysOrganization sysOrganization = sysOrganizationUtils.sysOrganizationMapper.selectOne(queryWrapper1);
        if (sysOrganization.getFullCode().contains("|")){
            String[] fullCode =  sysOrganization.getFullCode().split("\\|");
            orgCode =  fullCode[0];
        }else {
            orgCode = sysOrganization.getFullCode();
        }
        return orgCode;
    }

}

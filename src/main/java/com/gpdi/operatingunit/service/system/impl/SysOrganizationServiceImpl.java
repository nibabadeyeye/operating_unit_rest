package com.gpdi.operatingunit.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.controller.system.support.SysConstant;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.system.SysOrganizationService;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import com.gpdi.operatingunit.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Zhb
 * @date 2019/10/30 18:59
 **/
@Service
public class SysOrganizationServiceImpl implements SysOrganizationService {

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Override
    public List<SysOrganization> querySysOrganization() {
        QueryWrapper<SysOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*").gt("level", SysConstant.ORG_LEVEL_PROVINCE)
                .like("full_code", ShiroUtils.getTopOrgCode())
                .orderByAsc("seq");
        return sysOrganizationMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysOrganization> queryLocalCity() {
        SysUser user = ShiroUtils.getUser();
        QueryWrapper<SysOrganization> queryWrapper = new QueryWrapper<>();
        if (user.getTopOrgCode() == SysConstant.MAX_ORG_CODE) {
            queryWrapper.select("*").eq("level", SysConstant.ORG_LEVEL_CITY)
                    .or().eq("level", SysConstant.ORG_LEVEL_PROVINCE);
        } else {
            queryWrapper.select("*").eq("level", SysConstant.ORG_LEVEL_CITY)
                    .like("full_code", user.getTopOrgCode());
        }
        return sysOrganizationMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysOrganization> queryAllLocalCity() {
        QueryWrapper<SysOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*").eq("level", 2);
        return sysOrganizationMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysOrganization> queryAllSysOrganization() {
        QueryWrapper<SysOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        return sysOrganizationMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysOrganization> queryList(QueryParams queryParams) {
        if (queryParams.getTopOrgCode() == null) {
            queryParams.setTopOrgCode(ShiroUtils.getTopOrgCode());
        }
        List<SysOrganization> sysOrganizations = sysOrganizationMapper.queryList(queryParams.getTopOrgCode());
        return buildTree(sysOrganizations);
    }

    @Override
    public SysOrganization getByCode(Long code) {
        return sysOrganizationMapper.getByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(SysOrganization sysOrganization) {
        Integer userId = ShiroUtils.getUserId();
        if (sysOrganization.getCode() == null) {
            //获取父级
            SysOrganization parent = sysOrganizationMapper.getByCode(sysOrganization.getParentCode());
            //新增
            sysOrganization.setCreateOperId(userId);
            sysOrganization.setCreateTime(new Date());
            //获取并校验code值
            sysOrganization.setCode(createOrgCode(parent.getTopOrgCode(), sysOrganization.getLevel()));
            if (StrUtils.isEmpty(sysOrganization.getAlias())) {
                sysOrganization.setAlias(sysOrganization.getName());
            }
            //设置full_name,full_code
            sysOrganization.setFullName(parent.getFullName() + "|" + sysOrganization.getName());
            sysOrganization.setFullCode(parent.getFullCode() + "|" + sysOrganization.getCode());
            sysOrganization.setTopOrgCode(parent.getTopOrgCode());
            if (sysOrganization.getSeq() == null) {
                sysOrganization.setSeq(getNextSeq(sysOrganization.getLevel(), sysOrganization.getParentCode()));
            }
            sysOrganizationMapper.insert(sysOrganization);
        } else {
            //修改
            sysOrganization.setUpdateOperId(userId);
            sysOrganization.setUpdateTime(new Date());
            sysOrganizationMapper.updateById(sysOrganization);
        }
        return sysOrganization.getCode();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnableByCodes(Long[] codes, Integer enable) {
        Integer userId = ShiroUtils.getUserId();
        List<Long> idList = Arrays.asList(codes);
        //修改当前记录
        sysOrganizationMapper.updateEnableByCodes(idList, userId, enable);
        for (Long code : codes) {
            //查询所有关联子项
            List<Long> list = sysOrganizationMapper.queryAllChildCodes(code);
            if (list.size() == 0) {
                continue;
            }
            //修改子项
            sysOrganizationMapper.updateEnableByCodes(list, userId, enable);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByCodes(Long[] codes) {
        List<Long> idList = Arrays.asList(codes);
        //删除当前记录
        int deleteIds = sysOrganizationMapper.deleteByCodes(idList);
        for (Long code : codes) {
            //查询所有关联子项
            List<Long> list = sysOrganizationMapper.queryAllChildCodes(code);
            if (list.size() == 0) {
                continue;
            }
            //删除子项
            deleteIds += sysOrganizationMapper.deleteByCodes(list);
        }
        return deleteIds;
    }

    @Override
    public int getNextSeq(Integer level, Long parentCode) {
        Integer seq = sysOrganizationMapper.getMaxSeqByLevelAndParentCode(level, parentCode);
        return seq == null ? 10000 : seq + 1;
    }

    @Override
    public List<SysOrganization> queryCityOrgs() {
        List<SysOrganization> sysOrganizations = new ArrayList<>();
        QueryWrapper<SysOrganization> queryWrapper = new QueryWrapper<>();
        Long topOrgCode = ShiroUtils.getTopOrgCode();
        if (topOrgCode == SysConstant.MAX_ORG_CODE) {
            //省级组织，返回所有地市组织编码
            queryWrapper.select("code,name").eq("level", SysConstant.ORG_LEVEL_CITY);
            sysOrganizations = sysOrganizationMapper.selectList(queryWrapper);
        } else {
            queryWrapper.select("code,name").eq("code", topOrgCode);
            sysOrganizations.add(sysOrganizationMapper.selectOne(queryWrapper));
        }
        return sysOrganizations;
    }

    private List<SysOrganization> buildTree(List<SysOrganization> organizations) {
        List<SysOrganization> result = new ArrayList<>();
        Map<Long, SysOrganization> permissionMap = new HashMap<>();
        for (SysOrganization organization : organizations) {
            organization.setChildren(new ArrayList<>());
            permissionMap.put(organization.getCode(), organization);
            Long parentCode = organization.getParentCode();
            if (parentCode == SysConstant.MAX_ORG_CODE) {
                result.add(organization);
            } else {
                SysOrganization parent = permissionMap.get(parentCode);
                if (parent != null) {
                    parent.getChildren().add(organization);
                }
            }
        }
        return result;
    }

    private static Long createOrgCode(Long topOrgCode, Integer level) {
        String[] arr = {"", "0", "00"};
        int preLen = 3;
        //取组织编码前3位
        String str = String.valueOf(Math.abs(topOrgCode));
        if (str.length() < preLen) {
            str += arr[preLen - str.length()];
        } else {
            str = str.substring(0, preLen);
        }
        //等级1位
        str += level;
        //时分秒
        str += DateUtils.toString(new Date(), "MMddHHmmss");
        return StrUtils.toLong(str);
    }

}

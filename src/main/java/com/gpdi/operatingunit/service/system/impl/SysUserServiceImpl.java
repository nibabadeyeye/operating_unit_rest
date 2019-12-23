package com.gpdi.operatingunit.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.controller.system.support.SysConstant;
import com.gpdi.operatingunit.dao.system.*;
import com.gpdi.operatingunit.entity.system.*;
import com.gpdi.operatingunit.service.system.SysUserService;
import com.gpdi.operatingunit.utils.ShiroUtils;
import io.swagger.models.auth.In;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 用户管理
 * @Author: Lxq
 * @Date: 2019/9/6 10:48
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public SysUser queryUserById(Integer id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public SysUser queryUserByUserName(String userName) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        return sysUserMapper.selectOne(queryWrapper);
    }


    @Override
    public Map<String, Object> pageQuery(QueryParams queryParams) {
        // 获取登录用户的顶级组织
        SysUser user = ShiroUtils.getUser();
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        // 如何顶级组织编码是-1，代表超级管理员
        if (user.getOrgCode() == -1L) {
            queryWrapper.select("*")
                    .and(wapper -> wapper.like("name", queryParams.getSearchText()));
        } else {
            // 获取当前关系组织以及以下组织的编码
            QueryWrapper<SysOrganization> sysOrganizationQueryWrapper = new QueryWrapper<>();
            sysOrganizationQueryWrapper.select("*").eq("enable", 1).eq("top_org_code",user.getTopOrgCode()).orderByDesc("level");
            List<SysOrganization> sysOrganizations = sysOrganizationMapper.selectList(sysOrganizationQueryWrapper);
            List<Long> codeList = sysOrganizations.stream().map(SysOrganization::getCode).collect(Collectors.toList());
            queryWrapper.in("org_code", codeList)
                    .and(wapper -> wapper.like("name", queryParams.getSearchText()));
        }
        // 查询第1页,每页显示10条
        Page<SysUser> page = new Page<>(queryParams.getPageNumber(), queryParams.getPageSize());
        IPage<SysUser> iPage = sysUserMapper.selectPage(page, queryWrapper);
        List<SysUser> userList = new ArrayList<>();
        iPage.getRecords().forEach(item -> {
            item.setPassword("");
            item.setSalt("");
            userList.add(item);
        });
        //处理用户对应的角色名称

        map.put("records", userList);
        map.put("total", iPage.getTotal());
        return map;
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        // 删除用户关联的角色
        ids.forEach(id -> {
            UpdateWrapper<RelUserRole> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", id);
            relUserRoleMapper.delete(updateWrapper);
        });
        return sysUserMapper.deleteBatchIds(ids);
    }

    @Autowired
    private SysDictMapper sysDictMapper;

    @Autowired
    private RelUserRoleMapper relUserRoleMapper;

    @Override
    public int save(SysUser sysUser) {
        SysUser user = ShiroUtils.getUser();
        if (sysUser.getId() == null) {
            // 新增
            sysUser.setCreateTime(new Date());
            sysUser.setCreateOperId(user.getId());
            // 获取默认密码加盐
            sysUser.setSalt(getSalt());
            QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cat", "sys.default_password");
            SysDict sysDict = sysDictMapper.selectOne(queryWrapper);
            sysUser.setPassword(new Sha256Hash(sysDict.getValue(), sysUser.getSalt(), 3).toString());
            int insert = sysUserMapper.insert(sysUser);
            //插入角色
            Integer[] roles = sysUser.getRoles();
            for (int i = 0; i < roles.length; i++) {
                RelUserRole userRole = new RelUserRole();
                userRole.setEnable(1);
                userRole.setUserId(sysUser.getId());
                userRole.setRoleId(roles[i]);
                userRole.setCreateOperId(user.getId());
                userRole.setCreateTime(new Date());
                relUserRoleMapper.insert(userRole);
            }
            return insert;
        } else {
            // 删除原来个角色
            UpdateWrapper<RelUserRole> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", sysUser.getId());
            relUserRoleMapper.delete(updateWrapper);
            //插入角色
            Integer[] roles = sysUser.getRoles();
            for (int i = 0; i < roles.length; i++) {
                RelUserRole userRole = new RelUserRole();
                userRole.setEnable(1);
                userRole.setUserId(sysUser.getId());
                userRole.setRoleId(roles[i]);
                userRole.setCreateOperId(user.getId());
                userRole.setCreateTime(new Date());
                relUserRoleMapper.insert(userRole);
            }
            // 修改
            SysUser oldUser = sysUserMapper.selectById(sysUser.getId());
            sysUser.setPassword(oldUser.getPassword());
            sysUser.setSalt(oldUser.getSalt());
            sysUser.setUpdateOperId(user.getId());
            sysUser.setUpdateTime(new Date());
            return sysUserMapper.updateById(sysUser);
        }
    }

    @Override
    public SysUser uniqueUserName(String userName) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SysOrganization> getOrgCodeData() {
        Long orgCode = ShiroUtils.getOrgCode();
        // 查询所有的组织关系
        QueryWrapper<SysOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*").eq("enable", 1).orderByDesc("level");
        List<SysOrganization> sysOrganizations = sysOrganizationMapper.selectList(queryWrapper);
        Map<Long, List<SysOrganization>> collect = sysOrganizations.stream()
                .collect(Collectors.groupingBy(SysOrganization::getParentCode, Collectors.toList()));
        List<SysOrganization> resultList = new ArrayList<>();
        sysOrganizations.forEach(sysOrg -> {
            sysOrg.setChildren(collect.get(sysOrg.getCode()));
        });
        // 地市
        QueryWrapper<SysOrganization> sysOrganizationQueryWrapper = new QueryWrapper<>();
        sysOrganizationQueryWrapper.eq("level", 2);
        List<SysOrganization> organizationList = sysOrganizationMapper.selectList(sysOrganizationQueryWrapper);
        for (int i = 0; i < sysOrganizations.size(); i++) {
            if (orgCode == -1) {
                for (int j = 0; j < organizationList.size(); j++) {
                    if (sysOrganizations.get(i).getCode().equals(organizationList.get(j).getCode())) {
                        resultList.add(sysOrganizations.get(i));
                    }
                }
            } else {
                if (sysOrganizations.get(i).getCode().equals(orgCode)) {
                    resultList.add(sysOrganizations.get(i));
                }
            }

        }
        return resultList;
    }

    @Override
    public List getRoleOptionData() {
        SysUser user = ShiroUtils.getUser();
        // 获取用户角色信息
        QueryWrapper<RelUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());
        List<RelUserRole> relUserRoles = relUserRoleMapper.selectList(userRoleQueryWrapper);
        List<Integer> roleIds = new ArrayList<>();
        relUserRoles.forEach(relUerRole -> {
            roleIds.add(relUerRole.getRoleId());
        });
        List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
        // 获取level小的角色信息
        List<SysRole> sortList = roles.stream().sorted(Comparator.comparing(SysRole::getLevel)).collect(Collectors.toList());
        //获取用户所有的角色信息
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();
        List<SysRole> userRoles = null;
        if (sortList.get(0).getTopOrgCode() == -1) {
            // 超级管理员
            roleQueryWrapper.select("*").gt("level", sortList.get(0).getLevel()).orderByDesc("level");
            userRoles = sysRoleMapper.selectList(roleQueryWrapper);
        } else {
            roleQueryWrapper.eq("top_org_code", user.getTopOrgCode()).gt("level", sortList.get(0).getLevel()).orderByDesc("level");
            userRoles = sysRoleMapper.selectList(roleQueryWrapper);
        }
        List<SysRole> sysRoleList = new ArrayList<>();
        if (userRoles.size() > 0) {
            Map<Integer, List<SysRole>> parentIdBylist = userRoles.stream().collect(Collectors.groupingBy(SysRole::getParentId, Collectors.toList()));
            userRoles.forEach(role -> {
                role.setChildren(parentIdBylist.get(role.getId()));
            });
            // 获取最小的level
            int asInt = userRoles.stream().mapToInt(SysRole::getLevel).min().getAsInt();
            userRoles.forEach(sysRole -> {
                if (sysRole.getLevel() == asInt) {
                    sysRoleList.add(sysRole);
                }
            });

        }
        return sysRoleList;
    }

    private Set<Integer> parentSet = null;

    @Override
    public Object[] getReShowRoleData(Integer userId) {
        SysUser user = ShiroUtils.getUser();
        QueryWrapper<RelUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<RelUserRole> relUserRoles = relUserRoleMapper.selectList(queryWrapper);
        List<Set<Integer>> result = new ArrayList<>();
        // 获取当前角色最小的level
        List<Integer> roleIds = relUserRoles.stream().map(RelUserRole::getRoleId).collect(Collectors.toList());
        List<SysRole> sysRoles = sysRoleMapper.selectBatchIds(roleIds);
        Integer asInt;
        if (user.getTopOrgCode() == -1) {
            //超级管理员
            asInt = 1;
        } else {
            asInt = sysRoles.stream().filter(sysRole -> sysRole.getTopOrgCode().equals(user.getTopOrgCode()))
                    .mapToInt(SysRole::getId).min().getAsInt();
        }
        Integer finalAsInt = asInt;
        relUserRoles.forEach(relUserRole -> {
            Set<Integer> set = getParentId(relUserRole.getRoleId(), finalAsInt);
            result.add(set);
            parentSet = null;
        });
        Object[] objects = result.stream().toArray();
        return objects;
    }

    /**
     * 获取父id
     *
     * @param roleId 角色id
     * @return
     */
    private Set<Integer> getParentId(Integer roleId, Integer asInt) {
        if (parentSet == null) {
            parentSet = new HashSet<>();
        }
        if (roleId != SysConstant.MAX_PARENT_ID) {
            SysRole role = sysRoleMapper.selectById(roleId);
            if (role.getParentId().equals(asInt)) {
                parentSet.add(role.getId());
            } else {
                parentSet.add(role.getId());
                getParentId(role.getParentId(), asInt);
            }
        }
        return parentSet;
    }


    /**
     * 随机生成密码盐
     *
     * @return
     */
    private String getSalt() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}

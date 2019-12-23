package com.gpdi.operatingunit.service.system.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.dao.system.*;
import com.gpdi.operatingunit.entity.system.*;
import com.gpdi.operatingunit.service.system.SysRoleService;
import com.gpdi.operatingunit.utils.ShiroUtils;
import io.swagger.models.auth.In;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Lxq
 * @Date: 2019/9/7 23:23
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private RelRolePermissionMapper relRolePermissionMapper;

    @Autowired
    private RelUserRoleMapper relUserRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    Set<Integer> roleIdSet = null;

    @Override
    public Map<String, Object> pageQuery(QueryParams queryParams) {
        // 获取登录用户的顶级组织
        SysUser user = ShiroUtils.getUser();
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        // 如何顶级组织编码是-1，代表超级管理员
        if (user.getTopOrgCode() == -1L) {
            queryWrapper.select("*")
                    .and(wapper -> wapper.like("name", queryParams.getSearchText()));
        } else {
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
            roleQueryWrapper.eq("top_org_code", user.getTopOrgCode()).gt("level", sortList.get(0).getLevel()).orderByDesc("level");
            List<SysRole> userRoles = sysRoleMapper.selectList(roleQueryWrapper);

            Map<Integer, List<SysRole>> parentIdBylist = userRoles.stream().collect(Collectors.groupingBy(SysRole::getParentId, Collectors.toList()));
            userRoles.forEach(role -> {
                role.setChildren(parentIdBylist.get(role.getId()));
            });
            // 获取最小的level
            List<SysRole> sysRoleList = new ArrayList<>();
            if (userRoles.size() > 0) {
                int asInt = userRoles.stream().mapToInt(SysRole::getLevel).min().getAsInt();
                userRoles.forEach(sysRole -> {
                    if (sysRole.getLevel() == asInt) {
                        sysRoleList.add(sysRole);
                    }
                });
                getRoleId(sysRoleList);
                queryWrapper.in("id", roleIdSet)
                        .and(wapper -> wapper.like("name", queryParams.getSearchText()));
            }else {
                queryWrapper.in("id", 0)
                        .and(wapper -> wapper.like("name", queryParams.getSearchText()));
            }

        }
        // 查询第1页,每页显示10条
        Page<SysRole> page = new Page<>(queryParams.getPageNumber(), queryParams.getPageSize());
        IPage<SysRole> iPage = sysRoleMapper.selectPage(page, queryWrapper);
        map.put("records", iPage.getRecords());
        map.put("total", iPage.getTotal());
        return map;
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return sysRoleMapper.deleteBatchIds(ids);
    }


    @Override
    public List<SysRole> getUserRoleCascadeData() {
        SysUser user = ShiroUtils.getUser();
        List<SysRole> resultList = null;
        if (user.getTopOrgCode() == -1L) {
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("*").gt("level", 0).orderByDesc("level");
            List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
            Map<Integer, List<SysRole>> parentIdBylist = sysRoles.stream().collect(Collectors.groupingBy(SysRole::getParentId, Collectors.toList()));
            sysRoles.forEach(role -> {
                role.setChildren(parentIdBylist.get(role.getId()));
            });
            List<SysRole> roleList = sysRoles.stream().filter(sysRole -> sysRole.getLevel() == 1).collect(Collectors.toList());
            resultList = roleList;
        } else {
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
            roleQueryWrapper.eq("top_org_code", user.getTopOrgCode()).gt("level", sortList.get(0).getLevel()).orderByDesc("level");
            List<SysRole> userRoles = sysRoleMapper.selectList(roleQueryWrapper);

            Map<Integer, List<SysRole>> parentIdBylist = userRoles.stream().collect(Collectors.groupingBy(SysRole::getParentId, Collectors.toList()));
            userRoles.forEach(role -> {
                role.setChildren(parentIdBylist.get(role.getId()));
            });
            // 获取最小的level
            List<SysRole> sysRoleList = new ArrayList<>();
            int asInt = userRoles.stream().mapToInt(SysRole::getLevel).min().getAsInt();
            userRoles.forEach(sysRole -> {
                if (sysRole.getLevel() == asInt) {
                    sysRoleList.add(sysRole);
                }
            });
            resultList = sysRoleList;
        }
        return resultList;
    }

    @Override
    public int savePermisson(Integer roleId, Integer[] perminssions) {
        // 删除该角色原来权限
        QueryWrapper<RelRolePermission> relRolePermissionQueryWrapper = new QueryWrapper<>();
        relRolePermissionQueryWrapper.eq("role_id", roleId);
        relRolePermissionMapper.delete(relRolePermissionQueryWrapper);
        // 新增选中的权限信息
        int influence = 0;
        for (int i = 0; i < perminssions.length; i++) {
            if (perminssions[i] != -1) {
                RelRolePermission rel = new RelRolePermission();
                rel.setRoleId(roleId);
                rel.setPermissionId(perminssions[i]);
                rel.setEnable(1);
                rel.setCreateOperId(ShiroUtils.getUserId());
                rel.setCreateTime(new Date());
                influence++;
                relRolePermissionMapper.insert(rel);
            }
        }
        return influence;
    }

    @Override
    public Object[] getRolePermisson(Integer roleId) {
        QueryWrapper<RelRolePermission> relRolePermissionQueryWrapper = new QueryWrapper<>();
        relRolePermissionQueryWrapper.eq("role_id", roleId);
        List<RelRolePermission> relRolePermissions = relRolePermissionMapper.selectList(relRolePermissionQueryWrapper);
        // 收集要过滤的id
        List<Integer> filterList = new ArrayList<>();
        List<Object> result = new ArrayList<>();
        if (relRolePermissions.size() > 0) {
            List<Integer> rolePermissions = relRolePermissions.stream().map(RelRolePermission::getPermissionId).collect(Collectors.toList());
            List<SysPermission> sysPermissions = sysPermissionMapper.selectBatchIds(rolePermissions);
            // 按照父id分组
            Map<Integer, List<SysPermission>> userListMap = sysPermissions.stream()
                    .collect(Collectors.groupingBy(SysPermission::getParentId, Collectors.toList()));
            System.out.println(userListMap);

            // 获取所有的权限数据做对比
            QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("*");
            List<SysPermission> allPermissions = sysPermissionMapper.selectList(queryWrapper);
            Map<Integer, List<SysPermission>> allListMap = allPermissions.stream()
                    .collect(Collectors.groupingBy(SysPermission::getParentId, Collectors.toList()));
            System.out.println(allPermissions);

            userListMap.forEach((key, value) -> {
                // 去掉所有顶层id
                if (key == -1) {
                    value.forEach(permission -> {
                        filterList.add(permission.getId());
                    });
                } else {
                    value.forEach(permission -> {
                        if (permission.getLevel() == 2) {
                            List<SysPermission> sysPermissionList = userListMap.get(permission.getId());
                            if (sysPermissionList != null) {
                                List<SysPermission> sysPermissionList1 = allListMap.get(permission.getId());
                                if (sysPermissionList.size() != sysPermissionList1.size()) {
                                    filterList.add(permission.getId());
                                }
                            }
                        }
                    });
                }
            });

            //获取 rolePermissions去重 filterList 结果
            if (filterList.size() > 0) {
                HashSet hs1 = new HashSet(rolePermissions);
                HashSet hs2 = new HashSet(filterList);
                hs1.removeAll(hs2);
                result.addAll(hs1);
            }
        }
        return result.toArray();
    }

    @Override
    public Object[] queryRoleByParentId(Integer parentId) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<SysRole> roles = sysRoleMapper.selectList(queryWrapper);
        List<String> sysRoleNamesList = roles.stream().map(SysRole::getName).collect(Collectors.toList());
        Object[] roleNames = sysRoleNamesList.stream().toArray();
        return roleNames;
    }

    @Override
    public int save(SysRole sysRole) {
        SysUser user = ShiroUtils.getUser();
        if (sysRole.getId() == null) {
            // 新增
            sysRole.setCreateTime(new Date());
            sysRole.setCreateOperId(user.getId());
            sysRole.setParentId(sysRole.getParentId());
            // 获取对应的level
            SysRole role = sysRoleMapper.selectById(sysRole.getParentId());
            sysRole.setLevel(role.getLevel() + 1);
            return sysRoleMapper.insert(sysRole);
        } else {
            // 修改
            sysRole.setUpdateTime(new Date());
            sysRole.setUpdateOperId(user.getId());
            return sysRoleMapper.updateById(sysRole);
        }
    }

    @Override
    public List<SysRole> getparentIdRoleCascadeData() {
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
            roleQueryWrapper.select("*").gt("level", sortList.get(0).getLevel() - 1).orderByDesc("level");
            userRoles = sysRoleMapper.selectList(roleQueryWrapper);
        } else {
            roleQueryWrapper.eq("top_org_code", user.getTopOrgCode()).gt("level", sortList.get(0).getLevel() - 1).orderByDesc("level");
            userRoles = sysRoleMapper.selectList(roleQueryWrapper);
        }

        Map<Integer, List<SysRole>> parentIdBylist = userRoles.stream().collect(Collectors.groupingBy(SysRole::getParentId, Collectors.toList()));
        userRoles.forEach(role -> {
            role.setChildren(parentIdBylist.get(role.getId()));
        });
        // 获取最小的level
        List<SysRole> sysRoleList = new ArrayList<>();
        int asInt = userRoles.stream().mapToInt(SysRole::getLevel).min().getAsInt();
        userRoles.forEach(sysRole -> {
            if (sysRole.getLevel() == asInt) {
                sysRoleList.add(sysRole);
            }
        });

        return sysRoleList;
    }

    Set<Object> idsSet = null;

    @Override
    public Object[] reshowParentId(Integer id) {
        SysUser user = ShiroUtils.getUser();
        QueryWrapper<RelUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());
        List<RelUserRole> relUserRoles = relUserRoleMapper.selectList(userRoleQueryWrapper);
        List<Integer> roleIds = relUserRoles.stream()
                .map(RelUserRole::getRoleId).collect(Collectors.toList());
        List<SysRole> sysRoles = sysRoleMapper.selectBatchIds(roleIds);
        int asInt = sysRoles.stream().filter(sysRole -> sysRole.getTopOrgCode().equals(user.getTopOrgCode()))
                .mapToInt(SysRole::getId).min().getAsInt();
        SysRole sysRole = sysRoleMapper.selectById(id);

        List<Object> ids = new ArrayList<>();
        if (sysRole.getParentId() == asInt) {
            ids.add(sysRole.getParentId());
            return ids.toArray();
        } else {
            idsSet = null;
            getParentIdArray(sysRole, asInt);
            return idsSet.toArray();
        }
    }


    private void getParentIdArray(SysRole sysRole, int asInt) {
        if (idsSet == null) {
            idsSet = new HashSet<>();
        }
        idsSet.add(sysRole.getParentId());
        SysRole sysRole1 = sysRoleMapper.selectById(sysRole.getParentId());
        if (asInt == sysRole1.getParentId()) {
            idsSet.add(sysRole1.getParentId());
        } else {
            getParentIdArray(sysRole1, asInt);
        }
    }

    private void getRoleId(List<SysRole> sysRoles) {
        if (roleIdSet == null) {
            roleIdSet = new HashSet<>();
        }
        if (sysRoles.size() > 0) {
            for (int i = 0; i < sysRoles.size(); i++) {
                roleIdSet.add(sysRoles.get(i).getId());
                if (sysRoles.get(i).getChildren() != null) {
                    getRoleId(sysRoles.get(i).getChildren());
                }
            }
        }
    }

    private List<SysRole> buildRoles(List<SysRole> sysRoles) {
        List<SysRole> result = new ArrayList<>();
        Map<Integer, SysRole> sysRoleMap = new HashMap<>();
        for (SysRole sysRole : sysRoles) {
            sysRole.setChildren(new ArrayList<>());
            sysRoleMap.put(sysRole.getId(), sysRole);
            Integer parentId = sysRole.getParentId();
            if (parentId == -1) {
                result.add(sysRole);
            } else {
                SysRole parent = sysRoleMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(sysRole);
                }
            }
        }
        return result;
    }


}

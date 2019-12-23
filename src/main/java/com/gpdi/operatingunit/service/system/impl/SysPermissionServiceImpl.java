package com.gpdi.operatingunit.service.system.impl;

import com.gpdi.operatingunit.controller.system.support.SysConstant;
import com.gpdi.operatingunit.dao.system.SysPermissionMapper;
import com.gpdi.operatingunit.entity.system.SysPermission;
import com.gpdi.operatingunit.service.system.SysPermissionService;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author Lxq
 * @Date 2019/6/26 17:21
 **/
@Service
public class SysPermissionServiceImpl implements SysPermissionService {


    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> getUserPermission(Integer userId) {
        List<SysPermission> sysPermissions = sysPermissionMapper.queryUserPermission(userId);
        List<SysPermission> result = buildTree(sysPermissions);
        return result;
    }

    @Override
    public List<SysPermission> getPermissionData() {
        return sysPermissionMapper.queryPermissionData();
    }

    @Override
    public List<SysPermission> queryAllPermission() {
        List<SysPermission> permissions = sysPermissionMapper.queryAllPermission();
        return buildTree(permissions);
    }

    @Override
    public SysPermission getById(Integer id) {
        return sysPermissionMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(SysPermission sysPermission) {
        Integer userId = ShiroUtils.getUserId();
        if (sysPermission.getId() == null) {
            //新增
            sysPermission.setCreateOperId(userId);
            sysPermission.setCreateTime(new Date());
            if (sysPermission.getSeq() == null) {
                sysPermission.setSeq(getNextSeq(sysPermission.getLevel(), sysPermission.getParentId()));
            }
            sysPermissionMapper.insert(sysPermission);
        } else {
            //修改
            sysPermission.setUpdateOperId(userId);
            sysPermission.setUpdateTime(new Date());
            sysPermissionMapper.updateById(sysPermission);
        }
        return sysPermission.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnableByIds(Integer[] ids, Integer enable) {
        Integer userId = ShiroUtils.getUserId();
        List<Integer> idList = Arrays.asList(ids);
        //修改当前记录
        sysPermissionMapper.updateEnableByIds(idList, userId, enable);
        for (Integer id : ids) {
            //查询所有关联子项
            List<Integer> list = sysPermissionMapper.queryAllChildIds(id);
            if (list.size() == 0) {
                continue;
            }
            //修改子项
            sysPermissionMapper.updateEnableByIds(list, userId, enable);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        //删除当前记录
        int deleteIds = sysPermissionMapper.deleteBatchIds(idList);
        for (Integer id : ids) {
            //查询所有关联子项
            List<Integer> list = sysPermissionMapper.queryAllChildIds(id);
            if (list.size() == 0) {
                continue;
            }
            //删除子项
            deleteIds += sysPermissionMapper.deleteBatchIds(list);
        }
        return deleteIds;
    }

    @Override
    public int getNextSeq(Integer level, Integer parentId) {
        Integer seq = sysPermissionMapper.getMaxSeqByLevelAndParentId(level, parentId);
        return seq == null ? 10000 : seq + 1;
    }

    @Override
    public List<SysPermission> queryPermissionByUserId(Integer userId) {
        return sysPermissionMapper.queryUserPermission(userId);
    }

    @Override
    public List<SysPermission> queryPermissionByUserIdAndLevel(Integer userId, Integer level) {
        return sysPermissionMapper.queryPermissionByUserIdAndLevel(userId, level);
    }

    @Override
    public Map<String, Object> getCurrentUserPerms() {
        Map<String, Object> map = new HashMap<>(3);
        List<SysPermission> permissions = sysPermissionMapper.queryUserPermission(ShiroUtils.getUserId());
        List<SysPermission> routers = new ArrayList<>();
        Map<String, Object> perms = new LinkedHashMap<>();
        Map<String, SysPermission> menus = new LinkedHashMap<>();
        Map<Integer, SysPermission> permissionMap = new HashMap<>();
        for (SysPermission permission : permissions) {
            permission.setChildren(new ArrayList<>());
            permissionMap.put(permission.getId(), permission);
            Integer parentId = permission.getParentId();
            if (parentId == SysConstant.MAX_PERMS_ID) {
                routers.add(permission);
            } else {
                SysPermission parent = permissionMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(permission);
                }
            }
            if (permission.getLevel() == SysConstant.PERMS_LEVEL_BUTTON) {
                perms.put(permission.getPerms(), true);
            } else {
                menus.put(permission.getUrl(), permission);
            }
        }
        map.put("menus", menus);
        map.put("perms", perms);
        map.put("routers", routers);
        return map;
    }

    private List<SysPermission> buildTree(List<SysPermission> permissions) {
        List<SysPermission> result = new ArrayList<>();
        Map<Integer, SysPermission> permissionMap = new HashMap<>();
        for (SysPermission permission : permissions) {
            permission.setChildren(new ArrayList<>());
            permissionMap.put(permission.getId(), permission);
            Integer parentId = permission.getParentId();
            if (parentId == -1) {
                result.add(permission);
            } else {
                SysPermission parent = permissionMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(permission);
                }
            }
        }
        return result;
    }

}

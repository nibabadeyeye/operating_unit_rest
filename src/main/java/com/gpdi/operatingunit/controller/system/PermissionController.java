package com.gpdi.operatingunit.controller.system;


import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.system.SysPermission;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.system.SysPermissionService;
import com.gpdi.operatingunit.utils.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhb
 * @date 2019/10/23 16:43
 */
@RestController
@RequestMapping("/permission")
@Api(description = "用户权限控制管理控制器")
public class PermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 当前端页面传过来的的String类型的日期与后台实体类的Date类型不匹配时，需要加上该方法
     */
    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }

    @GetMapping("/queryUserPermission")
    @ApiOperation("获取用户拥有的菜单信息")
    public R queryUserPermission(Integer id) {
        try {
            // 获取用户拥有的菜单信息
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            Map<String, Object> map = new HashMap<>();
            List<SysPermission> userPermission = sysPermissionService.getUserPermission(user.getId());
            map.put("menus", userPermission);
            return R.ok(map);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/queryPermissionData")
    @ApiOperation("获取用户的菜单信息数据")
    public R queryPermissionData() {
        SysUser user = ShiroUtils.getUser();
        // 查询所有的菜单信息
        List<SysPermission> permissionData = sysPermissionService.getUserPermission(user.getId());
        return R.ok(permissionData);
    }

    @GetMapping("/queryAllPermission")
    @ApiOperation("获取全部菜单数据")
    public R queryAllPermission() {
        Map<String, Object> map = new HashMap<>(3);
        map.put("perms", sysPermissionService.queryAllPermission());
        return R.ok(map);
    }

    @PostMapping("/queryOne")
    @ApiOperation("根据id获取权限数据")
    @RequiresPermissions("system_permission_edit")
    public R queryOne(Integer id) {
        SysPermission sysPermission = sysPermissionService.getById(id);
        return R.ok(sysPermission);
    }

    @PostMapping("/save")
    @ApiOperation("保存权限数据")
    @RequiresPermissions("system_permission_add")
    public R save(SysPermission sysPermission) {
        Integer save = sysPermissionService.save(sysPermission);
        return R.ok(save);
    }

    @PostMapping("/changeEnable")
    @ApiOperation("修改是否启用状态")
    @RequiresPermissions("system_permission_enable")
    public R changeEnable(Integer[] ids, Integer enable) {
        sysPermissionService.updateEnableByIds(ids, enable);
        return R.ok();
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据id删除权限数据")
    @RequiresPermissions("system_permission_delete")
    public R deleteByIds(Integer[] ids) {
        int delete = sysPermissionService.deleteByIds(ids);
        return R.ok(delete);
    }

    @GetMapping("/getCurrentUserPerms")
    @ApiOperation("获取当前用户权限")
    public R getCurrentUserPerms() {
        Map<String, Object> userPerms = sysPermissionService.getCurrentUserPerms();
        return R.ok(userPerms);
    }
}

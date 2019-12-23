package com.gpdi.operatingunit.controller.system;


import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.entity.system.RelRolePermission;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysPermission;
import com.gpdi.operatingunit.entity.system.SysRole;
import com.gpdi.operatingunit.service.system.SysOrganizationService;
import com.gpdi.operatingunit.service.system.SysPermissionService;
import com.gpdi.operatingunit.service.system.SysRoleService;
import com.gpdi.operatingunit.service.system.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色管理
 *
 * @Author: Lxq
 * @Date: 2019/9/7 23:32
 */
@RestController
@Api(description = "角色查询管理控制器类")
@RequestMapping("/sysRole")
public class RoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysOrganizationService sysOrganizationService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 当前端页面传过来的的String类型的日期与后台实体类的Date类型不匹配时，需要加上该方法
     */
    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }

    @PostMapping("/pageQuery")
    @ApiOperation("分页查询角色管理数据")
    public R pageQuery(QueryParams queryParams) {
        Map<String, Object> map = sysRoleService.pageQuery(queryParams);
        return R.ok(map);
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据id批量删除用户信息")
    public R deleteByIds(Integer[] ids) {
        sysRoleService.deleteByIds(Arrays.asList(ids));
        return R.ok();
    }

    @GetMapping("/getAllPermissionData")
    @ApiOperation("获取树权限全部信息")
    public R getAllPermission() {
        List<SysPermission> allSysPermissionData = sysPermissionService.queryAllPermission();
        return R.ok(allSysPermissionData);
    }


    @GetMapping("/getUserRoleCascadeData")
    @ApiOperation("获取用户的角色级联数据")
    public R getUserRoleCascadeData() {
        List<SysRole> userRoleCascadeData = sysRoleService.getUserRoleCascadeData();
        return R.ok(userRoleCascadeData);
    }

    @PostMapping("/savePermisson")
    @ApiOperation("保存/修改角色权限")
    public R savePermisson(Integer roleId, Integer[] perminssions) {
        int permisson = sysRoleService.savePermisson(roleId, perminssions);
        return R.ok(permisson);
    }

    @PostMapping("getRolePermisson")
    @ApiOperation("获取角色权限(回显树权限)")
    public R getRolePermisson(Integer roleId){
        Object[] rolePermisson = sysRoleService.getRolePermisson(roleId);
        return R.ok(rolePermisson);
    }
    @GetMapping("/queryOrgs")
    @ApiOperation("查询组织")
    public R queryOrgs() {
        List<SysOrganization> list = sysOrganizationService.queryAllLocalCity();
        return R.ok(list);
    }

    @PostMapping("queryRoleByParentId")
    @ApiOperation("查询父级下面有多少角色")
    public R queryRoleByParentId(Integer parentId){
        Object[] roleNames = sysRoleService.queryRoleByParentId(parentId);
        return R.ok(roleNames);
    }

    @PostMapping("/save")
    @ApiOperation("保存或修改角色信息")
    public R save(SysRole sysRole) {
        int save = sysRoleService.save(sysRole);
        return R.ok(save);
    }

    @GetMapping("getOrgCodeOptionData")
    @ApiOperation("获取当前用户以及以下的组织关系数据")
    public R getOrgCodeData() {
        List<SysOrganization> sysOrganizations = sysUserService.getOrgCodeData();
        return R.ok(sysOrganizations);
    }

    @GetMapping("getparentIdRoleCascadeData")
    @ApiOperation("获取当前用户拥有的角色以及以下角色")
    public R getparentIdRoleCascadeData() {
        List<SysRole> sysRoles = sysRoleService.getparentIdRoleCascadeData();
        return R.ok(sysRoles);
    }


    @PostMapping("/reshowParentId")
    @ApiOperation("角色管理修改回显父级数据")
    public R reshowParentId(Integer id) {
        Object[] ids = sysRoleService.reshowParentId(id);
        return R.ok(ids);
    }


}

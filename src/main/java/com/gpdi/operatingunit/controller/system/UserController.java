package com.gpdi.operatingunit.controller.system;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysRole;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.system.SysOrganizationService;
import com.gpdi.operatingunit.service.system.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @description: 用户管理控制类
 * @author: Lxq
 * @date: 2019/11/1 16:42
 */
@RestController
@RequestMapping("sysUser")
@Api(description = "用户管理控制器")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysOrganizationService sysOrganizationService;

    /**
     * 当前端页面传过来的的String类型的日期与后台实体类的Date类型不匹配时，需要加上该方法
     */
    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }


    @PostMapping("/pageQuery")
    @ApiOperation("分页查询用户管理数据")
    public R pageQuery(QueryParams queryParams) {
        Map<String, Object> map = sysUserService.pageQuery(queryParams);
        return R.ok(map);
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据id批量删除用户信息")
    public R deleteByIds(Integer[] ids) {
        sysUserService.deleteByIds(Arrays.asList(ids));
        return R.ok();
    }

    @PostMapping("/save")
    @ApiOperation("保存或修改用户信息")
    public R save(SysUser sysUser) {
        int save = sysUserService.save(sysUser);
        return R.ok(save);
    }

    @GetMapping("/queryOrgs")
    @ApiOperation("查询组织")
    public R queryOrgs() {
        List<SysOrganization> list = sysOrganizationService.queryAllLocalCity();
        return R.ok(list);
    }

    @PostMapping("/queryUserNameUnique")
    @ApiOperation("查询该用户名是否唯一可用")
    public R queryUserNameUnique(String userName) {
        SysUser sysUser = sysUserService.uniqueUserName(userName);
        if (sysUser != null) {
            sysUser.setSalt("");
            sysUser.setPassword("");
        }
        return R.ok(sysUser);
    }

    @GetMapping("getOrgCodeOptionData")
    @ApiOperation("获取当前用户以及以下的组织关系数据")
    public R getOrgCodeData() {
        List<SysOrganization> sysOrganizations = sysUserService.getOrgCodeData();
        return R.ok(sysOrganizations);
    }

    @GetMapping("getRoleOptionData")
    @ApiOperation("获取当前用户的角色以及以下角色信息数据")
    public R getRoleOptionData() {
        List<SysRole> sysRoleList = sysUserService.getRoleOptionData();
        return R.ok(sysRoleList);
    }

    @PostMapping("getReShowRoleData")
    @ApiOperation("获取用户角色数据（修改回显角色数据）")
    public R getReShowRoleData(Integer userId) {
        Object[] rolesIds = sysUserService.getReShowRoleData(userId);
        return R.ok(rolesIds);
    }

    @GetMapping("queryOrgCodeNames")
    @ApiOperation("查询所有组织关系")
    public R queryAllOrganization(){
        List<SysOrganization> sysOrganizations = sysOrganizationService.queryAllSysOrganization();
        Map<Long, SysOrganization> collect = sysOrganizations.stream().collect(Collectors.toMap(SysOrganization::getCode, s -> s));
        return R.ok(collect);
    }

}

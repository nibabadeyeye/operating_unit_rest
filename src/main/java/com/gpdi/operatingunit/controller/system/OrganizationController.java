package com.gpdi.operatingunit.controller.system;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.service.system.SysDictService;
import com.gpdi.operatingunit.service.system.SysOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @date 2019/11/5 10:26
 */
@RestController
@Api(description = "组织机构管理控制器类")
@RequestMapping("/sysOrg")
public class OrganizationController {

    @Autowired
    private SysOrganizationService sysOrganizationService;
    @Autowired
    private SysDictService sysDictService;

    @GetMapping("/queryLocalCity")
    public R queryLocalCity() {
        List<SysOrganization> sysOrganizations = sysOrganizationService.queryAllLocalCity();
        Map<Long, Object> resultMap = new HashMap<>();
        sysOrganizations.forEach(sysorg -> resultMap.put(sysorg.getCode(), sysorg));
        return R.ok(resultMap);
    }

    /**
     * 当前端页面传过来的的String类型的日期与后台实体类的Date类型不匹配时，需要加上该方法
     */
    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }

    @GetMapping("/querySelections")
    @ApiOperation("获取选择框数据")
    public R querySelections() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("cities", sysOrganizationService.queryCityOrgs());
        map.put("levels", sysDictService.queryMapByCat(SysOrganization.ORG_LEVEL_CAT));
        return R.ok(map);
    }

    @PostMapping("/queryList")
    @ApiOperation("获取全部菜单数据")
    public R queryList(QueryParams queryParams) {
        return R.ok(sysOrganizationService.queryList(queryParams));
    }

    @PostMapping("/queryOne")
    @ApiOperation("根据id获取权限数据")
    public R queryOne(Long code) {
        SysOrganization sysOrganization = sysOrganizationService.getByCode(code);
        return R.ok(sysOrganization);
    }

    @PostMapping("/save")
    @ApiOperation("保存权限数据")
    public R save(SysOrganization sysOrganization) {
        Long save = sysOrganizationService.save(sysOrganization);
        return R.ok(save);
    }

    @PostMapping("/changeEnable")
    @ApiOperation("修改是否启用状态")
    public R changeEnable(Long[] codes, Integer enable) {
        sysOrganizationService.updateEnableByCodes(codes, enable);
        return R.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("根据id删除权限数据")
    public R delete(Long[] codes) {
//        int delete = sysOrganizationService.deleteByCodes(codes);
        return R.ok();
    }
}

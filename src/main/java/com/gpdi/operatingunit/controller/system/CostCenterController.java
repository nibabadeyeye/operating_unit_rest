package com.gpdi.operatingunit.controller.system;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.entity.system.SysCostCenter;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.service.system.CostCenterService;
import com.gpdi.operatingunit.service.system.SysOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zhb
 * @date 2019/10/28 17:45
 **/
@RestController
@RequestMapping("/costCenter")
@Api(description = "成本中心管理控制器")
public class CostCenterController {

    @Autowired
    private CostCenterService costCenterService;
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

    @PostMapping("/uploadExcel")
    @ApiOperation("成本中心excel文件上传")
    public R uploadExcel(@RequestParam(value = "file") MultipartFile file) throws Exception {
        return costCenterService.uploadExcel(file);
    }

    @PostMapping("/pageQuery")
    @ApiOperation("分页查询成本中心数据")
    public R pageQuery(QueryParams queryParams) {
        Map<String, Object> map = costCenterService.pageQuery(queryParams);
        return R.ok(map);
    }

    @GetMapping("/getById")
    @ApiOperation("根据id获取成本中心")
    public R getById(Long id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("info", costCenterService.getById(id));
        map.put("orgs", sysOrganizationService.querySysOrganization());
        return R.ok(map);
    }


    @PostMapping("/save")
    @ApiOperation("保存或修改成本中心")
    public R save(SysCostCenter sysCostCenter) {
        costCenterService.save(sysCostCenter);
        return R.ok();
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据id批量删除成本中心")
    public R deleteByIds(Long[] ids) {
        costCenterService.deleteByIds(Arrays.asList(ids));
        return R.ok();
    }

    @GetMapping("/queryOrgs")
    @ApiOperation("查询组织")
    public R queryOrgs() {
        List<SysOrganization> list = sysOrganizationService.querySysOrganization();
        return R.ok(list);
    }

}

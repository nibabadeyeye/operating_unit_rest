package com.gpdi.operatingunit.controller.business;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.service.business.GrossService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:毛利报表
 * @author: Lxq
 * @date: 2019/11/20 11:27
 */
@RestController
@Api(description = "毛利展示报表管理控制器类")
@RequestMapping("/gross")
public class GrossController {


    @Autowired
    private GrossService grossService;

    @GetMapping("/getColumn")
    @ApiOperation("毛利展示报表表头数据")
    public R getColumn() {
        List<SysReportColumn> sysReportColumns = grossService.queryReportColumn();
        return R.ok(sysReportColumns);
    }
}

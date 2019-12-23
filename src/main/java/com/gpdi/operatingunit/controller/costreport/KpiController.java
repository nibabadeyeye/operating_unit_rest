package com.gpdi.operatingunit.controller.costreport;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.service.costreport.KpiService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/kpi") //kpi/getSysOrgTree
@ApiOperation(value = "绩效考核")
public class KpiController {


    @ApiOperation("导入kpi数据")
    @RequestMapping("/uploadKpiData")
    public R uploadKpiData(MultipartFile file) throws Exception {
        return kpiService.uploadKpiData(file.getInputStream());
    }

    @ApiOperation("查询组织树信息")
    @RequestMapping("/getSysOrgTree")
    public R getSysOrgTree() {
        return kpiService.getSysOrgTree();
    }

    @ApiOperation("根据营服名称查询kpi信息")
    @RequestMapping("/getKpiByServiceName")
    public R getKpiByServiceName(String serviceName) {
        return kpiService.getServiceApiByServiceName(serviceName);
    }

    @Autowired
    private KpiService kpiService;


}

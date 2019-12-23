package com.gpdi.operatingunit.service.costreport;

import com.gpdi.operatingunit.config.R;
import io.swagger.annotations.Api;

import java.io.InputStream;

@Api("/绩效考核接口")
public interface KpiService {

    //绩效考核报表上传
    public R uploadKpiData(InputStream in);

    //根据用户信息查询组织数信息
    public R getSysOrgTree();

    //查询营服Api信息
    public R getServiceApiByServiceName(String serviceName);


}

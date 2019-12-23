package com.gpdi.operatingunit.dao.costreport;


import com.gpdi.operatingunit.entity.costreport.Kpi;
import java.util.List;

public interface KpiMapper {

    //批量插入kpi数据
    public void batchInsetKpiData(List<Kpi> kpi);

    //查询Kpi数据
    public List<Kpi> getKpiData();

    //查询营服Api信息
    public List<Kpi> getServiceApiByServiceName(String serviceName);
}

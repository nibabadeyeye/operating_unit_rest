package com.gpdi.operatingunit.service.evaluation;

import com.gpdi.operatingunit.entity.relation.RelReportColumnValue;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.service.data.QueryData;

import java.util.List;
import java.util.Map;

/**
 * @Author: zyb
 * @Date: 2019/11/15 9:39
 */
public interface BenefitsService {

    List<SysReportColumn> getColumns(Integer reportId, Long topOrgCode);

    List<Map<Object, Object>> getTableData(QueryData queryData, String code, Long topOrgCode);

    List<RelReportColumnValue> getMonth(Integer reportId, SysOrganization sysOrganization);

    List<SysOrganization> getCitys();

    String ifExist(QueryData queryData, Long topOrgCode);
}

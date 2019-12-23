package com.gpdi.operatingunit.service.business;

import com.gpdi.operatingunit.entity.system.SysReportColumn;

import java.util.List;

/**
 * @description: 毛利报表
 * @author: Lxq
 * @date: 2019/11/20 11:29
 */
public interface GrossService {

    /**
     * 业务量展示报表表头数据
     *
     * @return
     */
    List<SysReportColumn> queryReportColumn();
}

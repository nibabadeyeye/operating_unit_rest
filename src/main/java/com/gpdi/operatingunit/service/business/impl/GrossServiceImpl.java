package com.gpdi.operatingunit.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.system.SysReportColumnMapper;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.service.business.GrossService;
import com.gpdi.operatingunit.utils.GeneralReportColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: 毛利报表
 * @author: Lxq
 * @date: 2019/11/20 11:30
 */
@Service
@Transactional
public class GrossServiceImpl implements GrossService {

    @Autowired
    private SysReportColumnMapper sysReportColumnMapper;

    @Override
    public List<SysReportColumn> queryReportColumn() {
        QueryWrapper<SysReportColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", 3);
        List<SysReportColumn> sysReportColumns = sysReportColumnMapper.selectList(queryWrapper);
        List<SysReportColumn> reportTitleColumn = GeneralReportColumn.getReportTitleColumn(sysReportColumns);
        return reportTitleColumn;
    }
}

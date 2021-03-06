package com.gpdi.operatingunit.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.business.IncomeMapper;
import com.gpdi.operatingunit.dao.relation.RelReportColumnValueMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.dao.system.SysReportColumnMapper;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.business.IncomeService;
import com.gpdi.operatingunit.utils.GeneralReportColumn;
import com.gpdi.operatingunit.utils.ProductionExcelUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 收入展示报表
 * @author: Lxq
 * @date: 2019/11/19 10:55
 */
@Service
@Transactional
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private SysReportColumnMapper sysReportColumnMapper;

    @Autowired
    private RelReportColumnValueMapper relReportColumnValueMapper;

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Autowired
    private IncomeMapper incomeMapper;

    @Override
    public List<SysReportColumn> queryReportColumn() {
        QueryWrapper<SysReportColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", 2);
        List<SysReportColumn> sysReportColumns = sysReportColumnMapper.selectList(queryWrapper);
        List<SysReportColumn> reportTitleColumn = GeneralReportColumn.getReportTitleColumn(sysReportColumns);
        return reportTitleColumn;
    }

    @Override
    public int maxMonth() {
        return relReportColumnValueMapper.incomeMaxMonth();
    }

    @Override
    public List<Map<Object, Object>> queryIncomeData(Integer month) {
        // 判断登录人信息
        SysUser user = ShiroUtils.getUser();
        QueryWrapper<SysOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", user.getOrgCode());
        SysOrganization sysOrganization = sysOrganizationMapper.selectOne(queryWrapper);
        if (sysOrganization.getLevel() == 1) {
            // 省层级
            return null;
        } else if (sysOrganization.getLevel() == 2) {
            // 市层级
            List<Map<Object, Object>> parentList = relReportColumnValueMapper.queryIncomeCityCenterList(month, user.getTopOrgCode());
            // 市级对应区对应营服数据
            List<Map<Object, Object>> mapList = relReportColumnValueMapper.innerToIncomeCityCenterList(month, user.getTopOrgCode());
            //给所有的市级加上children
            parentList.forEach(parent -> {
                parent.put("children", null);
                parent.put("serviceCenter", parent.get("district"));
                List<Map<Object, Object>> sonList = new ArrayList<>();
                mapList.forEach(son -> {
                    if (parent.get("parentCode").equals(son.get("parentCode"))) {
                        sonList.add(son);
                    }
                });
                parent.put("children", sonList);
            });
            return parentList;
        } else if (sysOrganization.getLevel() == 3) {
            //区级数据
            List<Map<Object, Object>> parentList = relReportColumnValueMapper.queryIncomeDistrictCenterList(month, user.getTopOrgCode(), user.getOrgCode());
            List<Map<Object, Object>> mapList = relReportColumnValueMapper.innerToIncomeDistrictCenterList(month, user.getTopOrgCode(), user.getOrgCode());
            //给所有的市级加上children
            parentList.forEach(parent -> {
                parent.put("children", null);
                parent.put("serviceCenter", parent.get("district"));
                List<Map<Object, Object>> sonList = new ArrayList<>();
                mapList.forEach(son -> {
                    if (parent.get("parentCode").equals(son.get("parentCode"))) {
                        sonList.add(son);
                    }
                });
                parent.put("children", sonList);
            });
            return parentList;
        } else {
            // 营服以及以下层级
            List<Map<Object, Object>> mapList = relReportColumnValueMapper.queryIncomeServiceCenterList(month, user.getTopOrgCode(), user.getOrgCode());
            return mapList;
        }
    }

    @Override
    public List<Integer> monthList() {
        return incomeMapper.monthList();
    }
}


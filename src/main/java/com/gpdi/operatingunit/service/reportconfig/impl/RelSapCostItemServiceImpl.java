package com.gpdi.operatingunit.service.reportconfig.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gpdi.operatingunit.dao.reportconfig.RelSapCostItemMapper;
import com.gpdi.operatingunit.entity.reportconfig.RelSapCostItem;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.reportconfig.RelSapCostItemService;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/10/25 16:51
 */
@Service
@Transactional
public class RelSapCostItemServiceImpl implements RelSapCostItemService {

    @Autowired
    private RelSapCostItemMapper relSapCostItemMapper;

    @Override
    public int relSapCostItemUpload(List<Object> list, Integer reportId) {
        SysUser user = ShiroUtils.getUser();
        //删除原数据
        deleteByOrgCodeAndReportId(user.getTopOrgCode(), reportId);
        int index = 0;
        Integer year = DateUtils.getYear(new Date());
        for (int i = 1; i < list.size(); i++) {
            List<Object> objs = (List<Object>) list.get(i);
            Object sapCode = objs.get(0);
            if (sapCode == null) {
                continue;
            }
            Object costItem = objs.get(2);
            RelSapCostItem rel = new RelSapCostItem();
            rel.setOrgCode(user.getTopOrgCode());
            rel.setCostItemId(0L);
            rel.setReportId(reportId);
            rel.setYear(year);
            rel.setSapCode(Long.parseLong((String) sapCode));
            if (costItem == null) {
                rel.setCostItem("未知成本项" + DateUtils.toString(new Date()));
            } else {
                rel.setCostItem(costItem.toString().trim());
            }
            if (objs.get(3) != null) {
                rel.setHandlingDesc((String) objs.get(3));
            }
            rel.setEnable(1);
            rel.setCreateOperId(user.getId());
            rel.setUpdateOperId(user.getId());
            rel.setCreateTime(new Date());
            rel.setUpdateTime(new Date());
            int insert = relSapCostItemMapper.insert(rel);
            index += insert;
        }
        updateRelSapCostItem(reportId, user.getTopOrgCode(), year);
        return index;
    }

    /**
     * 根据orgCode、reportId 删除数据
     */
    private int deleteByOrgCodeAndReportId(Long orgCode, Integer reportId) {
        UpdateWrapper<RelSapCostItem> wrapper = new UpdateWrapper<>();
        wrapper.eq("org_code", orgCode);
        wrapper.eq("report_id", reportId);
        return relSapCostItemMapper.delete(wrapper);
    }

    /**
     * SAP科目与成本项目关系表和成本项目表关联
     *
     * @return
     */
    private int updateRelSapCostItem(Integer reportId, Long topOrgCode, Integer year) {
        return relSapCostItemMapper.relConfig(reportId, topOrgCode, year);
    }

}

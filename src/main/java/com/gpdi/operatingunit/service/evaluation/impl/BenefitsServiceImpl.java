package com.gpdi.operatingunit.service.evaluation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.relation.RelReportColumnValueMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.dao.system.SysReportColumnMapper;
import com.gpdi.operatingunit.entity.relation.RelReportColumnValue;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.data.QueryData;
import com.gpdi.operatingunit.service.evaluation.BenefitsService;
import com.gpdi.operatingunit.utils.GeneralReportColumn;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zyb
 * @Date: 2019/11/15 9:40
 */
@Service
@Transactional
public class BenefitsServiceImpl implements BenefitsService{

    @Autowired
    private RelReportColumnValueMapper relReportColumnValueMapper;
    @Autowired
    private SysReportColumnMapper sysReportColumnMapper;
    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Override
    public String ifExist(QueryData queryData, Long topOrgCode) {
        Integer month = relReportColumnValueMapper.getIfExist(queryData.getMonth(), queryData.getReportId(),topOrgCode);
        String str;
        if(month != null && month.equals(queryData.getMonth())){
            str = "success";
        }else{
            str = "fail";
        }
        return str;
    }

    @Override
    public List<Map<Object, Object>> getTableData(QueryData queryData, String code, Long topOrgCode) {
        SysUser user = ShiroUtils.getUser();

        List<Map<Object,Object>> result = new ArrayList<>();

        //判断角色等级
        QueryWrapper<SysOrganization> wq = new QueryWrapper<>();
        wq.select("*").eq("code",code);
        SysOrganization rel = sysOrganizationMapper.selectOne(wq);

        //获取需要赋值的列名
        List<SysReportColumn> column = getColumn(queryData.getReportId(),topOrgCode);
        //获取所有的区
        List<SysOrganization> district = getDistrict(rel,topOrgCode);
        //获取数据列表
        Map<String,RelReportColumnValue> valueMap = getMapValue(queryData,rel,topOrgCode);
        DecimalFormat df = new DecimalFormat("0.00%");


        if (rel.getLevel() == 4){
            Map<Long, List<SysOrganization>> center = getCenter(rel,topOrgCode);
            List<SysOrganization> centerList = center.get(rel.getCode());
            for (SysOrganization c : centerList) {
                Map<Object,Object> mm = new HashMap<>();
                mm.put("id",c.getCode());
                mm.put("center",c.getAlias());
                mm.put("parentCode",c.getCode());
                for (SysReportColumn col : column) {
                    if (!col.getProp().contains("Percent")){
                        mm.put(col.getProp(),getValue(valueMap.get(c.getParentCode()+"_"+c.getCode()+"_"+col.getProp())));
                    }else{
                        String value = getValue(valueMap.get(c.getParentCode() + "_" + c.getCode() + "_" + col.getProp()));
                        mm.put(col.getProp(),df.format(new BigDecimal(value)));
                    }
                }
                result.add(mm);
            }
        }else{
            for (SysOrganization d : district) {
                Map<Object,Object> map = new HashMap<>();
                map.put("id",d.getCode());
                map.put("center",d.getAlias());
                map.put("parentCode",d.getCode());
                for (SysReportColumn col : column) {
                    if(!col.getProp().contains("Percent")){
                        map.put(col.getProp(),getValue(valueMap.get(d.getCode()+"_"+d.getCode()+"_"+col.getProp())));
                    }else{
                        String value = getValue(valueMap.get(d.getCode()+"_"+d.getCode()+"_"+col.getProp()));
                        map.put(col.getProp(),df.format(new BigDecimal(value)));
                    }
                }
                List<Map<Object,Object>> list = new ArrayList<>();
                map.put("children",list);
                result.add(map);
            }
            //获取区对应的营服
            Map<Long, List<SysOrganization>> centerMap = getCenter(rel, topOrgCode);
            for (Map<Object, Object> r : result) {
                Long parentCode = (Long) r.get("parentCode");
                List<SysOrganization> centers = centerMap.get(parentCode);
                List<Map<Object,Object>> children = (List<Map<Object, Object>>) r.get("children");
                for (SysOrganization c : centers) {
                    Map<Object,Object> m = new HashMap<>();
                    m.put("id",c.getCode());
                    m.put("center",c.getAlias());
                    for (SysReportColumn col : column) {
                        if (!col.getProp().contains("Percent")){
                            m.put(col.getProp(),getValue(valueMap.get(c.getParentCode()+"_"+c.getCode()+"_"+col.getProp())));
                        }else{
                            String value = getValue(valueMap.get(c.getParentCode()+"_"+c.getCode()+"_"+col.getProp()));
                            m.put(col.getProp(),df.format(new BigDecimal(value)));
                        }
                    }
                    children.add(m);
                }
                r.put("children",children);
            }
        }
        return result;
    }


    /**
     * 获取报表表头
     * @param reportId
     * @param topOrgCode
     * @return
     */
    @Override
    public List<SysReportColumn> getColumns(Integer reportId, Long topOrgCode) {
        QueryWrapper<SysReportColumn> qw = new QueryWrapper<>();
        qw.select("*").eq("report_id",reportId).gt("level",1).
                eq("top_org_code",topOrgCode).eq("enabled",1).
                orderByAsc("level,parent_id,seq");
        List<SysReportColumn> list = sysReportColumnMapper.selectList(qw);
        List<SysReportColumn> columns = GeneralReportColumn.getReportTitleColumn(list);
        return columns;
    }

    /**
     * 获取当前报表的费用集合
     * @param reportId
     * @param sysOrganization
     * @return
     */
    @Override
    public List<RelReportColumnValue> getMonth(Integer reportId, SysOrganization sysOrganization) {
        List<RelReportColumnValue> result = relReportColumnValueMapper.selectMonth(reportId,sysOrganization.getTopOrgCode());
        return result;
    }


    /**
     * 获取城市列表
     * @return
     */
    @Override
    public List<SysOrganization> getCitys() {
        List<SysOrganization> result = null;
        Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();
        if (topOrgCode == -1){
            QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
            qw.select("*").eq("parent_code",topOrgCode).orderByAsc("seq");
            result = sysOrganizationMapper.selectList(qw);
        }else{
            QueryWrapper<SysOrganization> wq = new QueryWrapper<>();
            wq.select("*").eq("code",topOrgCode);
            result = sysOrganizationMapper.selectList(wq);
        }
        return result;
    }

    /**
     * 获取需要赋值的列名
     * @param reportId
     * @param topOrgCode
     * @return
     */
    private List<SysReportColumn> getColumn(Integer reportId, Long topOrgCode) {
        QueryWrapper<SysReportColumn> qw = new QueryWrapper<>();
        qw.select("*").eq("level",3).eq("report_id",reportId)
                .eq("top_org_code",topOrgCode).eq("enabled",1);
        List<SysReportColumn> column = sysReportColumnMapper.selectList(qw);
        return column;
    }

    /**
     * 获取区
     * @param rel
     * @param topOrgCode
     * @return
     */
    private List<SysOrganization> getDistrict(SysOrganization rel, Long topOrgCode){
        List<SysOrganization> district = new ArrayList<>();
        if (rel.getLevel()==4){
            district.add(rel);
        }else{
            QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
            qw.select("*").eq("level",3).eq("top_org_code",topOrgCode).eq("enable",1).orderByAsc("seq");
            district = sysOrganizationMapper.selectList(qw);
        }
        return district;
    }

    /**
     * 获取map形式的数据
     * @param queryData
     * @param rel
     * @param topOrgCode
     * @return
     */
    public Map<String,RelReportColumnValue> getMapValue(QueryData queryData, SysOrganization rel, Long topOrgCode){
        Map<String,RelReportColumnValue> map = new HashMap<>();
        String districtName = null;
        String centerName = null;
        if (rel.getLevel() == 3){
            districtName = String.valueOf(rel.getCode());
        }else if(rel.getLevel() ==4){
            centerName = String.valueOf(rel.getCode());
        }
        List<RelReportColumnValue> list = relReportColumnValueMapper.getBenefitsList(queryData.getMonth(),
                queryData.getReportId(),districtName,centerName,topOrgCode);
        for (RelReportColumnValue r : list) {
            map.put(r.getDistrictCode()+"_"+r.getCenterCode()+"_"+r.getProp(),r);
        }
        return map;
    }

    /**
     * 获取区对应的营服中心
     * @param rel
     * @param topOrgCode
     * @return
     */
    private Map<Long,List<SysOrganization>> getCenter(SysOrganization rel, Long topOrgCode){
        Map<Long,List<SysOrganization>> center = new HashMap<>();
        if (rel.getLevel() == 3){
            //判断是营服还是还是区，区为3，营服为4
            QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
            qw.select("*").eq("parent_code",rel.getCode()).notLike("alias","本部").
                    eq("top_org_code",topOrgCode).eq("enable",1).orderByAsc("level,parent_code,seq");
            List<SysOrganization> list = sysOrganizationMapper.selectList(qw);
            center.put(rel.getCode(),list);
        }else if(rel.getLevel() == 4){
            QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
            qw.select("*").eq("org_code",rel.getCode()).orderByAsc("level,parent_code,seq");
            List<SysOrganization> list = sysOrganizationMapper.selectList(qw);
            center.put(rel.getCode(),list);
        }else{
            QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
            qw.select("*").eq("level",4).notLike("alias","本部").
                    eq("top_org_code",topOrgCode).eq("enable",1).orderByAsc("level,parent_code,seq");
            List<SysOrganization> list = sysOrganizationMapper.selectList(qw);
            for (SysOrganization r : list) {
                List<SysOrganization> value = center.get(r.getParentCode());
                if (value == null){
                    value = new ArrayList<>();
                    value.add(r);
                }else{
                    value.add(r);
                }
                center.put(r.getParentCode(),value);
            }
        }
        return center;
    }

    private String getValue(RelReportColumnValue r){
        return r.getFinalValue();
    }


}

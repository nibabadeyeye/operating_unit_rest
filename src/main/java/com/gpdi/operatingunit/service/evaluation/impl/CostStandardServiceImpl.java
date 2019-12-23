package com.gpdi.operatingunit.service.evaluation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.relation.RelReportColumnValueMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.dao.system.SysReportColumnMapper;
import com.gpdi.operatingunit.entity.relation.RelReportColumnValue;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.service.data.QueryData;
import com.gpdi.operatingunit.service.evaluation.CostStandardService;
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
 * @Date: 2019/11/15 16:29
 */
@Service
@Transactional
public class CostStandardServiceImpl implements CostStandardService {

    @Autowired
    private RelReportColumnValueMapper relReportColumnValueMapper;
    @Autowired
    private SysReportColumnMapper sysReportColumnMapper;
    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    /**
     * 判断月份是否存在
     * @param queryData
     * @param topOrgCode
     * @return
     */
    @Override
    public String ifExist(QueryData queryData, Long topOrgCode) {
        Integer ifExist = relReportColumnValueMapper.getIfExist(queryData.getMonth(), queryData.getReportId(), topOrgCode);
        String str;
        if (ifExist != null && ifExist.equals(queryData.getMonth())){
            str = "success";
        }else{
            str = "fail";
        }
        return str;
    }

    /**
     * 展示数据
     * @param queryData
     * @param code
     * @return
     */
    @Override
    public List<Map<Object, Object>> getTableData(QueryData queryData, String code ,Long topOrgCode) {

        List<Map<Object,Object>> result = new ArrayList<>();
        //获取表头
        List<SysReportColumn> columns = getColumn(queryData.getReportId(),topOrgCode);
        //获取角色等级
        QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
        qw.select("*").eq("code",code);
        SysOrganization rel = sysOrganizationMapper.selectOne(qw);
        //获取区
        List<SysOrganization> district = getDistrict(rel,topOrgCode);
        //获取数据
        Map<String,RelReportColumnValue> valueMap = getValueMap(queryData,rel,topOrgCode);

        DecimalFormat df1 = new DecimalFormat("0.00");
        DecimalFormat df2 = new DecimalFormat("0.00%");

        if (rel.getLevel() == 4){
            Map<Long, List<SysOrganization>> center = getCenter(rel,topOrgCode);
            List<SysOrganization> centerList = center.get(rel.getCode());
            for (SysOrganization c : centerList) {
                Map<Object,Object> mm = new HashMap<>();
                mm.put("id",c.getCode());
                mm.put("center",c.getAlias());
                mm.put("parentCode",c.getCode());
                for (SysReportColumn column : columns) {
                    if (!column.getProp().equals("center")){
                        String b;
                        String value = getValue(valueMap.get(c.getParentCode() + "_" + c.getCode() + "_" + column.getProp()));
                        if (column.getUom().equals("%")){
                            b = df2.format(new BigDecimal(value));
                        }else if(column.getUom().equals("‰")){
                            b = df1.format(new BigDecimal(value).multiply(new BigDecimal(1000)))+"‰";
                        }else if(column.getUom().equals("万元")){
                            b = df1.format(new BigDecimal(value).divide(new BigDecimal(10000)));
                        }else{
                            b = df1.format(new BigDecimal(value));
                        }
                        mm.put(column.getProp(),b);
                    }
                }
                result.add(mm);
            }
        }else{
            for (SysOrganization d : district) {
                Map<Object,Object> m1 = new HashMap<>();
                m1.put("id",d.getCode());
                m1.put("center",d.getAlias());
                m1.put("parentCode",d.getCode());
                for (SysReportColumn column : columns) {
                    if (!column.getProp().equals("center")){
                        String b;
                        String value = getValue(valueMap.get(d.getCode() + "_" + d.getCode() + "_" + column.getProp()));
                        if (column.getUom().equals("%")){
                            b = df2.format(new BigDecimal(value));
                        }else if(column.getUom().equals("‰")){
                            b = df1.format(new BigDecimal(value).multiply(new BigDecimal(1000)))+"‰";
                        }else if(column.getUom().equals("万元")){
                            b = df1.format(new BigDecimal(value).divide(new BigDecimal(10000)));
                        }else{
                            b = df1.format(new BigDecimal(value));
                        }
                        m1.put(column.getProp(),b);
                    }
                }
                List<Map<Object,Object>> list = new ArrayList<>();
                m1.put("children",list);
                result.add(m1);
            }
            Map<Long, List<SysOrganization>> centerMap = getCenter(rel,topOrgCode);
            for (Map<Object, Object> r : result) {
                Object parentCode = r.get("parentCode");
                List<SysOrganization> center = centerMap.get(parentCode);
                List<Map<Object,Object>> children = (List<Map<Object, Object>>) r.get("children");
                for (SysOrganization c : center) {
                    Map<Object,Object> m2 = new HashMap<>();
                    m2.put("id",c.getCode());
                    m2.put("center",c.getAlias());
                    for (SysReportColumn column : columns) {
                        if (!column.getProp().equals("center")){
                            String b;
                            String value = getValue(valueMap.get(c.getParentCode() + "_" + c.getCode() + "_" + column.getProp()));
                            if (column.getUom().equals("%")){
                                b = df2.format(new BigDecimal(value));
                            }else if(column.getUom().equals("‰")){
                                b = df1.format(new BigDecimal(value).multiply(new BigDecimal(1000)))+"‰";
                            }else if(column.getUom().equals("万元")){
                                b = df1.format(new BigDecimal(value).divide(new BigDecimal(10000)));
                            }else{
                                b = df1.format(new BigDecimal(value));
                            }
                            m2.put(column.getProp(),b);
                        }
                    }
                    children.add(m2);
                }
                r.put("children",children);
            }
        }
        return result;
    }

    /**
     * 获取表头
     * @param reportId
     * @return
     */
    @Override
    public List<SysReportColumn> getColumn(Integer reportId,Long topOrgCode) {
        QueryWrapper<SysReportColumn> qw = new QueryWrapper<>();
        qw.select("*").eq("report_id",reportId).notIn("level",1).
                eq("top_org_code",topOrgCode).eq("enabled",1).
                orderByAsc("seq");
        List<SysReportColumn> columns = sysReportColumnMapper.selectList(qw);
        return columns;
    }

    /**
     * 获取月份集合
     * @param reportId
     * @param sysOrganization
     * @return
     */
    @Override
    public List<RelReportColumnValue> getMonth(Integer reportId, SysOrganization sysOrganization) {
        List<RelReportColumnValue> result = relReportColumnValueMapper.selectMonth(reportId, sysOrganization.getTopOrgCode());
        return result;
    }

    /**
     * 获取城市列表
     * @return
     */
    @Override
    public List<SysOrganization> getCityList() {
        List<SysOrganization> result;
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
            qw.select("*").eq("level",3).eq("top_org_code",topOrgCode).eq("enable",1).
                    orderByAsc("level,parent_code,seq");
            district = sysOrganizationMapper.selectList(qw);
        }
        return district;
    }

    /**获取map形式的结果数据
     * @param queryData
     * @param rel
     * @return
     */
    private Map<String, RelReportColumnValue> getValueMap(QueryData queryData, SysOrganization rel,Long topOrgCode) {
        Map<String,RelReportColumnValue> map = new HashMap<>();
        String districtName = null;
        String centerName = null;
        if (rel.getLevel() == 3){
            districtName = String.valueOf(rel.getCode());
        }else if(rel.getLevel() ==4){
            centerName = String.valueOf(rel.getCode());
        }
        List<RelReportColumnValue> list = relReportColumnValueMapper.getCostList(queryData.getMonth(),
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
    private Map<Long,List<SysOrganization>> getCenter(SysOrganization rel, Long topOrgCode) {
        Map<Long, List<SysOrganization>> center = new HashMap<>();
        if (rel.getLevel() == 3) {
            //判断是营服还是还是区，区为3，营服为4
            QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
            qw.select("*").eq("parent_code", rel.getCode()).notLike("alias", "本部").
                    eq("top_org_code", topOrgCode).eq("enable", 1).orderByAsc("level,parent_code,seq");
            List<SysOrganization> list = sysOrganizationMapper.selectList(qw);
            center.put(rel.getCode(), list);
        } else if (rel.getLevel() == 4) {
            QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
            qw.select("*").eq("org_code", rel.getCode()).orderByAsc("level,parent_code,seq");
            List<SysOrganization> list = sysOrganizationMapper.selectList(qw);
            center.put(rel.getCode(), list);
        } else {
            QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
            qw.select("*").eq("level", 4).notLike("alias", "本部").
                    eq("top_org_code", topOrgCode).eq("enable", 1).orderByAsc("level,parent_code,seq");
            List<SysOrganization> list = sysOrganizationMapper.selectList(qw);
            for (SysOrganization r : list) {
                List<SysOrganization> value = center.get(r.getParentCode());
                if (value == null) {
                    value = new ArrayList<>();
                    value.add(r);
                } else {
                    value.add(r);
                }
                center.put(r.getParentCode(), value);
            }
        }
        return center;
    }

    private String getValue(RelReportColumnValue r){
        return r.getFinalValue();
    }
}

package com.gpdi.operatingunit.service.reportconfig.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.reportconfig.RsCostReportDao;
import com.gpdi.operatingunit.dao.reportconfig.SysCostItemMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.service.data.QueryData;
import com.gpdi.operatingunit.service.reportconfig.CityService;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Author: zyb
 * @Date: 2019/11/19 17:57
 */
@Service
@Transactional
public class CityServiceImpl implements CityService {

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;
    @Autowired
    private RsCostReportDao rsCostReportDao;
    @Autowired
    private SysCostItemMapper sysCostItemMapper;

    /**
     * 获取市报表数据
     * @param queryData
     * @param topOrgCode
     * @return
     */
    @Override
    public List<Map<Object, Object>> getData(QueryData queryData, Long topOrgCode) {
        //获取表头
        List<SysCostItem> column = getColumns(queryData.getReportId(), topOrgCode);
        List<SysCostItem> columns = new ArrayList<>();
        for (SysCostItem item : column) {
            if (item.getName().equals("合计")){
                columns.add(item);
            }
            if (item.getItemsChildren()!=null){
                for (SysCostItem costItem : item.getItemsChildren()) {
                    columns.add(costItem);
                }
            }
        }

        //获取区
        List<SysOrganization> district = getDistrict(topOrgCode);
        //获取数据
        Map<String,RsCostReportEntity> valueMap = getValueMap(queryData,topOrgCode);

        DecimalFormat df = new DecimalFormat("0.00");
        //生成存放结果的集合
        List<Map<Object, Object>> result = new ArrayList<>();
        for (SysOrganization org : district) {
            Map<Object,Object> map = new LinkedHashMap<>();
            map.put("id",org.getCode());
            map.put("center",org.getAlias());
            map.put("centerCode",org.getCode());
            for (SysCostItem col : columns) {
                if (col.getLevel() == 1 && !col.getName().equals("合计")){

                }else{
                    Object value = getValue(valueMap.get(org.getCode()+"_"+org.getCode()+"_"+col.getId()));
                    String format = df.format(new BigDecimal(value.toString()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_UP));
                    map.put(col.getId(),format);
                }
            }
            List<Map<Object,Object>> list = new ArrayList<>();
            map.put("children",list);
            result.add(map);
        }

        Map<Long, List<SysOrganization>> centerMap = getCenter(topOrgCode);
        for (Map<Object, Object> res : result) {
            Long centerCode = (Long) res.get("centerCode");
            List<SysOrganization> center = centerMap.get(centerCode);
            List<Map<Object,Object>> children = (List<Map<Object, Object>>) res.get("children");
            if(center != null){
                for (SysOrganization cen : center) {
                    Map<Object,Object> mm = new LinkedHashMap<>();
                    mm.put("id",cen.getCode());
                    mm.put("center",cen.getAlias());
                    mm.put("centerCode",cen.getCode());
                    for (SysCostItem col : columns) {
                        if (col.getLevel() == 1 && !col.getName().equals("合计")){

                        }else{
                            Object value = getValue(valueMap.get(cen.getCode()+"_"+cen.getParentCode()+"_"+col.getId()));
                            String format = df.format(new BigDecimal(value.toString()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_UP));
                            mm.put(col.getId(),format);
                        }
                    }
                    children.add(mm);
                }
            }
            res.put("children",children);
        }
        return result;
    }

    /**
     * 获取表头集合
     * @param reportId
     * @param topOrgCode
     * @return
     */
    @Override
    public List<SysCostItem> getColumns(Integer reportId, Long topOrgCode) {
        QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
        qw.select("*").eq("report_id",reportId).eq("org_code",topOrgCode)
                .eq("enable",1).orderByAsc("level,parent_id,seq");
        List<SysCostItem> itemList = sysCostItemMapper.selectList(qw);
        List<SysCostItem> result = buildTree(itemList);
        for (SysCostItem item : result) {
            if (item.getItemsChildren()!= null){
                item.getItemsChildren().add(0,getSysCostItem(2,item.getId()));
            }
        }
        result.add(0,getSysCostItem(1, Long.valueOf(-1)));
        return result;
    }

    /**
     * 获取月份集合
     * @param reportId
     * @param topOrgCode
     * @return
     */
    @Override
    public List<RsCostReportEntity> getMonthList(Integer reportId, Long topOrgCode) {
        QueryWrapper<RsCostReportEntity> qw = new QueryWrapper<>();
        qw.select("distinct month,month as id").eq("report_id",reportId).eq("top_org_code",topOrgCode).orderByDesc("month");
        List<RsCostReportEntity> result = rsCostReportDao.selectList(qw);
        return result;
    }

    /**
     * 获取最小月份
     * @param reportId
     * @param topOrgCode
     * @param mon
     * @return
     */
    @Override
    public Integer getMinMonth(Integer reportId, Long topOrgCode, String mon) {
        Integer minMonth = rsCostReportDao.getMinMonth(reportId,topOrgCode,mon);
        return minMonth;
    }

    /**
     * 获取市报表的最大月份
     * @param reportId
     * @param topOrgCode
     * @return
     */
    @Override
    public Integer getMaxMonth(Integer reportId, Long topOrgCode) {
        Integer maxMonth = rsCostReportDao.getMaxMonth(reportId,topOrgCode);
        return maxMonth;
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
     * 把结果构造成树结构
     *
     * @param sysCostItems
     * @return
     */
    private List<SysCostItem> buildTree(List<SysCostItem> sysCostItems) {
        List<SysCostItem> result = new ArrayList<>();
        Map<Long, SysCostItem> itemMap = new HashMap<>();
        for (SysCostItem item : sysCostItems) {
            itemMap.put(item.getId(), item);
            Long parentId = item.getParentId();
            if (parentId == -1) {
                result.add(item);
            } else {
                SysCostItem parent = itemMap.get(parentId);
                if (parent != null) {
                    if (parent.getItemsChildren() == null){
                        parent.setItemsChildren(new ArrayList<>());
                    }
                    parent.getItemsChildren().add(item);
                }
            }
        }
        return result;
    }

    public SysCostItem getSysCostItem(Integer level,Long id){
        SysCostItem sysCostItem = new SysCostItem();
        sysCostItem.setName("合计");
        sysCostItem.setLevel(level);
        sysCostItem.setId(id);
        return sysCostItem;
    }


    /**
     * 获取区
     * @param topOrgCode
     * @return
     */
    private List<SysOrganization> getDistrict(Long topOrgCode){
        List<SysOrganization> district = new ArrayList<>();
        QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
        qw.select("*").eq("level",3).eq("top_org_code",topOrgCode).eq("enable",1).orderByAsc("seq");
        district = sysOrganizationMapper.selectList(qw);
        return district;
    }


    /**
     * 获取区对应的营服中心
     * @param
     * @param topOrgCode
     * @return
     */
    private Map<Long,List<SysOrganization>> getCenter(Long topOrgCode){
        Map<Long,List<SysOrganization>> center = new HashMap<>();
        QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
        qw.select("*").eq("level",4).eq("top_org_code",topOrgCode)
                .eq("enable",1).orderByAsc("level,parent_code,seq");
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
        return center;
    }

    /**
     * 获取map形式的数据
     * @param queryData
     * @param topOrgCode
     * @return
     */
    public Map<String,RsCostReportEntity> getValueMap(QueryData queryData,Long topOrgCode){
        List<RsCostReportEntity> result = new ArrayList<>();
        //获取费用数据
        List<RsCostReportEntity> cityValue = rsCostReportDao.getCityValue(queryData.getReportId(),queryData.getFromMonth(),queryData.getToMonth(),topOrgCode);
        result.addAll(cityValue);
        //获取成本集合数据
        List<RsCostReportEntity> cityCountValue = rsCostReportDao.getCityCountValue(queryData.getReportId(),queryData.getFromMonth(),queryData.getToMonth(),topOrgCode);
        result.addAll(cityCountValue);
        //获取成本
        List<RsCostReportEntity> cityAllCountValue = rsCostReportDao.getAllCountValue(queryData.getReportId(),queryData.getFromMonth(),queryData.getToMonth(),topOrgCode);
        result.addAll(cityAllCountValue);

        Map<String,RsCostReportEntity> map = new HashMap<>();
        for (RsCostReportEntity rs : result) {
            map.put(rs.getOrgCode()+"_"+rs.getParentOrgCode()+"_"+rs.getCostItemId(),rs);
        }
        return map;
    }

    /**
     * 数据处理
     * @param o
     * @return
     */
    private Object getValue(RsCostReportEntity o) {
        return o == null ? 0 : o.getCurrMonthValue();
    }
}

package com.gpdi.operatingunit.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.dao.reportconfig.DataSapListMapper;
import com.gpdi.operatingunit.dao.reportconfig.RelSapCostItemMapper;
import com.gpdi.operatingunit.dao.reportconfig.RsCostReportDao;
import com.gpdi.operatingunit.dao.reportconfig.SysCostItemMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.entity.common.DataSapList;
import com.gpdi.operatingunit.entity.reportconfig.RelSapCostItem;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.RelSapCostItemSubitem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: zyb
 * @Date: 2019/11/7 11:07
 */
@Service
@Transactional
public class DataGenerateUtils {

    @Autowired
    private RsCostReportDao rsCostReportDao;
    @Autowired
    private RelSapCostItemMapper relSapCostItemMapper;
    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;
    @Autowired
    private SysCostItemMapper sysCostItemMapper;
    @Autowired
    private DataSapListMapper dataSapListMapper;

    /**
     * 获取sap表的数据
     *
     * @param itemId
     * @param start
     * @param end
     * @param orgCode
     * @param topOrgCode
     * @param reportId
     * @return
     */
    public List<DataSapList> getSapList(Long itemId, Integer start, Integer end, Long orgCode, Long topOrgCode, Integer reportId) {
        Integer startYear = start / 100;
        Integer startPer = start % 100;

        Integer endYear = end / 100;
        Integer endPer = end % 100;

        QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
        qw.select("*").eq("code", orgCode);
        SysOrganization org = sysOrganizationMapper.selectOne(qw);
        List<DataSapList> result = new ArrayList<>();


        if (org.getLevel() == 4) {
            //获取没有条件限制的sap清单,营服级
            List<DataSapList> sapList1 = dataSapListMapper.getSapList1(itemId, startYear, endYear, startPer, endPer, orgCode, topOrgCode, reportId);
            result.addAll(sapList1);
        } else {
            //获取区级sap清单
            List<DataSapList> sapList3 = dataSapListMapper.getSapList3(itemId, startYear, endYear, startPer, endPer, orgCode, topOrgCode, reportId);
            result.addAll(sapList3);
        }

        //获取有条件限制的sap清单
        List<RelSapCostItem> itemList = relSapCostItemMapper.getSubitemByItem(itemId, endYear, reportId, topOrgCode);
        for (RelSapCostItem rel : itemList) {
            StringBuilder sb = new StringBuilder();
            sb.append(" a.cost_element = " + rel.getSapCode());
            for (RelSapCostItemSubitem subitem : rel.getSubitemList()) {
                if (subitem.getOperations().contains("like")) {
                    sb.append(" and a." + subitem.getFieldName() + " " + subitem.getOperations() + " '%" + subitem.getParams() + "%'");
                } else {
                    sb.append(" and a." + subitem.getFieldName() + " " + subitem.getOperations() + " '" + subitem.getParams() + "'");
                }
            }
            //营服级sap
            if (org.getLevel() == 4) {
                List<DataSapList> sapList2 = dataSapListMapper.getSapList2(startYear, endYear, startPer, endPer, orgCode, sb);
                result.addAll(sapList2);
            } else {
                List<DataSapList> sapList4 = dataSapListMapper.getSapList4(startYear, endYear, startPer, endPer, orgCode, sb);
                result.addAll(sapList4);
            }
        }
        return result;
    }

    public R insertQingYuanCountyValue(Integer month, Integer reportId) {
        //本年
        Integer year = month / 100;
        //本月
        Integer per = month % 100;
        //获取地市的编码
        Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();

        //判断当前月份是否存在数据，有的话则删除
        rsCostReportDao.deleteByMonth(month, topOrgCode, reportId);

        QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
        qw.select("*").eq("org_code", topOrgCode).eq("report_id", reportId).orderByAsc("level,seq");
        List<SysCostItem> itemLists = sysCostItemMapper.selectList(qw);
        List<SysCostItem> items = buildTree(itemLists);

        //取出没有父子级别的费用的本二月数
        //从sap表中获取出没有条件限制的本月数
        List<RsCostReportEntity> rsList = rsCostReportDao.getCountyCurrentMonthValue1(year, per, reportId, topOrgCode);

        //从sap表中被条件限制的本月数
        //0.用map去存数据对象
        Map<String, RsCostReportEntity> map = new HashMap<>();
        //1.先查询什么费用由什么条件限制
        List<RelSapCostItem> itemList = relSapCostItemMapper.getCountySubitemByItemId(year, reportId, topOrgCode);
        for (RelSapCostItem item : itemList) {
            StringBuilder sb = new StringBuilder();
            sb.append(" a.cost_element = " + item.getSapCode());
            for (RelSapCostItemSubitem subitem : item.getSubitemList()) {
                if (subitem.getOperations().contains("like")) {
                    sb.append(" and a." + subitem.getFieldName() + " " + subitem.getOperations() + " '%" + subitem.getParams() + "%'");
                } else {
                    sb.append(" and a." + subitem.getFieldName() + " " + subitem.getOperations() + " '" + subitem.getParams() + "'");
                }
            }
            //2.根据条件从sap表中查询对应的本月数
            List<RsCostReportEntity> rsCostList = rsCostReportDao.getCurrentMonthValue2(year, per, reportId, topOrgCode, item.getCostItemId(), sb);
            //3.根据条件取值，然后放到map中去
            for (RsCostReportEntity rs : rsCostList) {
                String key = rs.getOrgCode() + "-" + rs.getParentOrgCode() + "_" + rs.getCostItemId();
                if (!map.containsKey(key)) {
                    map.put(key, rs);
                } else {
                    RsCostReportEntity r = map.get(key);
                    r.setCurrMonthValue(new BigDecimal(r.getCurrMonthValue()).add(new BigDecimal(rs.getCurrMonthValue())).toPlainString());
                    map.put(key, r);
                }
            }
        }
        //4.将map中的结果放入本月数中
        for (String s : map.keySet()) {
            rsList.add(map.get(s));
        }
        Map<String, RsCostReportEntity> vMap = new HashMap<>();

        for (RsCostReportEntity rs : rsList) {
            rs.setCurrMonthValue(new BigDecimal(rs.getCurrMonthValue()).toPlainString());
            vMap.put(rs.getOrgCode() + "-" + rs.getParentOrgCode() + "_" + rs.getCostItemId(), rs);
        }
        //5.查询当前市的所有营服
        List<SysOrganization> oList = sysOrganizationMapper.getAllCenter(topOrgCode + "%");

        //6.查询当前所有费用名称
        QueryWrapper<SysCostItem> wq = new QueryWrapper<>();
        wq.select("id,name").eq("org_code", topOrgCode).eq("report_id", reportId).eq("enable", 1);
        List<SysCostItem> item = sysCostItemMapper.selectList(wq);
        //判断营服本月是否有数据，没有的话数据置为0
        for (SysOrganization o : oList) {
            for (SysCostItem i : item) {
                if (!vMap.containsKey(o.getCode() + "-" + o.getParentCode() + "_" + i.getId())) {
                    RsCostReportEntity entity = new RsCostReportEntity();
                    entity.setMonth(month);
                    entity.setCurrMonthValue("0");
                    entity.setBudget("0");
                    entity.setTopOrgCode(topOrgCode);
                    entity.setParentOrgCode(o.getParentCode());
                    entity.setOrgCode(o.getCode());
                    entity.setReportId(reportId);
                    entity.setCostItemId(i.getId());
                    rsList.add(entity);
                }
            }
        }

        for (RsCostReportEntity rs : rsList) {
            vMap.put(rs.getOrgCode()+"-"+rs.getParentOrgCode()+"_"+rs.getCostItemId(),rs);
        }
        Map<String, List<Long>> idMap = new LinkedHashMap<>();
        Map<String, List<Long>> count = getCount(idMap, items);

        for (String s : count.keySet()) {
            List<Long> cou = count.get(s);
            for (String key : vMap.keySet()) {
                if (key.contains("_"+s)){
                    String[] split = key.split("_");
                    String cost = split[0];
                    String id = split[1];
                    RsCostReportEntity rs = vMap.get(key);
                    for (Long c : cou) {
                        String str = cost+"_"+c;
                        if (!str.equals(key)) {
                            RsCostReportEntity rsc = vMap.get(str);
                            rs.setCurrMonthValue(new BigDecimal(rs.getCurrMonthValue()).
                                    add(new BigDecimal(rsc.getCurrMonthValue())).toPlainString());
                        }
                    }
                }
            }
        }
        List<RsCostReportEntity> list1 = new ArrayList<>();
        List<RsCostReportEntity> list2 = new ArrayList<>();
        List<RsCostReportEntity> list3 = new ArrayList<>();
        List<RsCostReportEntity> list4 = new ArrayList<>();
        for (int i = 0; i < rsList.size(); i++) {
            if (i<2000){
                list1.add(rsList.get(i));
            }else if(i>=2000 && i<4000){
                list3.add(rsList.get(i));
            }else if(i>=4000 && i<6000){
                list4.add(rsList.get(i));
            }else{
                list2.add(rsList.get(i));
            }
        }
        rsCostReportDao.insertByList(list1);
        rsCostReportDao.insertByList(list2);
        rsCostReportDao.insertByList(list3);
        rsCostReportDao.insertByList(list4);

        //获取各个区的值，并插入到数据库中
        List<RsCostReportEntity> reportList = rsCostReportDao.getCurrentMonthValue3(year * 100 + per, reportId, topOrgCode);
        for (RsCostReportEntity entity : reportList) {
            entity.setCurrMonthValue(new BigDecimal(entity.getCurrMonthValue()).toPlainString());
        }
        rsCostReportDao.insertByList(reportList);

        //将插入的数据查询出来
        List<RsCostReportEntity> rsCostReportEntityList = rsCostReportDao.selectReportList(topOrgCode, month, reportId);
        Map<String, RsCostReportEntity> valueMap = new HashMap<>();
        for (RsCostReportEntity rs : rsCostReportEntityList) {
            valueMap.put(rs.getOrgCode() + "_" + rs.getParentOrgCode() + "_" + rs.getCostItemId(), rs);
        }
        //生成完整的对象
        getRsCostReport(valueMap, reportId, month, topOrgCode, year, per);

        return R.ok("success");
    }

    /**
     * 生成化小成本报表数据（区县成本报表）
     * 实现思路： 先获取出所有的区以及营服的本月数，然后插入到数据库，再查询上月数，本年数，同期去年数，然后再去生成增减数及比例
     *
     * @param month
     * @param reportId
     * @return
     */
    public R insertRsCostReportValue(Integer month, Integer reportId) {
        //本年
        Integer year = month / 100;
        //本月
        Integer per = month % 100;
        //获取地市的编码
        Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();

        //判断当前月份是否存在数据，有的话则删除
        rsCostReportDao.deleteByMonth(month, topOrgCode, reportId);

        //从sap表中获取出没有条件限制的本月数
        List<RsCostReportEntity> rsList = rsCostReportDao.getCurrentMonthValue1(year, per, reportId, topOrgCode);

        //从sap表中被条件限制的本月数
        //0.用map去存数据对象
        Map<String, RsCostReportEntity> map = new HashMap<>();
        //1.先查询什么费用由什么条件限制
        List<RelSapCostItem> itemList = relSapCostItemMapper.getSubitemByItemId(year, reportId, topOrgCode);
        for (RelSapCostItem item : itemList) {
            StringBuilder sb = new StringBuilder();
            sb.append(" a.cost_element = " + item.getSapCode());
            for (RelSapCostItemSubitem subitem : item.getSubitemList()) {
                if (subitem.getOperations().contains("like")) {
                    sb.append(" and a." + subitem.getFieldName() + " " + subitem.getOperations() + " '%" + subitem.getParams() + "%'");
                } else {
                    sb.append(" and a." + subitem.getFieldName() + " " + subitem.getOperations() + " '" + subitem.getParams() + "'");
                }
            }
            //2.根据条件从sap表中查询对应的本月数
            List<RsCostReportEntity> rsCostList = rsCostReportDao.getCurrentMonthValue2(year, per, reportId, topOrgCode, item.getCostItemId(), sb);
            //3.根据条件取值，然后放到map中去
            for (RsCostReportEntity rs : rsCostList) {
                String key = rs.getOrgCode() + "_" + rs.getParentOrgCode() + "_" + rs.getCostItemId();
                if (!map.containsKey(key)) {
                    map.put(key, rs);
                } else {
                    RsCostReportEntity r = map.get(key);
                    r.setCurrMonthValue(new BigDecimal(r.getCurrMonthValue()).add(new BigDecimal(rs.getCurrMonthValue())).toPlainString());
                    map.put(key, r);
                }
            }
        }
        //4.将map中的结果放入本月数中
        for (String s : map.keySet()) {
            rsList.add(map.get(s));
        }
        Map<String, RsCostReportEntity> vMap = new HashMap<>();

        for (RsCostReportEntity rs : rsList) {
            rs.setCurrMonthValue(new BigDecimal(rs.getCurrMonthValue()).toPlainString());
            vMap.put(rs.getOrgCode() + "_" + rs.getParentOrgCode() + "_" + rs.getCostItemId(), rs);
        }
        //5.查询当前市的所有营服
        List<SysOrganization> oList = sysOrganizationMapper.getAllCenter(topOrgCode + "%");
        //6.查询当前所有费用名称
        QueryWrapper<SysCostItem> wq = new QueryWrapper<>();
        wq.select("id,name").eq("level", 2).eq("org_code", topOrgCode).eq("report_id", reportId).eq("enable", 1);
        List<SysCostItem> item = sysCostItemMapper.selectList(wq);
        //判断营服本月是否有数据，没有的话数据置为0
        for (SysOrganization o : oList) {
            for (SysCostItem i : item) {
                if (!vMap.containsKey(o.getCode() + "_" + o.getParentCode() + "_" + i.getId())) {
                    RsCostReportEntity entity = new RsCostReportEntity();
                    entity.setMonth(month);
                    entity.setCurrMonthValue("0");
                    entity.setBudget("0");
                    entity.setTopOrgCode(topOrgCode);
                    entity.setParentOrgCode(o.getParentCode());
                    entity.setOrgCode(o.getCode());
                    entity.setReportId(reportId);
                    entity.setCostItemId(i.getId());
                    rsList.add(entity);
                }
            }
        }
        //批量插入,此时budget默认放0，之后需要修改
        rsCostReportDao.insertByList(rsList);

        //获取各个区的值，并插入到数据库中
        List<RsCostReportEntity> reportList = rsCostReportDao.getCurrentMonthValue3(year * 100 + per, reportId, topOrgCode);
        for (RsCostReportEntity entity : reportList) {
            entity.setCurrMonthValue(new BigDecimal(entity.getCurrMonthValue()).toPlainString());
        }
        rsCostReportDao.insertByList(reportList);

        //将插入的数据查询出来
        List<RsCostReportEntity> rsCostReportEntityList = rsCostReportDao.selectReportList(topOrgCode, month, reportId);
        Map<String, RsCostReportEntity> valueMap = new HashMap<>();
        for (RsCostReportEntity rs : rsCostReportEntityList) {
            valueMap.put(rs.getOrgCode() + "_" + rs.getParentOrgCode() + "_" + rs.getCostItemId(), rs);
        }
        //生成完整的对象
        getRsCostReport(valueMap, reportId, month, topOrgCode, year, per);

        return R.ok("success");
    }

    /**
     * 给实体类其他属性赋值
     *
     * @param valueMap
     * @param reportId
     * @param month
     * @param topOrgCode
     * @param year
     * @param per
     */
    private void getRsCostReport(Map<String, RsCostReportEntity> valueMap, Integer reportId, Integer month, Long topOrgCode, Integer year, Integer per) {
        //获取上月数
        List<RsCostReportEntity> lastMonthValue = rsCostReportDao.getLastMonthValue(reportId, year * 100 + (per - 1), topOrgCode);
        for (RsCostReportEntity rs : lastMonthValue) {
            String key = rs.getOrgCode() + "_" + rs.getParentOrgCode() + "_" + rs.getCostItemId();
            RsCostReportEntity entity = valueMap.get(key);
            if (entity != null) {
                entity.setLastMonthValue(new BigDecimal(rs.getLastMonthValue()).toPlainString());
                valueMap.put(key, entity);
            }
        }
        //获取本年累计
        List<RsCostReportEntity> currentYearValue = rsCostReportDao.getYearValue(reportId, year * 100 + 1, month, topOrgCode);
        for (RsCostReportEntity rs : currentYearValue) {
            String key = rs.getOrgCode() + "_" + rs.getParentOrgCode() + "_" + rs.getCostItemId();
            RsCostReportEntity entity = valueMap.get(key);
            if (entity != null) {
                entity.setCurrYearValue(new BigDecimal(rs.getCurrYearValue()).toPlainString());
                valueMap.put(key, entity);
            }
        }
        //获取去年累计
        List<RsCostReportEntity> lastYearValue = rsCostReportDao.getYearValue(reportId, (year - 1) * 100 + 1, (year - 1) * 100 + per, topOrgCode);
        for (RsCostReportEntity rs : lastYearValue) {
            String key = rs.getOrgCode() + "_" + rs.getParentOrgCode() + "_" + rs.getCostItemId();
            RsCostReportEntity entity = valueMap.get(key);
            if (entity != null) {
                entity.setLastYearValue(new BigDecimal(rs.getCurrYearValue()).toPlainString());
                valueMap.put(key, entity);
            }
        }

        //生成其他数据
        for (String s : valueMap.keySet()) {
            RsCostReportEntity entity = valueMap.get(s);
            //生成比上月增减数
            entity.setDiffLastMonth(new BigDecimal(entity.getCurrMonthValue()).subtract(new BigDecimal(entity.getLastMonthValue())).toPlainString());
            //生成比上月增减比例
            entity.setPercLastMonth(getDivide(entity.getDiffLastMonth(), entity.getLastMonthValue()));
            //生成比上年同期增减数
            entity.setDiffLastYear(new BigDecimal(entity.getCurrYearValue()).subtract(new BigDecimal(entity.getLastYearValue())).toPlainString());
            //生成比上年同期增减比例
            entity.setPercLastYear(getDivide(entity.getDiffLastYear(), entity.getLastYearValue()));
            //累计进度
            entity.setProgress(getDivide(entity.getCurrYearValue(), entity.getBudget()));
            //剩余预算
            entity.setRemainingBudget(new BigDecimal(entity.getBudget()).subtract(new BigDecimal(entity.getCurrYearValue())).toPlainString());
            rsCostReportDao.updateById(entity);
        }

    }

    /**
     * 除法计算
     */
    public String getDivide(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        String str = "--";
        if (b2.compareTo(BigDecimal.ZERO) != 0) {
            str = b1.divide(b2, 7, BigDecimal.ROUND_UP).toPlainString();
        }
        return str;
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
            item.setItemsChildren(new ArrayList<>());
            itemMap.put(item.getId(), item);
            Long parentId = item.getParentId();
            if (parentId == -1) {
                result.add(item);
            } else {
                SysCostItem parent = itemMap.get(parentId);
                if (parent != null) {
                    parent.getItemsChildren().add(item);
                }
            }
        }
        return result;
    }

    public Map<String, List<Long>> getCount(Map<String, List<Long>> map, List<SysCostItem> itemList) {
        for (SysCostItem item : itemList) {
            List<SysCostItem> children = item.getItemsChildren();
            List<Long> list = new ArrayList<>();
            if (children.size() > 0) {
                getCount(map, children);
                for (SysCostItem child : children) {
                    list.add(child.getId());
                }
                map.put(item.getId().toString(), list);
            } else {
                list.add(item.getId());
                map.put(item.getId().toString(), list);
            }

        }
        return map;
    }
}

package com.gpdi.operatingunit.service.reportconfig.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.relation.RelOrgReportColumnMapper;
import com.gpdi.operatingunit.dao.reportconfig.DataSapListMapper;
import com.gpdi.operatingunit.dao.reportconfig.RelSapCostItemMapper;
import com.gpdi.operatingunit.dao.reportconfig.SapListMapper;
import com.gpdi.operatingunit.dao.reportconfig.SysCostItemMapper;
import com.gpdi.operatingunit.dao.system.RelSapCostItemSubitemMapper;
import com.gpdi.operatingunit.dao.system.SysDictMapper;
import com.gpdi.operatingunit.dao.system.SysSapSubjectMapper;
import com.gpdi.operatingunit.entity.common.SapList;
import com.gpdi.operatingunit.entity.relation.RelOrgReportColumn;
import com.gpdi.operatingunit.entity.reportconfig.DataSapListEntity;
import com.gpdi.operatingunit.entity.reportconfig.RelSapCostItem;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.RelSapCostItemSubitem;
import com.gpdi.operatingunit.entity.system.SysDict;
import com.gpdi.operatingunit.entity.system.SysSapSubject;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.reportconfig.CostConfigService;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zyb
 * @Date: 2019/10/25 11:34
 */
@Service
@Transactional
public class CostConfigServiceImpl implements CostConfigService {

    @Autowired
    private SysCostItemMapper sysCostItemMapper;
    @Autowired
    private SapListMapper sapListMapper;
    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysSapSubjectMapper sysSapSubjectMapper;
    @Autowired
    private RelSapCostItemSubitemMapper relSapCostItemSubitemMapper;
    @Autowired
    private RelSapCostItemMapper relSapCostItemMapper;
    @Autowired
    private DataSapListMapper dataSapListMapper;
    @Autowired
    private RelOrgReportColumnMapper relOrgReportColumnMapper;

    @Override
    public int updateSeqAndStatus(Integer reportId, Integer[] ids, Long userTopOrgCode) {
        // 获取所有的
        QueryWrapper<RelOrgReportColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", reportId);
        queryWrapper.eq("top_org_code", userTopOrgCode);
        List<RelOrgReportColumn> relOrgReportColumns = relOrgReportColumnMapper.selectList(queryWrapper);
        List<Integer> allidsList = relOrgReportColumns.stream()
                .map(RelOrgReportColumn::getId).collect(Collectors.toList());
        // 设置为不显示的id
        List<String> list = new ArrayList<>();
        HashSet allIdsSet = new HashSet(allidsList);
        HashSet idsSet = new HashSet(Arrays.asList(ids));
        allIdsSet.removeAll(idsSet);
        list.addAll(allIdsSet);
        // 需要被修改成 status:0 的数据
        if (list.size() > 0) {
            List<RelOrgReportColumn> needToUpdateStatus = relOrgReportColumnMapper.selectBatchIds(list);
            needToUpdateStatus.forEach(relOrgReportColumn -> {
                relOrgReportColumn.setStatus(0);
                relOrgReportColumnMapper.updateById(relOrgReportColumn);
            });
        }
        // 需要修改排序的数据
        for (int i = 0; i < ids.length; i++) {
            RelOrgReportColumn relOrgReportColumn = relOrgReportColumnMapper.selectById(ids[i]);
            relOrgReportColumn.setSeq(i + 1);
            relOrgReportColumn.setStatus(1);
            relOrgReportColumnMapper.updateById(relOrgReportColumn);
        }
        return 0;
    }

    @Override
    public List<RelOrgReportColumn> queryTreeEnableData(Integer reportId) {
        QueryWrapper<RelOrgReportColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", reportId).eq("status", 1)
                .eq("top_org_code", ShiroUtils.getUser().getTopOrgCode());
        List<RelOrgReportColumn> relOrgReportColumns = relOrgReportColumnMapper.selectList(queryWrapper);
        return relOrgReportColumns;
    }


    @Override
    public List<RelOrgReportColumn> queryTitleSettingData(Integer reportId) {
        QueryWrapper<RelOrgReportColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", reportId).eq("top_org_code", ShiroUtils.getUser().getTopOrgCode())
                .orderByDesc("status").orderByAsc("seq");
        List<RelOrgReportColumn> relOrgReportColumns = relOrgReportColumnMapper.selectList(queryWrapper);
        return relOrgReportColumns;
    }

    /**
     * 删除费用
     *
     * @param id
     * @return
     */
    @Override
    public Integer deleteItemById(Integer id) {
        List<Integer> itemList = sysCostItemMapper.getIdsByParentId(id);
        if (itemList.size() > 0) {
            List<Integer> costItemList = relSapCostItemMapper.getIdsByCostItemId(itemList);
            if (costItemList.size() > 0) {
                List<Integer> subitemList = relSapCostItemSubitemMapper.getIdsByItemId(costItemList);
                if (subitemList.size() > 0) {
                    relSapCostItemSubitemMapper.deleteBatchIds(subitemList);
                }
                relSapCostItemMapper.deleteBatchIds(costItemList);
            }
            sysCostItemMapper.deleteBatchIds(itemList);
        }
        int delete = sysCostItemMapper.deleteById(id);
        return delete;
    }

    /**
     * 回显树状的费用列表
     *
     * @param reportId
     * @return
     */
    @Override
    public List<SysCostItem> choseItemByReportId(Integer reportId) {
        Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();
        QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
        qw.select("*").eq("report_id", reportId).eq("enable", 1)
                .eq("org_code", topOrgCode).orderByAsc("seq");
        List<SysCostItem> itemList = sysCostItemMapper.selectList(qw);
        List<SysCostItem> result = buildTree(itemList);
        return result;
    }

    /**
     * 修改费用名
     *
     * @param id
     * @param name
     * @return
     */
    @Override
    public Integer updateItemName(Integer id, String name) {
        SysUser user = ShiroUtils.getUser();
        //修改sys_cost_item表的费用名及fullname等信息
        QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
        qw.select("*").eq("id", id);
        SysCostItem sysCostItem = sysCostItemMapper.selectOne(qw);

        List<SysCostItem> itemList = sysCostItemMapper.getChildren(sysCostItem.getReportId(), sysCostItem.getOrgCode(), sysCostItem.getId());
        for (SysCostItem item : itemList) {
            if (item.getFullName().contains(sysCostItem.getName())) {
                String replace = item.getFullName().replace(sysCostItem.getName(), name);
                item.setFullName(replace);
                sysCostItem.setUpdateOperId(user.getId());
                sysCostItem.setUpdateTime(DateUtils.getCurrentDate());
                sysCostItemMapper.updateById(item);
            }
        }

        //修改当前更换name的费用的fullname
        String replace = sysCostItem.getFullName().replace(sysCostItem.getName(), name);
        sysCostItem.setFullName(replace);
        //修改  时间、修改人id
        sysCostItem.setUpdateOperId(user.getId());
        sysCostItem.setUpdateTime(DateUtils.getCurrentDate());
        sysCostItem.setName(name);
        int update = sysCostItemMapper.updateById(sysCostItem);
        //修改对应的费用成本项的费用名称(表 rel_sap_cost_item)
        relSapCostItemMapper.updateByItemId(id, name);
        return update;
    }

    /**
     * 保存费用到区县报表
     *
     * @param sysCostItem
     * @return
     */
    @Override
    public Integer saveItemByCounty(SysCostItem sysCostItem) {
        SysUser user = ShiroUtils.getUser();
        int insert = 0;
        if (sysCostItem.getId() != -1) {
            QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
            qw.select("*").eq("id", sysCostItem.getId());
            SysCostItem item = sysCostItemMapper.selectOne(qw);

            //获取当前父id下的最大的seq
            Integer min = sysCostItemMapper.getMaxSeqByParentId(item.getId());
            if (min == null) {
                min = item.getSeq();
            }
            //获取与父同级的最小的seq
            Integer max = sysCostItemMapper.getSeqForCounty(min, item.getOrgCode(), item.getReportId());
            //二分法存入seq
            sysCostItem.setFullName(item.getFullName() + "|" + sysCostItem.getName());
            sysCostItem.setOrgCode(item.getOrgCode());
            sysCostItem.setParentId(item.getId());
            sysCostItem.setLevel(item.getLevel() + 1);
            sysCostItem.setReportId(item.getReportId());
            sysCostItem.setSeq((min + max) / 2);
            sysCostItem.setCreateOperId(user.getId());
            sysCostItem.setCreateTime(DateUtils.getCurrentDate());
            sysCostItem.setUpdateOperId(user.getId());
            sysCostItem.setUpdateTime(DateUtils.getCurrentDate());
            insert = sysCostItemMapper.insert(sysCostItem);
        } else {
            Integer maxSeq = sysCostItemMapper.getMaxSeqByParentId(sysCostItem.getId());
            sysCostItem.setFullName(sysCostItem.getName());
            sysCostItem.setOrgCode(user.getTopOrgCode());
            sysCostItem.setParentId(sysCostItem.getId());
            sysCostItem.setLevel(1);
            sysCostItem.setReportId(sysCostItem.getReportId());
            sysCostItem.setSeq(maxSeq + 100000);
            sysCostItem.setCreateOperId(user.getId());
            sysCostItem.setCreateTime(DateUtils.getCurrentDate());
            sysCostItem.setUpdateOperId(user.getId());
            sysCostItem.setUpdateTime(DateUtils.getCurrentDate());
            insert = sysCostItemMapper.insert(sysCostItem);
        }
        return insert;
    }

    /**
     * 保存费用到化小成本表
     *
     * @param sysCostItem
     * @return
     */
    @Override
    public Integer saveItemByCost(SysCostItem sysCostItem) {
        SysUser user = ShiroUtils.getUser();
        Integer insert = 0;
        if (sysCostItem.getId() != -1) {
            QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
            qw.select("*").eq("id", sysCostItem.getId());
            SysCostItem item = sysCostItemMapper.selectOne(qw);
            Integer max = sysCostItemMapper.getMaxSeqByParentId(item.getParentId());
            //把查出来的对象变成新的插入
            item.setParentId(item.getId());
            item.setCreateOperId(user.getId());
            item.setUpdateOperId(user.getId());
            item.setName(sysCostItem.getName());
            item.setLevel(item.getLevel() + 1);
            item.setDataFromDesc(sysCostItem.getDataFromDesc());
            item.setSeq(max + 1);
            item.setFullName(item.getFullName() + "|" + sysCostItem.getName());
            item.setCreateTime(DateUtils.getCurrentDate());
            item.setUpdateTime(DateUtils.getCurrentDate());
            sysCostItem.setId(null);
            insert = sysCostItemMapper.insert(item);
        } else {
            Integer max = sysCostItemMapper.getMaxSeqByParentId(sysCostItem.getId());
            Integer maxSeq = sysCostItemMapper.getMaxSeqByParentId(sysCostItem.getId());
            sysCostItem.setFullName(sysCostItem.getName());
            sysCostItem.setOrgCode(user.getTopOrgCode());
            sysCostItem.setParentId(sysCostItem.getId());
            sysCostItem.setLevel(1);
            sysCostItem.setReportId(sysCostItem.getReportId());
            sysCostItem.setSeq(maxSeq + 1);
            sysCostItem.setCreateOperId(user.getId());
            sysCostItem.setCreateTime(DateUtils.getCurrentDate());
            sysCostItem.setUpdateOperId(user.getId());
            sysCostItem.setUpdateTime(DateUtils.getCurrentDate());
            sysCostItem.setId(null);
            insert = sysCostItemMapper.insert(sysCostItem);
        }
        return insert;
    }

    @Override
    public List<SysDict> getDataFrom() {
        QueryWrapper<SysDict> qw = new QueryWrapper<>();
        qw.select("text").eq("cat", "sys_cost_item.data_from");
        List<SysDict> result = sysDictMapper.selectList(qw);
        return result;
    }

    /**
     * 选择要新增的费用的父级
     *
     * @param reportId
     * @return
     */
    @Override
    public List<SysCostItem> choseItem(Integer reportId) {
        Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();
        List<SysCostItem> result = new ArrayList<>();
        SysCostItem item = new SysCostItem();
        item.setParentId(Long.valueOf(-1));
        item.setName("新建成本");
        item.setId(Long.valueOf(-1));
        result.add(item);
        if (reportId == 20001) {
            QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
            qw.select("*").eq("report_id", reportId).eq("enable", 1).eq("org_code", topOrgCode).orderByAsc("seq");
            List<SysCostItem> itemList = sysCostItemMapper.selectList(qw);
            result.addAll(buildTree(itemList));
        } else if (reportId == 10001) {
            QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
            qw.select("*").eq("report_id", reportId).eq("level", 1).eq("enable", 1).eq("org_code", topOrgCode).orderByAsc("seq");
            List<SysCostItem> itemList = sysCostItemMapper.selectList(qw);
            result.addAll(buildTree(itemList));
        }
        return result;
    }

    /**
     * 给区县表配置划小成本的成本项
     *
     * @param cid      区县费用id
     * @param item     区县费用名称
     * @param sid      化小成本id
     * @param reportId
     * @param orgCode
     * @return
     */
    @Override
    public String setCountrySapSubject(Integer cid, String item, Integer sid, Integer reportId, Long orgCode) {
        SysUser user = ShiroUtils.getUser();
        Integer year = DateUtils.getYear(DateUtils.getCurrentDate());
        //获取化小成本的费用
        QueryWrapper<RelSapCostItem> qw = new QueryWrapper<>();
        qw.select("id as sid,org_code,year,sap_code,handling_desc,operation,enable")
                .eq("cost_item_id", sid).eq("enable", 1).eq("org_code", orgCode).eq("year", year);
        List<RelSapCostItem> itemList = relSapCostItemMapper.selectList(qw);
        //新建map用于区分化小成本以及区县
        Map<Long, RelSapCostItem> cost = new HashMap<>();
        Map<Long, RelSapCostItem> country = new HashMap<>();

        //生成区县对象并插入表，再将结果放进cost集合
        for (RelSapCostItem rel : itemList) {
            rel.setReportId(reportId);
            rel.setCostItemId(cid.longValue());
            rel.setCostItem(item);
            rel.setCreateOperId(user.getId());
            rel.setCreateTime(DateUtils.getCurrentDate());
            rel.setUpdateOperId(user.getId());
            rel.setUpdateTime(DateUtils.getCurrentDate());
            relSapCostItemMapper.insert(rel);
            cost.put(rel.getSapCode(), rel);
        }

        //获取插入表的数据的id,再根据id替换生成新区县的数据插入表
        QueryWrapper<RelSapCostItem> wq = new QueryWrapper<>();
        wq.select("*").eq("cost_item_id", cid).eq("enable", 1)
                .eq("org_code", orgCode);
        List<RelSapCostItem> countryItem = relSapCostItemMapper.selectList(wq);
        for (RelSapCostItem rel : countryItem) {
            country.put(rel.getSapCode(), rel);
        }

        //查询各个成本项目的条件并插入区县的数据
        for (Long sapCode : cost.keySet()) {
            Long id = country.get(sapCode).getId();
            Long costId = cost.get(sapCode).getSid();
            QueryWrapper<RelSapCostItemSubitem> q = new QueryWrapper<>();
            q.select("parent_id,org_code,year,report_id,sap_code,field_name,field_name_desc,operation,operation_desc,params")
                    .eq("parent_id", costId).eq("year", year);
            List<RelSapCostItemSubitem> relSapCostItemSubitems = relSapCostItemSubitemMapper.selectList(q);
            for (RelSapCostItemSubitem subitem : relSapCostItemSubitems) {
                subitem.setParentId(id);
                subitem.setReportId(reportId);
                relSapCostItemSubitemMapper.insert(subitem);
            }
        }
        return "success";
    }

    /**
     * 查询化小成本表费用用于处理区县配置
     *
     * @param reportId
     * @param orgCode
     * @return
     */
    @Override
    public List<SysCostItem> getCostItemByReportId(Integer reportId, Long orgCode) {
        return sysCostItemMapper.getCostItemByReportId(reportId, orgCode);
    }

    /**
     * 获取区县成本表的类别
     *
     * @param reportId
     * @param orgCode
     * @return
     */
    @Override
    public List<SysCostItem> getCountyItem(Integer reportId, Long orgCode) {
        Integer year = DateUtils.getYear(DateUtils.getCurrentDate());
        List<SysCostItem> sysCostItems = sysCostItemMapper.getCountyItem(reportId, orgCode, year);
        getSapCodeAndNameByList(sysCostItems);
        return sysCostItems;
    }

    /**
     * 删除所有匹配好的sap科目
     *
     * @param id
     * @return
     */
    @Override
    public Integer deleteAllSubitem(Integer id) {
        List<Integer> list = new ArrayList<>();
        list.add(id);
        Integer num = 0;
        //获取parent_id为费用id的id集合
        List<Integer> costItemLists = relSapCostItemMapper.getIdsByCostItemId(list);
        if (costItemLists.size() > 0) {
            //查询id子集
            List<Integer> subitemLists = relSapCostItemSubitemMapper.getIdsByItemId(costItemLists);
            if (subitemLists.size() > 0) {
                relSapCostItemSubitemMapper.deleteBatchIds(subitemLists);
            }
            num = relSapCostItemMapper.deleteBatchIds(costItemLists);
        }
        return num;
    }

    /**
     * 删除匹配好的sap科目
     *
     * @param id
     * @return
     */
    @Override
    public Integer deleteSubmitem(Integer id) {
        int delete = relSapCostItemSubitemMapper.deleteById(id);
        return delete;
    }

    /**
     * 删除匹配好的成本项
     *
     * @param sid
     * @return
     */
    @Override
    public Integer deleteRelSapCostItem(Long sid) {
        int delete = relSapCostItemMapper.deleteItem(sid);
        int del = relSapCostItemSubitemMapper.deleteSubitemByParentId(sid);
        return delete;
    }

    /**
     * 生成/修改新的成本项
     *
     * @param user
     * @param id
     * @param item
     * @param reportId
     * @param sysSapSubjects
     * @return
     */
    @Override
    public Object updateRelSapCostItem(SysUser user, Integer id, String item, Integer reportId, List<SysSapSubject> sysSapSubjects) {
        Map<Long, Long> map = new HashMap<>();
        for (SysSapSubject s : sysSapSubjects) {
            if (map.containsKey(s.getCode())) {
                return -1;
            } else {
                map.put(s.getCode(), s.getCode());
            }
        }
        Integer userId = user.getId();
        int year = DateUtils.getYear(DateUtils.getCurrentDate());
        List<RelSapCostItem> result = new ArrayList<>();
        for (SysSapSubject sbj : sysSapSubjects) {
            RelSapCostItem rel = new RelSapCostItem();
            rel.setOrgCode(user.getTopOrgCode());
            rel.setYear(year);
            rel.setReportId(reportId);
            rel.setSapCode(sbj.getCode());
            rel.setCostItemId(id.longValue());
            rel.setCostItem(item);
            rel.setEnable(1);
            rel.setCreateOperId(userId);
            rel.setCreateTime(DateUtils.getCurrentDate());
            rel.setUpdateOperId(userId);
            rel.setUpdateTime(DateUtils.getCurrentDate());
            result.add(rel);
        }
        Integer number = 0;
        for (RelSapCostItem rel : result) {
            QueryWrapper<RelSapCostItem> qw = new QueryWrapper<>();
            qw.select("*").eq("sap_code", rel.getSapCode()).eq("cost_item_id", rel.getCostItemId())
                    .eq("cost_item", rel.getCostItem()).eq("enable", 1).eq("report_id", reportId);
            RelSapCostItem r = relSapCostItemMapper.selectOne(qw);
            if (r != null) {
                number += 1;
            } else {
                int insert = relSapCostItemMapper.insert(rel);
                number += insert;
            }
        }
        List<SysSapSubject> sapSubject = sysSapSubjectMapper.getSapSubject(id, reportId, year);

        return sapSubject;
    }

    /**
     * 获取提供匹配的sap科目
     *
     * @return
     */
    @Override
    public List<SysSapSubject> getAllSubject() {
        QueryWrapper<SysSapSubject> qw = new QueryWrapper<>();
        qw.select("code,name").eq("enable", 1);
        List<SysSapSubject> result = sysSapSubjectMapper.selectList(qw);
        return result;
    }

    /**
     * 插入绑定好的数据
     *
     * @param sid
     * @param orgCode
     * @param year
     * @param sapCode
     * @param submitemList
     * @param reportId
     * @return
     */
    @Override
    public List<RelSapCostItemSubitem> insertSubmitem(Long sid, Long orgCode, Integer year, Long sapCode, List<RelSapCostItemSubitem> submitemList, Integer reportId) {
        Integer number = 0;

        List<SapList> sapNameList = getSapNameList();
        Map<String, String> sapMap = new HashMap<>();
        for (SapList s : sapNameList) {
            sapMap.put(s.getColumnName(), s.getComment());
        }

        Map<String, String> selMap = new HashMap<>();
        List<SysDict> selections = getSelections();
        for (SysDict s : selections) {
            selMap.put(s.getValue(), s.getText());
        }

        for (RelSapCostItemSubitem rsc : submitemList) {
            rsc.setOrgCode(orgCode);
            rsc.setYear(year);
            rsc.setParentId(sid);
            rsc.setSapCode(sapCode);
            rsc.setReportId(reportId);
            rsc.setFieldNameDesc(sapMap.get(rsc.getFieldName()));
            rsc.setOperationDesc(selMap.get(rsc.getOperation()));
            if (rsc.getId() == null) {
                int insert = relSapCostItemSubitemMapper.insert(rsc);
                number += insert;
            } else {
                int i = relSapCostItemSubitemMapper.updateById(rsc);
                number += i;
            }
        }

        List<RelSapCostItemSubitem> result = getRelSapCostItemSubmitemList(sid, orgCode, year, sapCode, reportId);


        return result;
    }

    /**
     * 获取已经被选择的成本项的值
     *
     * @param sid
     * @param orgCode
     * @param year
     * @param sapCode
     * @param reportId
     * @return
     */
    @Override
    public List<RelSapCostItemSubitem> getRelSapCostItemSubmitemList(Long sid, Long orgCode, Integer year, Long sapCode, Integer reportId) {
        QueryWrapper<RelSapCostItemSubitem> qw = new QueryWrapper<>();
        qw.select("*").eq("parent_id", sid).eq("org_code", orgCode).eq("year", year).eq("sap_code", sapCode).eq("report_id", reportId);
        return relSapCostItemSubitemMapper.selectList(qw);
    }

    /**
     * 获取提供绑定的成本项
     *
     * @param id
     * @param reportId
     * @return
     */
    @Override
    public List<SysSapSubject> getSapSubject(Integer id, Integer reportId) {
        Integer year = DateUtils.getYear(DateUtils.getCurrentDate());
        return sysSapSubjectMapper.getSapSubject(id, reportId, year);
    }

    /**
     * 获取选择条件
     *
     * @return
     */
    @Override
    public List<SysDict> getSelections() {
        return sysDictMapper.getSelections();
    }

    /**
     * 获取sap的字段名
     *
     * @return
     */
    @Override
    public List<SapList> getSapNameList() {
        return sapListMapper.getSapNameList();
    }

    /**
     * 获取sap列表的月份集合
     *
     * @param topOrgCode
     * @return
     */
    @Override
    public List<DataSapListEntity> getMonthList(Long topOrgCode) {
        QueryWrapper<DataSapListEntity> qw = new QueryWrapper<>();
        qw.select("distinct (year*100)+per as year,(year*100)+per as month").eq("org_code", topOrgCode).orderByAsc("per");
        List<DataSapListEntity> result = dataSapListMapper.selectList(qw);
        return result;

    }

    /**
     * 获取成本表的费用类别
     *
     * @param reportId
     * @param orgCode
     * @return
     */
    @Override
    public List<SysCostItem> getCostItem(Integer reportId, Long orgCode) {
        Integer year = DateUtils.getYear(DateUtils.getCurrentDate());
        List<SysCostItem> itemList = sysCostItemMapper.getSysCostItemList(reportId, orgCode, year);
        getSapCodeAndNameByList(itemList);
        return itemList;
    }

    /**
     * 根据list生成code值与成本项的关系
     *
     * @param itemList
     */
    public void getSapCodeAndNameByList(List<SysCostItem> itemList) {
        for (SysCostItem item : itemList) {
            if (item.getSapSubjectList() != null && item.getSapSubjectList().size() > 0) {
                String str = null;
                for (SysSapSubject sub : item.getSapSubjectList()) {
                    str += ", " + sub.getSname() + "(" + sub.getCode() + ") ";
                }
                String substring = str.substring(5);
                item.setSapCodeAndName(substring);
            }
        }
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
}

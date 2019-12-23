package com.gpdi.operatingunit.service.reportconfig.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.dao.reportconfig.SysCostItemHandleMapper;
import com.gpdi.operatingunit.dao.reportconfig.SysCostItemMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItemHandle;
import com.gpdi.operatingunit.entity.system.SysOrgAnizationCamp;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.reportconfig.RsCostReportService;
import com.gpdi.operatingunit.service.reportconfig.SysCostItemService;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import com.gpdi.operatingunit.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Zhb
 * @date 2019/11/4 9:35
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysCostItemServiceImpl implements SysCostItemService {

    @Autowired
    private SysCostItemMapper sysCostItemMapper;

    @Autowired
    private SysCostItemHandleMapper sysCostItemHandleMapper;

    @Autowired
    private RsCostReportService rsCostReportService;

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Override
    public R costItemUpload(List<Object> list, Integer reportId) {
        //判断sys_cost_item是否存在数据
        SysUser user = ShiroUtils.getUser();
        QueryWrapper<SysCostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", reportId).eq("org_code", user.getTopOrgCode());
        Integer count = sysCostItemMapper.selectCount(queryWrapper);
        //excel数据
        Map<String, List<SysCostItem>> map = buildMapData(list);
        if (count > 0) {
            // 存在数据，则执行
            updateCostItems(reportId, user, map);
        } else {
            //不存在数据，直接插入sys_cost_item表
            saveCostItems(reportId, user, map);
        }
        return R.ok();
    }

    @Override
    public R countyUpload(List<Object> list, Integer reportId) {
        //判断sys_cost_item是否存在数据
        SysUser user = ShiroUtils.getUser();
        QueryWrapper<SysCostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", reportId).eq("org_code", user.getTopOrgCode());
        Integer count = sysCostItemMapper.selectCount(queryWrapper);
        //去掉表头
        list.remove(0);
        //第一项等级必须为1
        if (count > 0) {
            // 存在数据，则执行
            updateCostItems(reportId, user, list);
        } else {
            //不存在数据，直接插入sys_cost_item表
            saveCostItems(reportId, user, list);
        }
        return R.ok();
    }

    /**
     * 修改sys_cost_item
     */
    private void updateCostItems(Integer reportId, SysUser user, Map<String, List<SysCostItem>> map) {
        int parentSeq = 1;
        // 1.删除sys_cost_item_handle对应报表的数据
        UpdateWrapper<SysCostItemHandle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("report_id", reportId).eq("org_code", user.getTopOrgCode());
        sysCostItemHandleMapper.delete(updateWrapper);
        // 2.插入数据到sys_cost_item_handle表
        for (Map.Entry<String, List<SysCostItem>> entry : map.entrySet()) {
            //保存父项
            SysCostItemHandle item = new SysCostItemHandle();
            item.setName(entry.getKey());
            item.setFullName(item.getName());
            item.setOrgCode(user.getTopOrgCode());
            item.setReportId(reportId);
            item.setLevel(1);
            item.setSeq(parentSeq);
            item.setParentId(-1L);
            item.setCreateOperId(user.getId());
            item.setCreateTime(new Date());
            sysCostItemHandleMapper.insert(item);
            Long parentId = item.getId();
            //保存子项
            int seq = 1;
            for (SysCostItem sysCostItem : entry.getValue()) {
                SysCostItemHandle childItem = new SysCostItemHandle();
                childItem.setName(sysCostItem.getName());
                childItem.setFullName(item.getName() + "|" + sysCostItem.getName());
                childItem.setOrgCode(user.getTopOrgCode());
                childItem.setReportId(reportId);
                childItem.setLevel(2);
                childItem.setSeq(seq);
                childItem.setParentId(parentId);
                childItem.setDataFromDesc(sysCostItem.getDataFromDesc());
                childItem.setCreateOperId(user.getId());
                childItem.setCreateTime(new Date());
                sysCostItemHandleMapper.insert(childItem);
                seq++;
            }
            parentSeq++;
        }
        //执行剩余步骤
        updateCostItems(reportId, user.getTopOrgCode());
    }

    /**
     * 修改sys_cost_item（区县报表）
     */
    private void updateCostItems(Integer reportId, SysUser user, List<Object> list) {
        // 1.删除sys_cost_item_handle对应报表的数据
        UpdateWrapper<SysCostItemHandle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("report_id", reportId).eq("org_code", user.getTopOrgCode());
        sysCostItemHandleMapper.delete(updateWrapper);
        // 2.插入数据到sys_cost_item_handle表
        saveCostItemHandles(reportId, user, list);
        //执行剩余步骤
        updateCostItems(reportId, user.getTopOrgCode());
    }

    /**
     * 剩余步骤
     */
    private void updateCostItems(Integer reportId, Long topOrgCode) {
        // 3.用join查询为修改成本项，获取原数据中需删除的数据的id，删除原数据中需删除的数据
        List<Long> deleteIds = sysCostItemMapper.getDeleteIds(reportId, topOrgCode, reportId, topOrgCode);
        if (deleteIds.size() > 0) {
            sysCostItemMapper.deleteBatchIds(deleteIds);
        }
        // 4.用left join查询新数据，新增的数据id为空，未修改数据id为原数据id
        List<SysCostItem> items = sysCostItemMapper.selectUpdates(reportId, topOrgCode);
        // 5.循环遍历插入/更新数据
        for (SysCostItem item : items) {
            if (item.getId() != null) {
                //修改
                UpdateWrapper<SysCostItem> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", item.getId());
                sysCostItemMapper.update(item, wrapper);
            } else {
                //新增
                item.setParentId(-10000L);
                sysCostItemMapper.insert(item);
            }
        }
        // 6.更新成本项目父级id(parent_id = -10000)的数据
        sysCostItemMapper.updateItemsParentId(reportId, topOrgCode);
    }

    /**
     * 批量保存sys_cost_item
     */
    private void saveCostItems(Integer reportId, SysUser user, Map<String, List<SysCostItem>> map) {
        int parentSeq = 1;
        for (Map.Entry<String, List<SysCostItem>> entry : map.entrySet()) {
            //保存父项
            SysCostItem item = new SysCostItem();
            item.setName(entry.getKey());
            item.setFullName(item.getName().trim());
            item.setOrgCode(user.getTopOrgCode());
            item.setReportId(reportId);
            item.setLevel(1);
            item.setSeq(parentSeq);
            item.setParentId(-1L);
            item.setCreateOperId(user.getId());
            item.setCreateTime(new Date());
            sysCostItemMapper.insert(item);
            Long parentId = item.getId();
            //保存子项
            int seq = 1;
            for (SysCostItem childItem : entry.getValue()) {
                childItem.setFullName(item.getName() + "|" + childItem.getName());
                childItem.setOrgCode(user.getTopOrgCode());
                childItem.setReportId(reportId);
                childItem.setLevel(2);
                childItem.setSeq(seq);
                childItem.setParentId(parentId);
                childItem.setCreateOperId(user.getId());
                childItem.setCreateTime(new Date());
                sysCostItemMapper.insert(childItem);
                seq++;
            }
            parentSeq++;
        }
    }

    /**
     * 批量保存sys_cost_item（区县报表）
     */
    private void saveCostItems(Integer reportId, SysUser user, List<Object> list) {
        Long parentId;
        int seq = 1000000;
        Map<String, SysCostItem> lastItemMap = new HashMap<>(1);
        Map<Integer, SysCostItem> levelItemMap = new HashMap<>();
//        Map<Integer, Integer> seqItemMap = new HashMap<>();
        for (Object object : list) {
            List<String> objects = (List) object;
            String remark = objects.get(0);
            String name = objects.get(1);
            String dataFromDesc = objects.get(2);
            int level = Integer.parseInt(objects.get(3));
            //获取前个对象
            SysCostItem lastItem = lastItemMap.get("lastItem");
            if (lastItem == null) {
                //第一个对象
                lastItem = new SysCostItem();
                lastItem.setLevel(1);
                lastItem.setParentId(-1L);
                lastItem.setId(-1L);
                level = 1;
            }
            if (lastItem.getLevel() < level) {
                //等级增大，当前对象属于上个对象的子项
                parentId = lastItem.getId();
                levelItemMap.put(level, lastItem);
//                seq = 1;
            } else if (lastItem.getLevel() == level) {
                //等级不变，当前对象与上个对象同级
                if (level == 1) {
                    parentId = -1L;
                } else {
                    parentId = lastItem.getParentId();
                }
//                seq++;
            } else {
                //等级变小
                if (level == 1) {
                    parentId = -1L;
                    levelItemMap = new HashMap<>();
                } else {
                    SysCostItem levelItem = levelItemMap.get(level);
                    parentId = levelItem.getId();
                }
//                seq = seqItemMap.get(level) + 1;
            }
            //生成 SysCostItem 对象
            seq += 100000;
            SysCostItem sysCostItem = new SysCostItem();
            sysCostItem.setName(name.trim());
            sysCostItem.setOrgCode(user.getTopOrgCode());
            sysCostItem.setReportId(reportId);
            sysCostItem.setLevel(level);
            sysCostItem.setSeq(seq);
            sysCostItem.setDataFromDesc(dataFromDesc);
            sysCostItem.setEnable(1);
            sysCostItem.setCreateOperId(user.getId());
            sysCostItem.setCreateTime(new Date());
            sysCostItem.setRemark(remark);
            sysCostItem.setParentId(parentId);
            sysCostItemMapper.insert(sysCostItem);
//            seqItemMap.put(level, seq);
            //前一个对象
            lastItemMap.put("lastItem", sysCostItem);
        }
        //更新成本项目full_name
        sysCostItemMapper.updateCountyItemFullName(reportId, user.getTopOrgCode());
    }

    /**
     * 批量保存sys_cost_item_handle（区县报表）
     */
    private void saveCostItemHandles(Integer reportId, SysUser user, List<Object> list) {
        Long parentId;
        int seq = 1000000;
        Map<String, SysCostItemHandle> lastItemMap = new HashMap<>(1);
        Map<Integer, SysCostItemHandle> levelItemMap = new HashMap<>();
//        Map<Integer, Integer> seqItemMap = new HashMap<>();
        for (Object object : list) {
            List<String> objects = (List) object;
            String remark = objects.get(0);
            String name = objects.get(1);
            String dataFromDesc = objects.get(2);
            int level = Integer.parseInt(objects.get(3));
            //获取前个对象
            SysCostItemHandle lastItem = lastItemMap.get("lastItem");
            if (lastItem == null) {
                //第一个对象
                lastItem = new SysCostItemHandle();
                lastItem.setLevel(1);
                lastItem.setParentId(-1L);
                lastItem.setId(-1L);
                level = 1;
            }
            if (lastItem.getLevel() < level) {
                //等级增大，当前对象属于上个对象的子项
                parentId = lastItem.getId();
                levelItemMap.put(level, lastItem);
//                seq = 1;
            } else if (lastItem.getLevel() == level) {
                //等级不变，当前对象与上个对象同级
                if (level == 1) {
                    parentId = -1L;
                } else {
                    parentId = lastItem.getParentId();
                }
//                seq++;
            } else {
                //等级变小
                if (level == 1) {
                    parentId = -1L;
                    levelItemMap = new HashMap<>();
                } else {
                    SysCostItemHandle levelItem = levelItemMap.get(level);
                    parentId = levelItem.getId();
                }
//                seq = seqItemMap.get(level) + 1;
            }
            //生成 SysCostItem 对象
            seq += 100000;
            SysCostItemHandle sysCostItemHandle = new SysCostItemHandle();
            sysCostItemHandle.setName(name);
            sysCostItemHandle.setOrgCode(user.getTopOrgCode());
            sysCostItemHandle.setReportId(reportId);
            sysCostItemHandle.setLevel(level);
            sysCostItemHandle.setSeq(seq);
            sysCostItemHandle.setDataFromDesc(dataFromDesc);
            sysCostItemHandle.setEnable(1);
            sysCostItemHandle.setCreateOperId(user.getId());
            sysCostItemHandle.setCreateTime(new Date());
            sysCostItemHandle.setRemark(remark);
            sysCostItemHandle.setParentId(parentId);
            sysCostItemHandleMapper.insert(sysCostItemHandle);
//            seqItemMap.put(level, seq);
            //前一个对象
            lastItemMap.put("lastItem", sysCostItemHandle);
        }
        //更新成本项目full_name
        sysCostItemHandleMapper.updateCountyItemFullName(reportId, user.getTopOrgCode());
    }

    /**
     * 生成成本报表map数据
     */
    private static Map<String, List<SysCostItem>> buildMapData(List<Object> list) {
        Map<String, List<SysCostItem>> map = new LinkedHashMap<>();
        String parent = "";
        //不读取表头
        list.remove(0);
        for (Object object : list) {
            List<String> objects = (List) object;
            if (StrUtils.notEmpty(objects.get(0))) {
                parent = objects.get(0);
                map.put(parent, new ArrayList<>());
                SysCostItem sysCostItem = new SysCostItem();
                sysCostItem.setName(objects.get(1));
                sysCostItem.setDataFromDesc(objects.get(3));
                map.get(parent).add(sysCostItem);
            } else {
                //遍历子项
                SysCostItem sysCostItem = new SysCostItem();
                sysCostItem.setName(objects.get(1));
                sysCostItem.setDataFromDesc(objects.get(3));
                map.get(parent).add(sysCostItem);
            }
        }
        return map;
    }

    /**
     * description: 获取划小一级分类和成本项目
     *
     * @param reportId 报表id
     * @param orgCode 组织编码
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    @Override
    public List<SysCostItem> getCostItem(Long reportId, Long orgCode) {
        List<SysCostItem> list = null;
        List<SysCostItem> data = new ArrayList<>();

        try {
            //list = sysCostItemMapper.getCostItem(reportId);
            Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();
            if(orgCode == 0){
                orgCode = topOrgCode;
            }else{
                topOrgCode = orgCode;
            }
            list = sysCostItemMapper.getCostItemName(-1,reportId,topOrgCode);
            for(SysCostItem sysCostItem : list){
                List<SysCostItem> costItemMenu = sysCostItemMapper.getCostItemMenu(sysCostItem.getName(), reportId, topOrgCode);
                data.addAll(costItemMenu);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public RsCostReportEntity getRsCostReport(RsCostReportEntity rsCostReportEntity) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (rsCostReportEntity.getCurrMonthValue() == null) {
            rsCostReportEntity.setCurrMonthValue("0.0");
        } else {
            //本月数
            rsCostReportEntity.setCurrMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrMonthValue()) / 10000)));
        }
        if (rsCostReportEntity.getLastMonthValue() == null) {
            rsCostReportEntity.setLastMonthValue("0.0");
        } else {
            //上月数
            rsCostReportEntity.setLastMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastMonthValue()) / 10000)));
        }
        if (rsCostReportEntity.getDiffLastMonth() == null) {
            rsCostReportEntity.setDiffLastMonth("0.0");
        } else {
            //比上月增减（万元）
            rsCostReportEntity.setDiffLastMonth(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastMonth()) / 10000)));
        }
        if (rsCostReportEntity.getCurrYearValue() == null) {
            rsCostReportEntity.setCurrYearValue("0.0");
        } else {
            //本年累计数
            rsCostReportEntity.setCurrYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrYearValue()) / 10000)));
        }

        if (rsCostReportEntity.getLastYearValue() == null) {
            rsCostReportEntity.setLastYearValue("0.0");
        } else {
            //上年累计数
            rsCostReportEntity.setLastYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastYearValue()) / 10000)));
        }

        if (rsCostReportEntity.getDiffLastYear() == null) {
            rsCostReportEntity.setDiffLastYear("0.0");
        } else {
            //比上年增减（万元）
            rsCostReportEntity.setDiffLastYear(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastYear()) / 10000)));
        }

        if (rsCostReportEntity.getBudget() == null) {
            rsCostReportEntity.setBudget("0.0");
        }

        if (rsCostReportEntity.getProgress() == null) {
            rsCostReportEntity.setProgress("0.0");
        }

        if (rsCostReportEntity.getRemainingBudget() == null) {
            rsCostReportEntity.setRemainingBudget("0.0");
        } else {
            //剩余预算
            rsCostReportEntity.setRemainingBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getRemainingBudget()) / 10000)));
        }

        if (rsCostReportEntity.getPercLastMonth() == null) {
            rsCostReportEntity.setPercLastMonth("--");
        } else if (!rsCostReportEntity.getPercLastMonth().equals("--")) {
            rsCostReportEntity.setPercLastMonth(df.format(new BigDecimal(rsCostReportEntity.getPercLastMonth())));
        }
        if (rsCostReportEntity.getPercLastYear() == null) {
            rsCostReportEntity.setPercLastYear("--");
        } else if (!rsCostReportEntity.getPercLastYear().equals("--")) {
            rsCostReportEntity.setPercLastYear(df.format(new BigDecimal(rsCostReportEntity.getPercLastYear())));
        }
        return rsCostReportEntity;
    }

    @Override
    public List<SysCostItem> getCityCostItem(Long reportId){
        List<SysCostItem> list = new ArrayList<>();
        SysCostItem costItems = new SysCostItem();
        costItems.setName("营服中心");

        SysCostItem costItem = new SysCostItem();
        costItem.setName("合计");
        list.add(costItems);
        list.add(costItem);
        try {
            Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();
            list.addAll(sysCostItemMapper.getCostItemName(-1,reportId,topOrgCode));

            for(SysCostItem sysCostItem : list){
                List<SysCostItem> costItemMenu1 = sysCostItemMapper.getCostItemMenu(sysCostItem.getName(), reportId, topOrgCode);
                List<SysCostItem> costItemMenu = new ArrayList<>();
                if(costItemMenu1.size() > 0){
                    SysCostItem costItem1 = new SysCostItem();
                    costItem1.setName(sysCostItem.getName());
                    costItem1.setCname("合计");
                    costItemMenu.add(costItem1);
                    costItemMenu.addAll(costItemMenu1);
                }
                sysCostItem.setItemsChildren(costItemMenu);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * description: 获取划小对应成本项目下的数据
     *
     * @param reportId      报表id
     * @param code       组织编码
     * @param beginMonth    月份
     * @param endMonth      月份
     * @param orgCode       组织编码
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */

    /*public List<SysCostItem> getCostItemData(Long reportId, Long code, Long beginMonth,Long endMonth, Long orgCode) {
        List<SysCostItem> list = null;
        try {
            list = getCostItem(reportId,orgCode);
            for(SysCostItem sysCostItem : list){
                List<RsCostReportEntity> itemsChildrens = sysCostItem.getItemsChildrens();
                //本月数
                itemsChildrens.get(0).setCurrMonthValue("0.00");
                //上月数
                itemsChildrens.get(0).setLastMonthValue("0.00");
                //比上月增减（万元）
                itemsChildrens.get(0).setDiffLastMonth("0.00");
                //本年累计数
                itemsChildrens.get(0).setCurrYearValue("0.00");
                //上年累计数
                itemsChildrens.get(0).setLastYearValue("0.00");
                //比上年增减（万元）
                itemsChildrens.get(0).setDiffLastYear("0.00");
                //剩余预算
                itemsChildrens.get(0).setRemainingBudget("0.00");
                itemsChildrens.get(0).setBudget("0.00");

                List<RsCostReportEntity> costItemDatas = sysCostItemMapper.getCostItemDatas(reportId, code, beginMonth,endMonth, sysCostItem.getId());
                for(RsCostReportEntity rsCostReportEntity : costItemDatas){

                    DecimalFormat df = new DecimalFormat("0.00");
                    //本月数
                    rsCostReportEntity.setCurrMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrMonthValue()) / 10000)) );
                    //上月数
                    rsCostReportEntity.setLastMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastMonthValue()) / 10000)) );
                    //比上月增减（万元）
                    rsCostReportEntity.setDiffLastMonth(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastMonth()) / 10000)));
                    //本年累计数
                    rsCostReportEntity.setCurrYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrYearValue()) / 10000)));
                    //上年累计数
                    rsCostReportEntity.setLastYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastYearValue()) / 10000)));
                    //比上年增减（万元）
                    rsCostReportEntity.setDiffLastYear(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastYear()) / 10000)));
                    //剩余预算
                    rsCostReportEntity.setRemainingBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getRemainingBudget()) / 10000)));
                    rsCostReportEntity.setBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getBudget()) / 10000)));

                    //perc_last_month

                    if(!rsCostReportEntity.getPercLastMonth().equals("--")){
                        rsCostReportEntity.setPercLastMonth(df.format(new BigDecimal(rsCostReportEntity.getPercLastMonth())));
                    }
                    if(!rsCostReportEntity.getPercLastYear().equals("--")){
                        rsCostReportEntity.setPercLastYear(df.format(new BigDecimal(rsCostReportEntity.getPercLastYear())));
                    }
                }
                if(costItemDatas.size() > 0){
                    sysCostItem.setItemsChildrens(costItemDatas);
                }
            }

            //list = sysCostItemMapper.getCostItemData(reportId, orgCode, beginMonth, endMonth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }*/
    @Override
    public List<SysCostItem> getCostItemData(Long reportId, Long code, Long beginMonth,Long endMonth, Long orgCode) {
        List<SysCostItem> list = null;
        try {
            list = getCostItem(reportId,orgCode);
            for(SysCostItem sysCostItem : list){
                List<RsCostReportEntity> itemsChildrens = sysCostItem.getItemsChildrens();
                itemsChildrens.get(0).setCostItemId(sysCostItem.getId());
                //本月数
                itemsChildrens.get(0).setCurrMonthValue("0.00");
                //上月数
                itemsChildrens.get(0).setLastMonthValue("0.00");
                //比上月增减（万元）
                itemsChildrens.get(0).setDiffLastMonth("0.00");
                //本年累计数
                itemsChildrens.get(0).setCurrYearValue("0.00");
                //上年累计数
                itemsChildrens.get(0).setLastYearValue("0.00");
                //比上年增减（万元）
                itemsChildrens.get(0).setDiffLastYear("0.00");
                //剩余预算
                itemsChildrens.get(0).setRemainingBudget("0.00");
                itemsChildrens.get(0).setBudget("0.00");

                List<RsCostReportEntity> costItemDatas = sysCostItemMapper.getCostItemDatas(reportId, code, beginMonth,endMonth, sysCostItem.getId());
                for(RsCostReportEntity rsCostReportEntity : costItemDatas){

                    DecimalFormat df = new DecimalFormat("0.00");
                    rsCostReportEntity.setCostItemId(sysCostItem.getId());
                    //本月数
                    rsCostReportEntity.setCurrMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrMonthValue()) / 10000)) );
                    //上月数
                    rsCostReportEntity.setLastMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastMonthValue()) / 10000)) );
                    //比上月增减（万元）
                    rsCostReportEntity.setDiffLastMonth(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastMonth()) / 10000)));
                    //本年累计数
                    rsCostReportEntity.setCurrYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrYearValue()) / 10000)));
                    //上年累计数
                    rsCostReportEntity.setLastYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastYearValue()) / 10000)));
                    //比上年增减（万元）
                    rsCostReportEntity.setDiffLastYear(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastYear()) / 10000)));
                    //剩余预算
                    rsCostReportEntity.setRemainingBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getRemainingBudget()) / 10000)));
                    rsCostReportEntity.setBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getBudget()) / 10000)));

                    //perc_last_month

                    if(!rsCostReportEntity.getPercLastMonth().equals("--")){
                        rsCostReportEntity.setPercLastMonth(df.format(new BigDecimal(rsCostReportEntity.getPercLastMonth())));
                    }
                    if(!rsCostReportEntity.getPercLastYear().equals("--")){
                        rsCostReportEntity.setPercLastYear(df.format(new BigDecimal(rsCostReportEntity.getPercLastYear())));
                    }
                }
                if(costItemDatas.size() > 0){
                    sysCostItem.setItemsChildrens(costItemDatas);
                }
            }

            //list = sysCostItemMapper.getCostItemData(reportId, orgCode, beginMonth, endMonth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<SysCostItem> getCountyCostItemData(Long reportId, Long code, Long beginMonth,Long endMonth, Long orgCode) {
        List<SysCostItem> list = null;
        try {
            list = getCostItem(reportId,orgCode);
            for(SysCostItem sysCostItem : list){
                List<RsCostReportEntity> itemsChildrens = sysCostItem.getItemsChildrens();
                //本月数
                itemsChildrens.get(0).setCurrMonthValue("0.00");
                //上月数
                itemsChildrens.get(0).setLastMonthValue("0.00");
                //比上月增减（万元）
                itemsChildrens.get(0).setDiffLastMonth("0.00");
                //本年累计数
                itemsChildrens.get(0).setCurrYearValue("0.00");
                //上年累计数
                itemsChildrens.get(0).setLastYearValue("0.00");
                //比上年增减（万元）
                itemsChildrens.get(0).setDiffLastYear("0.00");
                //剩余预算
                itemsChildrens.get(0).setRemainingBudget("0.00");
                itemsChildrens.get(0).setBudget("0.00");

                List<RsCostReportEntity> costItemDatas = sysCostItemMapper.getCostItemDatas(reportId, code, beginMonth,endMonth, sysCostItem.getId());
                for(RsCostReportEntity rsCostReportEntity : costItemDatas){

                    DecimalFormat df = new DecimalFormat("0.00");
                    //本月数
                    rsCostReportEntity.setCurrMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrMonthValue()) / 10000)) );
                    //上月数
                    rsCostReportEntity.setLastMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastMonthValue()) / 10000)) );
                    //比上月增减（万元）
                    rsCostReportEntity.setDiffLastMonth(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastMonth()) / 10000)));
                    //本年累计数
                    rsCostReportEntity.setCurrYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrYearValue()) / 10000)));
                    //上年累计数
                    rsCostReportEntity.setLastYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastYearValue()) / 10000)));
                    //比上年增减（万元）
                    rsCostReportEntity.setDiffLastYear(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastYear()) / 10000)));
                    //剩余预算
                    rsCostReportEntity.setRemainingBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getRemainingBudget()) / 10000)));
                    rsCostReportEntity.setBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getBudget()) / 10000)));
                    //perc_last_month

                    if(!rsCostReportEntity.getPercLastMonth().equals("--")){
                        rsCostReportEntity.setPercLastMonth(df.format(new BigDecimal(rsCostReportEntity.getPercLastMonth())));
                    }
                    if(!rsCostReportEntity.getPercLastYear().equals("--")){
                        rsCostReportEntity.setPercLastYear(df.format(new BigDecimal(rsCostReportEntity.getPercLastYear())));
                    }
                }
                if(costItemDatas.size() > 0){
                    sysCostItem.setItemsChildrens(costItemDatas);
                }
            }

            //list = sysCostItemMapper.getCostItemData(reportId, orgCode, beginMonth, endMonth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<SysCostItem> getByOrgCodeAndReportIdColumn(Long reportId, Long topOrgCode){
        return sysCostItemMapper.getByOrgCodeAndReportIdColumn(reportId,topOrgCode);
    }
    @Override
    public Map<String, Object> getCityCostItemData(String level, Long parentCode, Long reportId, Long beginMonth, Long endMonth, Long orgCode){
        Map<String, Object> map = rsCostReportService.getOrganization(level, parentCode, reportId);
        List<SysOrganization> list = (List<SysOrganization>) map.get("list");

        for(SysOrganization sysOrganization : list){
            // 获取分公司下的营服中心
            List<SysOrgAnizationCamp> childrens = sysOrganization.getChildrens();


            SysOrgAnizationCamp sysOrgAnizationCampInfo = new SysOrgAnizationCamp();
            List<SysCostItem> costItemData2 = getCostItemData(reportId, sysOrganization.getCode(), beginMonth,endMonth, parentCode);
            if(costItemData2.size() > 0){

                String name = "";
                // 去重
                Map<String,String> dataMap = new HashMap<>();
                for(int i = 0;i<costItemData2.size();i++){
                    dataMap.put(costItemData2.get(i).getName(),costItemData2.get(i).getName());
                }

                Iterator<String> iterator = dataMap.keySet().iterator();
                double sums = 0D;
                // 统计每个分类下的总额
                while (iterator.hasNext()){
                    String next = iterator.next();
                    double sum = 0D;
                    for(int i = 0;i<costItemData2.size();i++){
                        if(next.equals(costItemData2.get(i).getName())){
                            name = costItemData2.get(i).getName();
                            for(int l = 0;l<costItemData2.get(i).getItemsChildrens().size();l++){
                                sum += Double.parseDouble(costItemData2.get(i).getItemsChildrens().get(l).getCurrMonthValue());
                            }

                            //sum += Double.parseDouble(costItemData2.get(i).getCurrMonthValue());
                        }
                    }
                    sums += sum;
                    SysCostItem sysCostItem = new SysCostItem();
                    sysCostItem.setCname("合计");
                    sysCostItem.setName(name);
                    sysCostItem.setCurrMonthValue((int)sum+"");
                    costItemData2.add(sysCostItem);
                }

                sysOrganization.setItemsChildren(costItemData2);

                sysOrganization.setCname("合计");
                sysOrganization.setCurrMonthValue((int)sums+"");
            }






            for(SysOrgAnizationCamp sysOrgAnizationCamp : childrens){
                // 获取费用数据
                List<SysCostItem> costItemData = getCostItemData(reportId, sysOrgAnizationCamp.getCode(), beginMonth,endMonth, parentCode);
                if(costItemData.size() > 0){

                    String name = "";
                    // 去重
                    Map<String,String> dataMap = new HashMap<>();
                    for(int i = 0;i<costItemData.size();i++){
                        dataMap.put(costItemData.get(i).getName(),costItemData.get(i).getName());
                    }

                    Iterator<String> iterator = dataMap.keySet().iterator();
                    double sums = 0D;
                    // 统计每个分类下的总额
                    while (iterator.hasNext()){
                        String next = iterator.next();
                        double sum = 0D;
                        for(int i = 0;i<costItemData.size();i++){
                            if(next.equals(costItemData.get(i).getName())){
                                name = costItemData.get(i).getName();
                                for(int l = 0;l<costItemData2.get(i).getItemsChildrens().size();l++){
                                    sum += Double.parseDouble(costItemData2.get(i).getItemsChildrens().get(l).getCurrMonthValue());
                                }
                                //sum += Double.parseDouble(costItemData.get(i).getCurrMonthValue());
                            }
                        }
                        sums += sum;
                        SysCostItem sysCostItem = new SysCostItem();
                        sysCostItem.setCname("合计");
                        sysCostItem.setName(name);
                        sysCostItem.setCurrMonthValue((int)sum+"");
                        costItemData.add(sysCostItem);
                    }

                    sysOrgAnizationCamp.setItemsChildren(costItemData);

                    sysOrgAnizationCamp.setCname("合计");
                    sysOrgAnizationCamp.setCurrMonthValue((int)sums+"");
                }
            }
        }
        return map;
    }

    public void get(){

    }

    /**
     * description: 获取区县对应成本项目下的数据
     *
     * @param reportId 报表id
     * @param orgCode 组织编码
     * @param month 月份
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SysCostItem>
     */
    @Override
    public List<SysCostItem> getCostItemCountyData(Long reportId, Long orgCode, Long month,Long userOrgCode) {
        List<SysCostItem> list = null;
        try {

            Integer year = DateUtils.getYear(DateUtils.getCurrentDate());
            SysUser user = ShiroUtils.getUser();
            Long topOrgCode = user.getTopOrgCode();
            if(userOrgCode != 0){
                topOrgCode = userOrgCode;
            }

            List<SysOrganization> organization = sysOrganizationMapper.getOrganization("3", topOrgCode);
            /*SysOrganization sysOrganizationInfo = new SysOrganization();
            for(SysOrganization sysOrganization : organization){
                Long code = sysOrganization.getCode();
                if(orgCode.equals(code)){
                    sysOrganizationInfo = sysOrganization;
                    break;
                }
            }*/
            if(organization.size() > 0){
                orgCode = organization.get(0).getCode();
                list = sysCostItemMapper.getOrganizationByOrgCode(reportId, orgCode, year, topOrgCode, month);
                //for(SysCostItem sysCostItem : list){
                    //List<RsCostReportEntity> costItemCountyData = sysCostItem.getItemsChildrens();
                    //for(RsCostReportEntity rsCostReportEntity : costItemCountyData){
                    //    rsCostReportEntity = getRsCostReport(rsCostReportEntity);
                    /*DecimalFormat df = new DecimalFormat("0.00");
                    if(rsCostReportEntity.getCurrMonthValue() == null){
                        rsCostReportEntity.setCurrMonthValue("0.0");
                    }else{
                        //本月数
                        rsCostReportEntity.setCurrMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrMonthValue()) / 10000)) );
                    }
                    if(rsCostReportEntity.getLastMonthValue() == null){
                        rsCostReportEntity.setLastMonthValue("0.0");
                    }else{
                        //上月数
                        rsCostReportEntity.setLastMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastMonthValue()) / 10000)) );
                    }
                    if(rsCostReportEntity.getDiffLastMonth() == null){
                        rsCostReportEntity.setDiffLastMonth("0.0");
                    }else{
                        //比上月增减（万元）
                        rsCostReportEntity.setDiffLastMonth(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastMonth()) / 10000)));
                    }
                    if(rsCostReportEntity.getCurrYearValue() == null){
                        rsCostReportEntity.setCurrYearValue("0.0");
                    }else{
                        //本年累计数
                        rsCostReportEntity.setCurrYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrYearValue()) / 10000)));
                    }

                    if(rsCostReportEntity.getLastYearValue() == null){
                        rsCostReportEntity.setLastYearValue("0.0");
                    }else{
                        //上年累计数
                        rsCostReportEntity.setLastYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastYearValue()) / 10000)));
                    }

                    if(rsCostReportEntity.getDiffLastYear() == null){
                        rsCostReportEntity.setDiffLastYear("0.0");
                    }else{
                        //比上年增减（万元）
                        rsCostReportEntity.setDiffLastYear(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastYear()) / 10000)));
                    }

                    if(rsCostReportEntity.getBudget() == null){
                        rsCostReportEntity.setBudget("0.0");
                    }

                    if(rsCostReportEntity.getProgress() == null){
                        rsCostReportEntity.setProgress("0.0");
                    }

                    if(rsCostReportEntity.getRemainingBudget() == null){
                        rsCostReportEntity.setRemainingBudget("0.0");
                    }else{
                        //剩余预算
                        rsCostReportEntity.setRemainingBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getRemainingBudget()) / 10000)));
                    }

                    if(rsCostReportEntity.getPercLastMonth() == null){
                        rsCostReportEntity.setPercLastMonth("--");
                    }else if(!rsCostReportEntity.getPercLastMonth().equals("--")){
                        rsCostReportEntity.setPercLastMonth(df.format(new BigDecimal(rsCostReportEntity.getPercLastMonth())));
                    }
                    if(rsCostReportEntity.getPercLastYear() == null){
                        rsCostReportEntity.setPercLastYear("--");
                    }else if(!rsCostReportEntity.getPercLastYear().equals("--")){
                        rsCostReportEntity.setPercLastYear(df.format(new BigDecimal(rsCostReportEntity.getPercLastYear())));
                    }*/
                    //}
                //}
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * description: 获取所有区县对应成本项目下的数据
     *
     * @param reportId
     * @param month
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysOrganization>
     */
    @Override
    public List<SysOrganization> getAllCostItemCountyData(Long reportId, Long month,Long topOrgCode, Long orgCode) {
        List<SysOrganization> organization = null;
        try {
            Integer year = DateUtils.getYear(DateUtils.getCurrentDate());
            SysUser user = ShiroUtils.getUser();
            Long tempTopOrgCode = user.getTopOrgCode();
            if(tempTopOrgCode != -1){
                topOrgCode = tempTopOrgCode;
            }
            organization = sysOrganizationMapper.getOrganization("3", topOrgCode);
            List<SysOrganization> list = new ArrayList<>();
            // 判断显示多少区
            for(SysOrganization sysOrganization : organization){
                Long code = sysOrganization.getCode();
                if(code.equals(orgCode)){
                    list.add(sysOrganization);
                    break;
                }
            }
            if(list.size() > 0){
                organization = list;
            }
            for(SysOrganization sysOrganization : organization){
                Long code = sysOrganization.getCode();
                List<RsCostReportEntity> organizationByOrgCodeData = sysCostItemMapper.getOrganizationByOrgCodeData(reportId, code, year, topOrgCode, month);
                for(RsCostReportEntity rsCostReportEntity : organizationByOrgCodeData){
                    rsCostReportEntity = getRsCostReport(rsCostReportEntity);
                }
                sysOrganization.setChildrensMonth(organizationByOrgCodeData);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return organization;
    }

    /**
     * description: 获取城市
     *
     * @param
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysOrganization>
     */
    @Override
    public List<SysOrganization> getCity(){
        QueryWrapper<SysOrganization> qw = new QueryWrapper<>();
        qw.select("*").eq("parent_code",-1).orderByAsc("seq");
        List<SysOrganization> list = sysOrganizationMapper.selectList(qw);
        System.out.println(list);
        return list;
    }
/*
    public List<SysCostItem> getCostItemCountyData(Long reportId, Long orgCode, Long month,Long userOrgCode) {
        List<SysCostItem> list = null;
        try {

            Integer year = DateUtils.getYear(DateUtils.getCurrentDate());
            SysUser user = ShiroUtils.getUser();
            Long topOrgCode = user.getTopOrgCode();
            if(userOrgCode != 0){
                topOrgCode = userOrgCode;
            }

            list = sysCostItemMapper.getCountyItem(reportId.intValue(),topOrgCode,year);

            for(SysCostItem sysCostItem : list){
                List<RsCostReportEntity> itemsChildrens = sysCostItem.getItemsChildrens();



                List<RsCostReportEntity> costItemCountyData = sysCostItemMapper.getCostItemCountDatas(reportId, orgCode, month, sysCostItem.getId());
                for(RsCostReportEntity rsCostReportEntity : costItemCountyData){


                    DecimalFormat df = new DecimalFormat("0.00");
                    //本月数
                    rsCostReportEntity.setCurrMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrMonthValue()) / 10000)) );
                    //上月数
                    rsCostReportEntity.setLastMonthValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastMonthValue()) / 10000)) );
                    //比上月增减（万元）
                    rsCostReportEntity.setDiffLastMonth(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastMonth()) / 10000)));
                    //本年累计数
                    rsCostReportEntity.setCurrYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrYearValue()) / 10000)));
                    //上年累计数
                    rsCostReportEntity.setLastYearValue(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getLastYearValue()) / 10000)));
                    //比上年增减（万元）
                    rsCostReportEntity.setDiffLastYear(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getDiffLastYear()) / 10000)));
                    //剩余预算
                    rsCostReportEntity.setRemainingBudget(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getRemainingBudget()) / 10000)));
                    if(!rsCostReportEntity.getPercLastMonth().equals("--")){
                        rsCostReportEntity.setPercLastMonth(df.format(new BigDecimal(rsCostReportEntity.getPercLastMonth())));
                    }
                    if(!rsCostReportEntity.getPercLastYear().equals("--")){
                        rsCostReportEntity.setPercLastYear(df.format(new BigDecimal(rsCostReportEntity.getPercLastYear())));
                    }
                }


                if(costItemCountyData != null && costItemCountyData.size() != 0){
                    costItemCountyData.add(new RsCostReportEntity());
                }else{
                    costItemCountyData = new ArrayList<>();
                    RsCostReportEntity rsCostReportEntity = new RsCostReportEntity();
                    //本月数
                    rsCostReportEntity.setCurrMonthValue("0.00");
                    //上月数
                    rsCostReportEntity.setLastMonthValue("0.00");
                    //比上月增减（万元）
                    rsCostReportEntity.setDiffLastMonth("0.00");
                    //本年累计数
                    rsCostReportEntity.setCurrYearValue("0.00");
                    //上年累计数
                    rsCostReportEntity.setLastYearValue("0.00");
                    //比上年增减（万元）
                    rsCostReportEntity.setDiffLastYear("0.00");
                    //剩余预算
                    rsCostReportEntity.setRemainingBudget("0.00");
                    rsCostReportEntity.setBudget("0.00");
                    costItemCountyData.add(rsCostReportEntity);
                }
                sysCostItem.setItemsChildrens(costItemCountyData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
*/
}


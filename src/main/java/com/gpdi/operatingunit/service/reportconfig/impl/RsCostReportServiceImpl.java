package com.gpdi.operatingunit.service.reportconfig.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.reportconfig.RsCostReportDao;
import com.gpdi.operatingunit.dao.reportconfig.SysCostItemMapper;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.dao.system.SysRoleMapper;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrgAnizationCamp;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysRole;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.data.QueryData;
import com.gpdi.operatingunit.service.parallel.RsCostReportCallable;
import com.gpdi.operatingunit.service.reportconfig.RsCostReportService;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Author：long
 * @Date：2019/10/30 10:03
 * @Description：
 */
@Service
public class RsCostReportServiceImpl implements RsCostReportService {

    @Autowired
    private RsCostReportDao rsCostReportDao;

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Autowired
    private SysCostItemMapper sysCostItemMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public Map<String, Object> getOrganization(String level, Long parentCode, Long reportId) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 获取市
            List<SysOrganization> list = getList(level, parentCode, reportId);
            map.put("list", list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    /**
     * description: 区成本报表
     *
     * @param level
     * @param parentCode
     * @param reportId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> getCountyOrganization(String level, Long parentCode, Long reportId) {
        Map<String, Object> map = new HashMap<>();
        try {
            Long topOrgCode = ShiroUtils.getUser().getTopOrgCode();
            if(topOrgCode != -1){
                parentCode = topOrgCode;
            }
            // 获取市
            List<SysOrganization> list = getList(level, parentCode, reportId);
            for(SysOrganization sysOrganization : list){
                List<SysOrgAnizationCamp> childrens = sysOrganization.getChildrens();
                SysOrgAnizationCamp sysOrgAnizationCamp = new SysOrgAnizationCamp();
                sysOrgAnizationCamp.setName("区划小预算");
                sysOrgAnizationCamp.setAlias("区划小预算");
                sysOrgAnizationCamp.setId(0);
                childrens.add(0,sysOrgAnizationCamp);
                SysOrgAnizationCamp sysOrgAnizationCamp2 = new SysOrgAnizationCamp();
                sysOrgAnizationCamp2.setId(1);
                sysOrgAnizationCamp2.setName("区划小进度");
                sysOrgAnizationCamp2.setAlias("区划小进度");
                childrens.add(1,sysOrgAnizationCamp2);
                SysOrgAnizationCamp sysOrgAnizationCamp3 = new SysOrgAnizationCamp();
                sysOrgAnizationCamp3.setName("营服合计");
                sysOrgAnizationCamp3.setAlias("营服合计");
                sysOrgAnizationCamp3.setId(2);
                childrens.add(2,sysOrgAnizationCamp3);

                SysOrgAnizationCamp sysOrgAnizationCamp4 = new SysOrgAnizationCamp();
                sysOrgAnizationCamp4.setName("区合计");
                sysOrgAnizationCamp4.setAlias("区合计");
                sysOrgAnizationCamp4.setId(3);
                childrens.add(3,sysOrgAnizationCamp4);
            }
            map.put("list", list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> getCounty(String level, Long parentCode, Long reportId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<SysOrganization> list = sysOrganizationMapper.getOrganization(level,parentCode);

            SysUser user = ShiroUtils.getUser();
            List<SysOrganization> byTopOrgCodeAndOrgCodeList = sysOrganizationMapper.getByTopOrgCodeAndOrgCode(user.getTopOrgCode(), user.getOrgCode());
            List<SysOrganization> byTopOrgCodeAndOrgCodeCity = sysOrganizationMapper.getByTopOrgCodeAndOrgCodeCity(user.getTopOrgCode(), user.getOrgCode());
            // 为区角色
            if(byTopOrgCodeAndOrgCodeList.size() >= 0 && byTopOrgCodeAndOrgCodeList.get(0) != null && byTopOrgCodeAndOrgCodeList.get(0).getLevel() == 3){
                SysOrganization byTopOrgCodeAndOrgCode = byTopOrgCodeAndOrgCodeList.get(0);
                List<SysOrganization> tempList = new ArrayList<>();
                for(SysOrganization sysOrganization : list){
                    if(sysOrganization.getName().equals(byTopOrgCodeAndOrgCode.getName())){
                        tempList.add(sysOrganization);
                        break;
                    }
                }
                list = tempList;
            }

            for(SysOrganization sysOrganization : list){
                Long campCode = sysOrganization.getCode();
                List<RsCostReportEntity> timeList = new ArrayList<>();
                timeList.addAll(rsCostReportDao.getCostReportTime(campCode, reportId));
                /*List<SysOrgAnizationCamp> childrens = sysOrganization.getChildrens();
                List<RsCostReportEntity> timeList = new ArrayList<>();
                for(SysOrgAnizationCamp sysOrgAnizationCamp : childrens){
                    Long campCode = sysOrgAnizationCamp.getCode();
                    timeList.addAll(rsCostReportDao.getCostReportTime(campCode, reportId));
                }*/
                sysOrganization.setChildrensMonth(timeList);
            }
            map.put("list", list);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return map;
    }

    public List<SysCostItem> get(List<SysOrganization> organization,Long reportId, Long topOrgCode){
        DecimalFormat df = new DecimalFormat("0.00");
        List<SysCostItem> tempList = new ArrayList<>();
        Map<String,Integer> maps = new HashMap<>();
        for (int k = 3;k<organization.size();k++) {
            Long code = organization.get(k).getCode();
            String name = "";
            tempList = new ArrayList<>();
            for(SysCostItem sysCostItem : sysCostItemMapper.getCostItemName(-1,reportId,topOrgCode)){
                List<SysCostItem> costItemMenu = sysCostItemMapper.getCostItemMenu(sysCostItem.getName(), reportId, topOrgCode);
                tempList.addAll(costItemMenu);
            }
            for (int i = 0; i < tempList.size(); ) {
                if (i == 0) {
                    name = tempList.get(i).getName();
                    // 设置所属分类
                    SysCostItem sysCostItem = new SysCostItem();
                    sysCostItem.setCname("合计");
                    sysCostItem.setName(name);
                    tempList.add(i, sysCostItem);
                    maps.put(name, i);
                }
                if (name.equals(tempList.get(i).getName())) {
                    i++;
                }
                else {
                    name = tempList.get(i).getName();
                    // 设置所属分类
                    SysCostItem sysCostItem = new SysCostItem();
                    sysCostItem.setName(name);
                    sysCostItem.setCname("合计");
                    tempList.add(i, sysCostItem);
                }
            }
        }
        return tempList;
    }

    @Override
    public List<SysOrganization> getCountyData(Long reportId,Long month,Long endMonth,Long orgCode,Long codes) {
        /*Long reportId = 10001L;
        Long orgCode = 757000000L;
        Long month = 201907L;
        Long endMonth = 201908L;*/
        // 获取顺德下的营服
        List<SysOrganization> organization = new ArrayList<>();
        SysOrganization sysOrganizationBudget = new SysOrganization();
        sysOrganizationBudget.setName("区划小预算");
        sysOrganizationBudget.setCname("合计");
        organization.add(sysOrganizationBudget);

        SysOrganization sysOrganizationProgress = new SysOrganization();
        sysOrganizationProgress.setName("区划小进度");
        organization.add(sysOrganizationProgress);

        SysOrganization sysOrganizationying = new SysOrganization();
        sysOrganizationying.setName("营服合计");
        organization.add(sysOrganizationying);

        SysOrganization sysOrganizationqu = new SysOrganization();
        sysOrganizationqu.setName("区合计");
        organization.add(sysOrganizationqu);

        organization.addAll(sysOrganizationMapper.getOrganization("4", codes));
        // 获取需要展示的字段


        List<SysCostItem> data = new ArrayList<>();
        Long topOrgCode = orgCode;

        // 字段


        List<SysCostItem> lists = new ArrayList<>();
        List<SysCostItem> listy = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        Map<String,Integer> maps = new HashMap<>();
        lists = get(organization,reportId,topOrgCode);
        for(int i = 0;i<lists.size();i++){


            SysCostItem sysCostItem = new SysCostItem();
            sysCostItem.setName(lists.get(i).getName());
            sysCostItem.setCname(lists.get(i).getCname());

            List<RsCostReportEntity> itemsChildrenst = new ArrayList<>();
            RsCostReportEntity rsCostReportEntitys = new RsCostReportEntity();
            rsCostReportEntitys.setCurrMonthValue("0.00");
            itemsChildrenst.add(rsCostReportEntitys);
            sysCostItem.setItemsChildrens(itemsChildrenst);
            listy.add(sysCostItem);
        }
        /*for (int k = 3;k<organization.size();k++) {
            Long code = organization.get(k).getCode();
            String name = "";
            List<SysCostItem> tempList = new ArrayList<>();
            for(SysCostItem sysCostItem : sysCostItemMapper.getCostItemName(-1,reportId,topOrgCode)){
                List<SysCostItem> costItemMenu = sysCostItemMapper.getCostItemMenu(sysCostItem.getName(), reportId, topOrgCode);
                tempList.addAll(costItemMenu);
            }
            for (int i = 0; i < tempList.size(); ) {
                if (i == 0) {
                    name = tempList.get(i).getName();
                    // 设置所属分类
                    SysCostItem sysCostItem = new SysCostItem();
                    sysCostItem.setCname("合计");
                    sysCostItem.setName(name);
                    tempList.add(i, sysCostItem);
                    maps.put(name, i);
                }
                if (name.equals(tempList.get(i).getName())) {
                    i++;
                }
                else {
                    name = tempList.get(i).getName();
                    // 设置所属分类
                    SysCostItem sysCostItem = new SysCostItem();
                    sysCostItem.setName(name);
                    sysCostItem.setCname("合计");
                    tempList.add(i, sysCostItem);
                }
            }
            lists = tempList;
            break;
        }*/
        //listy = get(organization,reportId,topOrgCode);
        /*for (int k = 3;k<organization.size();k++) {
            Long code = organization.get(k).getCode();
            String name = "";
            List<SysCostItem> tempLists = new ArrayList<>();
            for(SysCostItem sysCostItem : sysCostItemMapper.getCostItemName(-1,reportId,topOrgCode)){
                List<SysCostItem> costItemMenu = sysCostItemMapper.getCostItemMenu(sysCostItem.getName(), reportId, topOrgCode);
                tempLists.addAll(costItemMenu);
            }
            for (int i = 0; i < tempLists.size(); ) {
                if (i == 0) {
                    name = tempLists.get(i).getName();
                    // 设置所属分类
                    SysCostItem sysCostItem = new SysCostItem();
                    sysCostItem.setCname("合计");
                    sysCostItem.setName(name);
                    tempLists.add(i, sysCostItem);
                    maps.put(name, i);
                }
                if (name.equals(tempLists.get(i).getName())) {
                    i++;
                }
                else {
                    name = tempLists.get(i).getName();
                    // 设置所属分类
                    SysCostItem sysCostItem = new SysCostItem();
                    sysCostItem.setName(name);
                    sysCostItem.setCname("合计");
                    tempLists.add(i, sysCostItem);
                }
            }
            listy = tempLists;
            break;
        }*/



        Map<String,Integer> map = new HashMap<>();
        double countyMonth = 0.0;
        double countyMonthy = 0.0;
        Map<String,Double> dMap = new HashMap<>();
        dMap.put("countyMonth",countyMonth);
        dMap.put("countyMonthy",countyMonthy);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(15, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20000));
        threadPoolExecutor.allowCoreThreadTimeOut(true); //超过空闲时间后，允许关闭核心线程
        ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(threadPoolExecutor);
        //ExecutorService pool = Executors.newFixedThreadPool(organization.size());
        // 区
        int taskNum = 0;
        for (int k = 0;k<organization.size();k++) {
            Long code = organization.get(k).getCode();
            List<SysCostItem> list = get(organization,reportId,topOrgCode);
            executorCompletionService.submit(new RsCostReportCallable(code, list, map,organization,k,reportId,month,endMonth,
                    lists,listy,dMap));
            taskNum ++;
        }

        for (int i = 0; i < taskNum; i++) {
            try {
                executorCompletionService.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        threadPoolExecutor.shutdown();
        //pool.shutdown();
        // 营服合计
        organization.get(2).setItemsChildren(listy);
        //organization.get(2).setCurrMonthValue(countyMonthy+"");

        organization.get(2).setCurrMonthValue(df.format(new BigDecimal(dMap.get("countyMonthy"))));

        // 区合计
        organization.get(3).setItemsChildren(lists);
        organization.get(3).setCurrMonthValue(df.format(new BigDecimal(dMap.get("countyMonth"))));
        return organization;
    }

    @Override
    public List<Map<Object, Object>> getData(QueryData queryData, Long topOrgCode, Long code) {
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
        List<SysOrganization> districts = getDistrict(topOrgCode);
        List<SysOrganization> district = new ArrayList<>();
        for(SysOrganization sysOrganization : districts){
            Long tempCode = sysOrganization.getCode();
            if(tempCode.equals(code)){
                district.add(sysOrganization);
                break;
            }
        }
        //获取数据
        Map<String,RsCostReportEntity> valueMap = getValueMap(queryData,topOrgCode,code);

        DecimalFormat df = new DecimalFormat("0.00");
        //生成存放结果的集合
        List<Map<Object, Object>> result = new ArrayList<>();
        for (SysOrganization org : district) {
            Map<Object,Object> map = new LinkedHashMap<>();
            map.put("id",org.getCode());
            map.put("center",org.getAlias());
            map.put("centerCode",org.getCode());
            /*for (SysCostItem col : columns) {
                if (col.getLevel() == 1 && !col.getName().equals("合计")){

                }else{
                    String key = org.getCode()+"_"+org.getCode()+"_"+col.getId();
                    RsCostReportEntity rsCostReportEntity = valueMap.get(key);
                    Object value = getValue(valueMap.get(org.getCode()+"_"+org.getCode()+"_"+col.getId()));
                    String format = df.format(new BigDecimal(value.toString()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_UP));
                    map.put(col.getId(),format);
                }
            }*/
            List<Map<Object,Object>> list = new ArrayList<>();
            map.put("children",list);
            result.add(map);
        }

        List<Map<Object,Object>> list = new ArrayList<>();

        for (Map<Object, Object> res : result) {

            Long centerCode = (Long) res.get("centerCode");
            List<SysOrganization> center = sysOrganizationMapper.getParentCode(centerCode);
            SysOrganization sysOrganization = new SysOrganization();
            sysOrganization.setName("区合计");
            sysOrganization.setAlias("区合计");
            sysOrganization.setCode(centerCode);
            sysOrganization.setParentCode(centerCode);
            center.add(0,sysOrganization);
            List<Map<Object,Object>> children = (List<Map<Object, Object>>) res.get("children");
            for (SysOrganization cen : center) {
                Map<Object,Object> mm = new IdentityHashMap<>();
                //Map<Object,Object> mm = new LinkedHashMap<>();
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
            list = children;
            //res.put("children",children);
        }


        SysOrganization byTopOrgCodeAndParentCode = sysOrganizationMapper.getByTopOrgCodeAndParentCode(topOrgCode, code);
        // 营服合计
        Map<Object, Object> costReportValueMap = getCostReportValueMap(queryData, topOrgCode, code, byTopOrgCodeAndParentCode, columns, valueMap);
        list.add(0,costReportValueMap);

        Map<Object, Object> progressValueMap = getProgressValueMap(queryData, topOrgCode, code, byTopOrgCodeAndParentCode, columns, valueMap);
        list.add(0,progressValueMap);

        Map<Object, Object> budgetValueMap = getBudgetValueMap(queryData, topOrgCode, code, byTopOrgCodeAndParentCode, columns, valueMap);
        list.add(0,budgetValueMap);

        //result.get(0).put("children",list);
        return list;
    }

    /**
     * description: 营服合计
     *
     * @param queryData
     * @param topOrgCode
     * @param code
     * @param byTopOrgCodeAndParentCode
     * @param columns
     * @param valueMap
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     */
    public Map<Object,Object> getCostReportValueMap(QueryData queryData, Long topOrgCode, Long code, SysOrganization byTopOrgCodeAndParentCode, List<SysCostItem> columns
    ,Map<String,RsCostReportEntity> valueMap){
        DecimalFormat df = new DecimalFormat("0.00");
        Map<String, RsCostReportEntity> costReportValueMap = getCostReportValueMap(queryData, topOrgCode, code, byTopOrgCodeAndParentCode.getCode());
        Map<Object,Object> mm = new IdentityHashMap<>();
        mm.put("id",-1);
        mm.put("center","营服合计");
        mm.put("centerCode",-1);
        for (SysCostItem col : columns) {
            if (col.getLevel() == 1 && !col.getName().equals("合计")){

            }else{
                String key =  "-1_-1_" + col.getId();
                RsCostReportEntity rsCostReportEntity = valueMap.get(key);
                Object value = getValue(costReportValueMap.get(key));
                if(col.getId() == -1){
                    RsCostReportEntity cityAllCountValue = rsCostReportDao.getAllCountyCostReportCountValue(queryData.getReportId(), queryData.getFromMonth(),
                            queryData.getToMonth(), topOrgCode, code, byTopOrgCodeAndParentCode.getCode());
                    if(cityAllCountValue != null && cityAllCountValue.getCurrMonthValue() != null){
                        value = cityAllCountValue.getCurrMonthValue();
                    }
                }
                String format = df.format(new BigDecimal(value.toString()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_UP));
                mm.put(col.getId(),format);
            }
        }
        return mm;
        //list.add(0,mm);
    }

    /**
     * description: 区划小进度
     *
     * @param queryData
     * @param topOrgCode
     * @param code
     * @param byTopOrgCodeAndParentCode
     * @param columns
     * @param valueMap
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     */
    public Map<Object,Object> getProgressValueMap(QueryData queryData, Long topOrgCode, Long code, SysOrganization byTopOrgCodeAndParentCode, List<SysCostItem> columns
    ,Map<String,RsCostReportEntity> valueMap){
        DecimalFormat df = new DecimalFormat("0.00");
        Map<String, RsCostReportEntity> costReportValueMap = getProgressValueMap(queryData, topOrgCode, code, byTopOrgCodeAndParentCode.getCode());
        Map<Object,Object> mm = new IdentityHashMap<>();
        mm.put("id",-2);
        mm.put("center","区划小进度");
        mm.put("centerCode",-2);
        for (SysCostItem col : columns) {
            if (col.getLevel() == 1 && !col.getName().equals("合计")){

            }else{
                String key =  "-2_-2_" + col.getId();
                Object value = getProgress(costReportValueMap.get(key));
                if(col.getId() == -1){
                    RsCostReportEntity cityAllCountValue = rsCostReportDao.getAllCountyProgressCountValue(queryData.getReportId(), queryData.getFromMonth(),
                            queryData.getToMonth(), topOrgCode, code, byTopOrgCodeAndParentCode.getCode());
                    if(cityAllCountValue != null && cityAllCountValue.getProgress() != null){
                        value = cityAllCountValue.getProgress();
                    }
                }
                String format = df.format(new BigDecimal(value.toString()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_UP));
                mm.put(col.getId(),format);
            }
        }
        return mm;
        //list.add(0,mm);
    }

    /**
     * description: 区划小预算
     *
     * @param queryData
     * @param topOrgCode
     * @param code
     * @param byTopOrgCodeAndParentCode
     * @param columns
     * @param valueMap
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     */
    public Map<Object,Object> getBudgetValueMap(QueryData queryData, Long topOrgCode, Long code, SysOrganization byTopOrgCodeAndParentCode, List<SysCostItem> columns
    ,Map<String,RsCostReportEntity> valueMap){
        DecimalFormat df = new DecimalFormat("0.00");
        Map<String, RsCostReportEntity> costReportValueMap = getBudgetValueMap(queryData, topOrgCode, code, byTopOrgCodeAndParentCode.getCode());
        Map<Object,Object> mm = new IdentityHashMap<>();
        mm.put("id",-3);
        mm.put("center","区划小预算");
        mm.put("centerCode",-3);
        for (SysCostItem col : columns) {
            if (col.getLevel() == 1 && !col.getName().equals("合计")){

            }else{
                String key =  "-3_-3_" + col.getId();
                Object value = getBudget(costReportValueMap.get(key));
                if(col.getId() == -1){
                    RsCostReportEntity cityAllCountValue = rsCostReportDao.getAllCountyBudgetCountValue(queryData.getReportId(), queryData.getFromMonth(),
                            queryData.getToMonth(), topOrgCode, code, byTopOrgCodeAndParentCode.getCode());
                    if(cityAllCountValue != null && cityAllCountValue.getBudget() != null){
                        value = cityAllCountValue.getBudget();
                    }
                }
                String format = df.format(new BigDecimal(value.toString()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_UP));
                mm.put(col.getId(),format);
            }
        }
        return mm;
        //list.add(0,mm);
    }

    /**
     * 获取表头集合
     * @param reportId
     * @param topOrgCode
     * @return
     */
    public List<SysCostItem> getColumns(Integer reportId, Long topOrgCode) {
        QueryWrapper<SysCostItem> qw = new QueryWrapper<>();
        qw.select("*").eq("report_id",reportId).eq("org_code",topOrgCode)
                .eq("enable",1).orderByAsc("level,parent_id,seq");
        List<SysCostItem> itemList = sysCostItemMapper.selectList(qw);
        List<SysCostItem> result = buildTree(itemList);
        for (SysCostItem item : result) {
            if(item.getItemsChildren() != null){
                item.getItemsChildren().add(0,getSysCostItem(2,item.getId()));
            }
        }
        result.add(0,getSysCostItem(1, Long.valueOf(-1)));
        return result;
    }

    /**
     * 数据处理
     * @param o
     * @return
     */
    private Object getValue(RsCostReportEntity o) {
        return o == null ? 0 : o.getCurrMonthValue();
    }

    private Object getProgress(RsCostReportEntity o) {
        return o == null ? 0 : o.getProgress();
    }

    private Object getBudget(RsCostReportEntity o) {
        return o == null ? 0 : o.getBudget();
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
     * 获取map形式的数据
     * @param queryData
     * @param topOrgCode
     * @return
     */
    public Map<String,RsCostReportEntity> getValueMap(QueryData queryData,Long topOrgCode,Long code){
        List<RsCostReportEntity> result = new ArrayList<>();
        //获取费用数据
        List<RsCostReportEntity> cityValue = rsCostReportDao.getCountyValue(queryData.getReportId(),queryData.getFromMonth(),queryData.getToMonth(),topOrgCode,code);
        result.addAll(cityValue);
        //获取成本集合数据
        List<RsCostReportEntity> cityCountValue = rsCostReportDao.getCountyCountValue(queryData.getReportId(),queryData.getFromMonth(),queryData.getToMonth(),topOrgCode,code);
        result.addAll(cityCountValue);
        //获取成本
        List<RsCostReportEntity> cityAllCountValue = rsCostReportDao.getAllCountyCountValue(queryData.getReportId(),queryData.getFromMonth(),queryData.getToMonth(),topOrgCode,code);
        result.addAll(cityAllCountValue);

        Map<String,RsCostReportEntity> map = new HashMap<>();
        for (RsCostReportEntity rs : result) {
            map.put(rs.getOrgCode()+"_"+rs.getParentOrgCode()+"_"+rs.getCostItemId(),rs);
        }
        return map;
    }

    /**
     * description: 营服合计
     *
     * @param queryData
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.Map<java.lang.String,com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    public Map<String,RsCostReportEntity> getCostReportValueMap(QueryData queryData,Long topOrgCode,Long parentOrgCode,Long orgCode){
        List<RsCostReportEntity> result = new ArrayList<>();
        //获取费用数据
        List<RsCostReportEntity> allCountyCostReportCountValue = rsCostReportDao.getCountyCostReportValue(queryData.getReportId(), queryData.getFromMonth(),
                queryData.getToMonth(), topOrgCode, parentOrgCode, orgCode);
        result.addAll(allCountyCostReportCountValue);
        //获取成本集合数据
        List<RsCostReportEntity> cityCountValue = rsCostReportDao.getCountyCostReportCountValue(queryData.getReportId(), queryData.getFromMonth(),
                queryData.getToMonth(), topOrgCode, parentOrgCode, orgCode);
        result.addAll(cityCountValue);
        //获取成本
        /*List<RsCostReportEntity> cityAllCountValue = rsCostReportDao.getAllCountyCostReportCountValue(queryData.getReportId(), queryData.getFromMonth(),
                queryData.getToMonth(), topOrgCode, parentOrgCode, orgCode);
        result.addAll(cityAllCountValue);*/

        Map<String,RsCostReportEntity> map = new HashMap<>();
        for (RsCostReportEntity rs : result) {
            map.put(rs.getOrgCode()+"_"+rs.getParentOrgCode()+"_"+rs.getCostItemId(),rs);
        }
        return map;
    }

    /**
     * description: 区划小进度
     *
     * @param queryData
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.Map<java.lang.String,com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    public Map<String,RsCostReportEntity> getProgressValueMap(QueryData queryData,Long topOrgCode,Long parentOrgCode,Long orgCode){
        List<RsCostReportEntity> result = new ArrayList<>();
        //获取费用数据
        List<RsCostReportEntity> allCountyCostReportCountValue = rsCostReportDao.getCountyProgressValue(queryData.getReportId(), queryData.getFromMonth(),
                queryData.getToMonth(), topOrgCode, parentOrgCode, orgCode);
        result.addAll(allCountyCostReportCountValue);
        //获取成本集合数据
        List<RsCostReportEntity> cityCountValue = rsCostReportDao.getCountyProgressCountValue(queryData.getReportId(), queryData.getFromMonth(),
                queryData.getToMonth(), topOrgCode, parentOrgCode, orgCode);
        result.addAll(cityCountValue);
        Map<String,RsCostReportEntity> map = new HashMap<>();
        for (RsCostReportEntity rs : result) {
            map.put(rs.getOrgCode()+"_"+rs.getParentOrgCode()+"_"+rs.getCostItemId(),rs);
        }
        return map;
    }

    /**
     * description: 区划小预算
     *
     * @param queryData
     * @param topOrgCode
     * @param parentOrgCode
     * @param orgCode
     * @return java.util.Map<java.lang.String,com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity>
     */
    public Map<String,RsCostReportEntity> getBudgetValueMap(QueryData queryData,Long topOrgCode,Long parentOrgCode,Long orgCode){
        List<RsCostReportEntity> result = new ArrayList<>();
        //获取费用数据
        List<RsCostReportEntity> allCountyCostReportCountValue = rsCostReportDao.getCountyBudgetValue(queryData.getReportId(), queryData.getFromMonth(),
                queryData.getToMonth(), topOrgCode, parentOrgCode, orgCode);
        result.addAll(allCountyCostReportCountValue);
        //获取成本集合数据
        List<RsCostReportEntity> cityCountValue = rsCostReportDao.getCountyBudgetCountValue(queryData.getReportId(), queryData.getFromMonth(),
                queryData.getToMonth(), topOrgCode, parentOrgCode, orgCode);
        result.addAll(cityCountValue);
        Map<String,RsCostReportEntity> map = new HashMap<>();
        for (RsCostReportEntity rs : result) {
            map.put(rs.getOrgCode()+"_"+rs.getParentOrgCode()+"_"+rs.getCostItemId(),rs);
        }
        return map;
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
        return center;
    }



    @Override
    public List<SysOrganization> getDistrictOrganization(String level, Long parentCode) {
        List<SysOrganization> list = sysOrganizationMapper.getOrganization(level,parentCode);
        return list;
    }

    public List<SysOrganization> getList(String level, Long parentCode, Long reportId){
        SysUser user = ShiroUtils.getUser();
        List<SysOrganization> byTopOrgCodeAndOrgCodeList = sysOrganizationMapper.getByTopOrgCodeAndOrgCode(user.getTopOrgCode(), user.getOrgCode());
        List<SysOrganization> list = sysOrganizationMapper.getOrganization(level,parentCode);
        // 为营服角色
        if(byTopOrgCodeAndOrgCodeList.size() == 1 && byTopOrgCodeAndOrgCodeList.get(0) != null && byTopOrgCodeAndOrgCodeList.get(0).getLevel() == 4){
            SysOrganization byTopOrgCodeAndOrgCode = byTopOrgCodeAndOrgCodeList.get(0);
            boolean flag = false;
            for(SysOrganization sysOrganization : list){
                List<SysOrganization> tempList = new ArrayList<>();
                List<SysOrgAnizationCamp> templChildrens = new ArrayList<>();
                List<SysOrgAnizationCamp> childrens = sysOrganization.getChildrens();
                for(SysOrgAnizationCamp sysOrgAnizationCamp : childrens){
                    if(sysOrgAnizationCamp.getName().equals(byTopOrgCodeAndOrgCode.getName())){
                        templChildrens.add(sysOrgAnizationCamp);
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    sysOrganization.setChildrens(templChildrens);
                    tempList.add(sysOrganization);
                    list = tempList;
                    break;
                }
            }
        }
        // 为区角色
        if(byTopOrgCodeAndOrgCodeList.size() == 1 && byTopOrgCodeAndOrgCodeList.get(0) != null && byTopOrgCodeAndOrgCodeList.get(0).getLevel() == 3){
            SysOrganization byTopOrgCodeAndOrgCode = byTopOrgCodeAndOrgCodeList.get(0);
            List<SysOrganization> tempList = new ArrayList<>();
            for(SysOrganization sysOrganization : list){
                if(sysOrganization.getName().equals(byTopOrgCodeAndOrgCode.getName())){
                    tempList.add(sysOrganization);
                    break;
                }
            }
            list = tempList;
        }
        for(SysOrganization sysOrganization : list){

            List<SysOrgAnizationCamp> childrens = sysOrganization.getChildrens();
            int i = 4;
            // 获取月份
            for(SysOrgAnizationCamp sysOrgAnizationCamp : childrens){
                Long campCode = sysOrgAnizationCamp.getCode();
                sysOrgAnizationCamp.setId(i);
                i++;
                List<RsCostReportEntity> timeList = rsCostReportDao.getCostReportTime(campCode, reportId);
                sysOrgAnizationCamp.setTimeList(timeList);
            }
        }
        return list;
    }

    @Override
    public List<SysCostItem> getCountyItem(Integer reportId, Long orgCode){
        Integer year = DateUtils.getYear(DateUtils.getCurrentDate());
        List<SysCostItem> sysCostItems = sysCostItemMapper.getCountyItem(reportId,orgCode,year);
        for(SysCostItem sysCostItem : sysCostItems){
            sysCostItem.getId();

            //itemsChildrens
            List<RsCostReportEntity> itemsChildrens = sysCostItem.getItemsChildrens();
            //sysCostItem.setItemsChildrens();
        }
        return sysCostItems;
    }
}

package com.gpdi.operatingunit.service.costreport.impl;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.dao.costreport.ReportShowMapper;
import com.gpdi.operatingunit.entity.costreport.*;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.service.costreport.ReportShowService;
import com.gpdi.operatingunit.utils.ExcelUtils;
import com.gpdi.operatingunit.utils.StringToIntUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportShowImpl implements ReportShowService {
    @Override
    public R initYangJiangOrganizedData() {
        List<SysOrganization> list = reportShowMapper.getAllOrganized();
        Map<Integer, List<SysOrganization>> listMap = list.stream().filter(sys -> sys.getTopOrgCode() == 441400001L)
                .collect(Collectors.groupingBy(SysOrganization::getLevel, Collectors.toList()));
        listMap.get(3).forEach(p -> {
            List<SysOrganization> sonList = new ArrayList<>();
            listMap.get(4).forEach(s -> {
                if (p.getCode().equals(s.getParentCode())) {
                    sonList.add(s);
                }
                p.setChildren(sonList);
            });
        });
        List<SysOrganization> sysOrganizations = listMap.get(3);
        return R.ok(sysOrganizations);
    }

    @Override
    public R getCostBarChat(int code, int month) {
        List<ReportShow> list = new ArrayList<>();
        List<Map<Object, Object>> mapss = reportShowMapper.getAllCostItemsByOrganizedIdAndMonth(month, code);
        for (int i = 0; i < mapss.size(); i++) {
            ReportShow r = new ReportShow(1, null, 0);
            r.setName(mapss.get(i).get("name").toString());
            r.setId(Integer.parseInt(mapss.get(i).get("id").toString()));
            String oldValue = mapss.get(i).get("value").toString();
            r.setValue(stringToIntUtil.stringToInt(oldValue));
            list.add(r);
        }
        if (list.size() == 0) {
            return R.ok("没有数据，请查询其他月份");
        }
        if (list.size() != 16) {
            return R.ok("数据不全，请查询其他月份");
        }
        int badCostId = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("坏账费用")) {
                badCostId = list.get(i).getId();
                list.remove(list.get(i));
                i--;
            }
        }
        String oldBadValue = reportShowMapper.getBadCodeFromMonthAndOrganized(month, code);
        int badValue = stringToIntUtil.stringToInt(oldBadValue);

        list.add(new ReportShow(badCostId, "坏账费用", badValue));
        //小环（1、装移机材料），
        // 业务发展成本
        int sumServiceDevelopmentCost = 0;
        //日常运营成本
        int sumGeneralOperationCost = 0;

        //终端数据合并
        int finallyCost = 0;
        list.forEach(a -> {


        });
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("终端成本-itv")) {
                finallyCost = finallyCost + list.get(i).getValue();
                list.remove(list.get(i));
                i--;
            }
            if (list.get(i).getName().equals("终端成本-宽带")) {
                finallyCost = finallyCost + list.get(i).getValue();
                list.remove(list.get(i));
                i--;
            }
            if (list.get(i).getName().equals("终端成本-智能组网")) {
                finallyCost = finallyCost + list.get(i).getValue();
                list.remove(list.get(i));
                i--;
            }
        }

        list.add(new ReportShow(100, "终端成本", finallyCost));

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("装移机材料") || list.get(i).getName().equals("终端成本") || list.get(i).getName().equals("坏账费用") || list.get(i).getName().equals("渠道佣金")) {
                sumServiceDevelopmentCost += list.get(i).getValue();
            } else {
                sumGeneralOperationCost += list.get(i).getValue();
            }
        }
        String serviceName = reportShowMapper.getOrganizedServiceName(code);
        //当月收入
        String monthIncome = reportShowMapper.getMonthIncomeByMonthAndServiceName(month, serviceName);
        if (monthIncome == null) {
            monthIncome = "100";
        }
        Map map = new HashMap();
        map.put("montIncome", monthIncome);
        map.put("itemList", list);
        return R.ok(map);
    }

    @Override
    public R getCostLimit(String code, String month) {
        //营服中心名称
        String serviceName = reportShowMapper.getOranziedByCode(code);
        String year = month.substring(0, 4);
        String strMonth = month.substring(4, 6);
        String sql = "select * from cost_index where 1=1  ";
        sql += " and month>=" + year + "01";
        sql += " and month<=" + month;
        sql += " and dept='" + serviceName + "'";
        List<CostIndex> costIndexList = reportShowMapper.getCostIndexBySQL(sql);
        if (costIndexList.size() == 0) {
        } else {
            int intMonth = 0;
            if (strMonth.equals("12") || strMonth.equals("11") || strMonth.equals("10")) {
                intMonth = Integer.parseInt(strMonth);
            } else {
                intMonth = Integer.parseInt(month.substring(5, 6));
            }

            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < costIndexList.size(); i++) {
                int myMonth = 0;
                String resultMonth = costIndexList.get(i).getMonth().substring(4, 6);
                if (resultMonth.equals("12") || resultMonth.equals("11") || resultMonth.equals("10")) {
                    myMonth = Integer.parseInt(costIndexList.get(i).getMonth().substring(4, 6));
                } else {
                    myMonth = Integer.parseInt(costIndexList.get(i).getMonth().substring(5, 6));
                }
                map.put(myMonth, 1);
            }
            for (int i = 1; i <= intMonth; i++) {
                if (map.containsKey(i)) {

                } else {
                    if (costIndexList.size() > 0) {
                        CostIndex costIndex = costIndexList.get(0);
                        costIndex.setValue("0");
                        if (i <= 9) {
                            CostIndex costIndex1 = new CostIndex(null, null, null, null);
                            costIndex1.setMonth(year + "0" + i);
                            costIndex1.setCompany(costIndex.getCompany());
                            costIndex1.setYearlyBudget(costIndex.getYearlyBudget());
                            costIndex1.setValue("0");
                            costIndexList.add(costIndex1);
                        } else {
                            CostIndex costIndex1 = new CostIndex(null, null, null, null);
                            costIndex1.setCompany(costIndex.getCompany());
                            costIndex1.setYearlyBudget(costIndex.getYearlyBudget());
                            costIndex1.setMonth(year + "" + i + "");
                            costIndex1.setValue("0");
                            costIndexList.add(costIndex1);
                        }
                    }
                }
            }
            costIndexList.sort((CostIndex a1, CostIndex a2) -> Integer.parseInt(a1.getMonth()) - Integer.parseInt(a2.getMonth()));
        }

        for (int i = 0; i < costIndexList.size(); i++) {
            String value = costIndexList.get(i).getValue();
            String yearlyBudget = costIndexList.get(i).getYearlyBudget();
            costIndexList.get(i).setValue(stringToIntUtil.stringSubTwoDecimalPoint(value));
            costIndexList.get(i).setYearlyBudget(stringToIntUtil.stringSubTwoDecimalPoint(yearlyBudget));

        }

        // 处理折线图数据
        List<Map<Object, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < costIndexList.size(); i++) {

            Map<Object, Object> map = new HashMap<>();
            map.put("type", costIndexList.get(i).getMonth());
            map.put("money", Double.parseDouble(costIndexList.get(i).getValue()));
            resultList.add(map);
            if (i == costIndexList.size() - 1) {
                Map<Object, Object> surplus = new HashMap<>();
                surplus.put("type", "剩余余额");
                double z = Double.parseDouble(costIndexList.get(i).getYearlyBudget()) - Double.parseDouble(costIndexList.get(i).getValue());
                surplus.put("money", Double.parseDouble(stringToIntUtil.stringSubTwoDecimalPoint("" + z)));

                Map<Object, Object> all = new HashMap<>();
                all.put("type", "总额度");
                try {
                    double money = Double.parseDouble(costIndexList.get(i).getYearlyBudget());
                    all.put("money", money);
                    resultList.add(surplus);
                    resultList.add(all);
                } catch (Exception e) {
                }

            }
        }
        return R.ok(resultList);
    }

    @Override
    public R getCostTimeSchedule(String code, String month) {
        //营服中心名称
        String serviceName = reportShowMapper.getOranziedByCode(code);
        String year = month.substring(0, 4);
        String strMonth = month.substring(4, 6);
        String sql = "select * from cost_index where 1=1  ";
        sql += " and month>=" + year + "01";
        sql += " and month<=" + month;
        sql += " and dept='" + serviceName + "'";
        List<CostIndex> costIndexList = reportShowMapper.getCostIndexBySQL(sql);
        if (costIndexList.size() == 0) {

        } else {
            int intMonth = 0;
            if (strMonth.equals("12") || strMonth.equals("11") || strMonth.equals("10")) {
                intMonth = Integer.parseInt(strMonth);
            } else {
                intMonth = Integer.parseInt(month.substring(5, 6));
            }
            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < costIndexList.size(); i++) {
                int myMonth = 0;
                String resultMonth = costIndexList.get(i).getMonth().substring(4, 6);
                if (resultMonth.equals("12") || resultMonth.equals("11") || resultMonth.equals("10")) {
                    myMonth = Integer.parseInt(costIndexList.get(i).getMonth().substring(4, 6));
                } else {
                    myMonth = Integer.parseInt(costIndexList.get(i).getMonth().substring(5, 6));
                }
                map.put(myMonth, 1);
            }
            for (int i = 1; i <= intMonth; i++) {
                if (map.containsKey(i)) {

                } else {
                    if (costIndexList.size() > 0) {
                        CostIndex costIndex = costIndexList.get(0);
                        costIndex.setValue("0");
                        if (i <= 9) {
                            CostIndex costIndex1 = new CostIndex(null, null, null, null);
                            costIndex1.setMonth(year + "0" + i);
                            costIndex1.setCompany(costIndex.getCompany());
                            costIndex1.setYearlyBudget(costIndex.getYearlyBudget());
                            costIndex1.setValue("0");
                            costIndexList.add(costIndex1);
                        } else {
                            CostIndex costIndex1 = new CostIndex(null, null, null, null);
                            costIndex1.setCompany(costIndex.getCompany());
                            costIndex1.setYearlyBudget(costIndex.getYearlyBudget());
                            costIndex1.setMonth(year + "" + i + "");
                            costIndex1.setValue("0");
                            costIndexList.add(costIndex1);
                        }
                    }
                }
            }
            costIndexList.sort((CostIndex a1, CostIndex a2) -> Integer.parseInt(a1.getMonth()) - Integer.parseInt(a2.getMonth()));
        }
        List<CostIndex2> costIndex2List = new Vector<>();
        costIndexList.forEach(a -> {
            //预算执行率
            CostIndex2 costIndex2 = new CostIndex2();
            costIndex2.setId(a.getId());
            costIndex2.setCompany(a.getCompany());
            costIndex2.setDept(a.getDept());
            costIndex2.setMonth(a.getMonth());
            costIndex2.setValue(a.getValue());
            costIndex2.setName("预算执行率");
            try {
                if (a.getBudgetImplementationRate().isEmpty() || a.getBudgetImplementationRate().equals("null")) {
                    costIndex2.setNameValue("0.00");
                } else {
                    costIndex2.setNameValue(a.getBudgetImplementationRate());
                }

            } catch (Exception e) {
                costIndex2.setNameValue("0.00");
            }
            costIndex2.setYearlyBudget(a.getYearlyBudget());
            costIndex2List.add(costIndex2);
            //时间进度
            CostIndex2 costIndex3 = new CostIndex2();
            costIndex3.setId(a.getId());
            costIndex3.setDept(a.getDept());
            costIndex3.setCompany(a.getCompany());
            costIndex3.setMonth(a.getMonth());
            costIndex3.setValue(a.getValue());
            costIndex3.setName("时间进度");
            try {
                if (a.getBudgetImplementationRate().isEmpty() || a.getBudgetImplementationRate().equals("null")) {
                    costIndex3.setNameValue("0.00");
                } else {
                    costIndex3.setNameValue(a.getTimeSchedule());
                }
            } catch (Exception e) {
                costIndex3.setNameValue("0.00");
            }

            costIndex3.setYearlyBudget(a.getYearlyBudget());
            costIndex2List.add(costIndex3);


        });

        //转化百分数
        for (int i = 0; i < costIndex2List.size(); i++) {
            double value = Double.parseDouble(costIndex2List.get(i).getNameValue());
            try {
                NumberFormat percentInstance = NumberFormat.getPercentInstance();
                percentInstance.setMaximumFractionDigits(2); // 保留小数两位
                String format = percentInstance.format(value); // 结果是81.25% ，最后一们四舍五入了
                costIndex2List.get(i).setNameValue(format);
            } catch (Exception e) {
                //  e.printStackTrace();
            }

        }


        return R.ok(costIndex2List);
    }

    @Override
    public R getCostRingTable(int code, int month) {
        List<ReportShow> list = new ArrayList<>();
        //大环
        List<Map<Object, Object>> mapss = reportShowMapper.getAllCostItemsByOrganizedIdAndMonth(month, code);
        for (int i = 0; i < mapss.size(); i++) {
            ReportShow r = new ReportShow(1, null, 0);
            r.setName(mapss.get(i).get("name").toString());
            r.setId(Integer.parseInt(mapss.get(i).get("id").toString()));
            String oldValue = mapss.get(i).get("value").toString();
            r.setValue(stringToIntUtil.stringToInt(oldValue));
            list.add(r);
        }
        if (list.size() == 0) {
            return R.ok("没有数据，请查询其他月份");
        }
        if (list.size() != 16) {
            return R.ok("数据不全，请查询其他月份");
        }
        int badCostId = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue() == 0) {
                list.remove(list.get(i));
                i--;
            }
            if (list.get(i).getName().equals("坏账费用")) {
                badCostId = list.get(i).getId();
                list.remove(list.get(i));
                i--;
            }
        }
        String getBadValue = reportShowMapper.getBadCodeFromMonthAndOrganized(month, code);
        int badValue = stringToIntUtil.stringToInt(getBadValue);
        if (badValue != 0) {
            list.add(new ReportShow(badCostId, "坏账费用", badValue));
        }
        //小环（1、装移机材料），
        // 业务发展成本
        int sumServiceDevelopmentCost = 0;
        //日常运营成本
        int sumGeneralOperationCost = 0;
        //终端数据合并
        int finallyCost = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("终端成本-itv")) {
                finallyCost = finallyCost + list.get(i).getValue();
                list.remove(list.get(i));
                i--;
            }
            if (list.get(i).getName().equals("终端成本-宽带")) {
                finallyCost = finallyCost + list.get(i).getValue();
                list.remove(list.get(i));
                i--;
            }
            if (list.get(i).getName().equals("终端成本-智能组网")) {
                finallyCost = finallyCost + list.get(i).getValue();
                list.remove(list.get(i));
                i--;
            }
        }

        list.add(new ReportShow(100, "终端成本", finallyCost));

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("装移机材料") || list.get(i).getName().equals("终端成本") || list.get(i).getName().equals("坏账费用") || list.get(i).getName().equals("渠道佣金")) {
                sumServiceDevelopmentCost += list.get(i).getValue();
            } else {
                sumGeneralOperationCost += list.get(i).getValue();
            }
        }
        List<SmallRing> smallRinglist = new Vector<>();

        smallRinglist.add(new SmallRing("业务发展成本", sumServiceDevelopmentCost));

        smallRinglist.add(new SmallRing("日常运营成本", sumGeneralOperationCost));

        List<ReportShow> serviceList = new ArrayList<>();

        List<ReportShow> generalList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("装移机料费") || list.get(i).getName().equals("终端成本") || list.get(i).getName().equals("坏账费用") || list.get(i).getName().equals("渠道佣金及支撑费")) {
                serviceList.add(list.get(i));
            } else {
                generalList.add(list.get(i));
            }
        }
        for (int i = 0; i < smallRinglist.size(); i++) {
            if (smallRinglist.get(i).getName().equals("业务发展成本")) {
                smallRinglist.get(i).setList(serviceList);
            }
            if (smallRinglist.get(i).getName().equals("日常运营成本")) {
                smallRinglist.get(i).setList(generalList);
            }
        }
        Map map = new HashMap();
        //总的金额
        map.put("sumAll", sumServiceDevelopmentCost + sumGeneralOperationCost);
        map.put("outsideList", list);
        map.put("innerList", smallRinglist);
        List<Map<Object, Object>> resultMap = new ArrayList<>();
        smallRinglist.forEach(p -> {
            List<ReportShow> list1 = p.getList();
            list1.forEach(s -> {
                Map<Object, Object> map1 = new HashMap<>();
                map1.put("type", p.getName());
                map1.put("value", s.getValue());
                map1.put("name", s.getName());
                resultMap.add(map1);
            });
        });

        return R.ok(resultMap);
    }

    @Override
    public R importBadCostList(InputStream inputStream) {

        List<Object> objectList = ExcelUtils.readLessThan1000RowBySheetAndStream(inputStream, null);
        if (objectList.size() <= 0) {
            return R.error("数据为空");
        }
        List<BadCost> listEntityList = new ArrayList<>();
        for (int i = 1; i < objectList.size(); i++) {
            BadCost d = new BadCost();
            List list = (List) objectList.get(i);
            if (list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    if (j == 0) {
                        try {
                            d.setMonth(list.get(j).toString());
                        } catch (Exception e) {

                        }

                    }
                    if (j == 1) {
                        try {
                            d.setDistrict(list.get(j).toString());
                        } catch (Exception e) {

                        }

                    }
                    if (j == 2) {
                        try {
                            d.setServiceCenter(list.get(j).toString());
                        } catch (Exception e) {

                        }

                    }
                    if (j == 3) {
                        try {
                            d.setType(list.get(j).toString());

                        } catch (Exception e) {

                        }
                    }
                    if (j == 4) {
                        try {
                            d.setValue(list.get(j).toString());

                        } catch (Exception e) {

                        }

                    }
                    if (j == 4) {
                        listEntityList.add(d);
                    }

                }
            }

        }
        if (listEntityList.size() > 0) {
            reportShowMapper.addBadCostlList(listEntityList);

        }
        return R.ok("上传数据成功","1");
    }

    @Override
    public R importCostIndexList(InputStream inputStream) {

        List<Object> objectList = ExcelUtils.readLessThan1000RowBySheetAndStream(inputStream, null);
        if (objectList.size() <= 0) {
            return R.error("数据为空");
        }
        List<CostIndex> listEntityList = new ArrayList<>();
        for (int i = 1; i < objectList.size(); i++) {
            CostIndex d = new CostIndex();
            List list = (List) objectList.get(i);
            if (list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    if (j == 0) {
                        try {
                            d.setCompany(list.get(j).toString());
                        } catch (Exception e) {

                        }

                    }
                    if (j == 1) {
                        try {
                            d.setDept(list.get(j).toString());
                        } catch (Exception e) {

                        }

                    }
                    if (j == 2) {
                        try {
                            d.setYearlyBudget(list.get(j).toString());
                        } catch (Exception e) {

                        }

                    }
                    if (j == 3) {
                        try {
                            d.setBudgetImplementationRate(list.get(j).toString());
                        } catch (Exception e) {

                        }

                    }
                    if (j == 4) {

                            try {
                                d.setTimeSchedule(list.get(j).toString());
                            } catch (Exception e) {

                            }



                    }
                    if (j == 5) {
                        try {
                            d.setProgressIsPoor(list.get(j).toString());
                        } catch (Exception e) {

                        }

                    }
                    if (j == 6) {
                        try {
                            d.setValue(list.get(j).toString());
                        } catch (Exception e) {

                        }
                    }
                    if (j == 7) {
                        try {
                            d.setMonth(list.get(j).toString());
                        } catch (Exception e) {

                        }
                    }
                    if (j == 7) {
                        listEntityList.add(d);
                    }
                }
            }
        }
        if (listEntityList.size() > 0) {
            reportShowMapper.addCostIndexlList(listEntityList);
        }
        return R.ok("上传成本进度数据成功","1");
    }

    @Override
    public R importCostIncomeList(InputStream inputStream) {
        List<Object> objectList = ExcelUtils.readLessThan1000RowBySheetAndStream(inputStream, null);
        if (objectList.size() <= 0) {
            return R.error("数据为空");
        }
        List<MonthIncome> listEntityList = new ArrayList<>();
        for (int i = 1; i < objectList.size(); i++) {
            MonthIncome d = new MonthIncome();
            List list = (List) objectList.get(i);
            if (list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    if (j == 0) {
                        d.setMonth(list.get(j).toString());
                    }
                    if (j == 1) {
                        d.setCompany(list.get(j).toString());
                    }
                    if (j == 2) {
                        d.setUnit(list.get(j).toString());
                    }
                    if (j == 3) {
                        d.setDistrict(list.get(j).toString());
                    }
                    if (j == 4) {
                        d.setServiceCenter(list.get(j).toString());
                    }
                    if (j == 5) {
                        d.setValue(Integer.parseInt(list.get(j).toString().substring(0, list.get(j).toString().lastIndexOf("."))));
                    }
                    if (j == 5) {
                        listEntityList.add(d);
                    }
                }
            }

        }
        //对数据进行去重操作
        //if(listEntityList.get(0))

        if (listEntityList.size() > 0) {

            reportShowMapper.addCostIncomeList(listEntityList);
        }
        return R.ok("上传成本进度数据成功","1");
    }


    @Autowired
    private ReportShowMapper reportShowMapper;

    StringToIntUtil stringToIntUtil = new StringToIntUtil();
}

package com.gpdi.operatingunit.service.parallel;

import com.gpdi.operatingunit.dao.reportconfig.SysCostItemMapper;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.utils.SpringTool;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @Author：long
 * @Date：2019/11/21 10:04
 * @Description：
 */
public class RsCostReportCallable implements Callable {

    private static SysCostItemMapper sysCostItemMapper = (SysCostItemMapper) SpringTool.getBean("sysCostItemMapper");

    private Long code;

    private int k;

    private List<SysCostItem> list;

    private Map<String,Integer> map;

    private List<SysOrganization> organization;
    private List<SysCostItem> lists;
    private List<SysCostItem> listy;
    private Map<String,Double> dMap;

    private Long reportId;
    private Long month;
    private Long endMonth;

    public RsCostReportCallable(Long code, List<SysCostItem> list, Map<String,Integer> map,
                                List<SysOrganization> organization,int k,Long reportId,Long month,Long endMonth,
                                List<SysCostItem> lists,List<SysCostItem> listy,Map<String,Double> dMap){
        this.code = code;
        this.list = list;
        this.map = map;
        this.organization = organization;
        this.lists = lists;
        this.listy = listy;
        this.reportId = reportId;
        this.month = month;
        this.endMonth = endMonth;
        this.k = k;
        this.dMap = dMap;
    }

    @Override
    public Object call() throws Exception {
        Double countyMonth = dMap.get("countyMonth");
        Double countyMonthy = dMap.get("countyMonthy");
        DecimalFormat df = new DecimalFormat("0.00");
        String name = "";
        for(int i = 0;i<list.size();i++) {
            SysCostItem sysCostItem = list.get(i);
            sysCostItem.getName();
            sysCostItem.getCname();
            if("合计".equals(sysCostItem.getCname())){
                map.put(sysCostItem.getName(),i);
            }
            List<RsCostReportEntity> itemsChildrenst = sysCostItem.getItemsChildrens();
            if(itemsChildrenst == null){
                itemsChildrenst = new ArrayList<>();
                RsCostReportEntity rsCostReportEntity = new RsCostReportEntity();
                itemsChildrenst.add(rsCostReportEntity);
                sysCostItem.setItemsChildrens(itemsChildrenst);
            }
            for (RsCostReportEntity rsCostReportEntity : itemsChildrenst) {
                // 预算
                rsCostReportEntity.setBudget("0.00");
                // 进度
                rsCostReportEntity.setProgress("0.00");
                // 本月数
                rsCostReportEntity.setCurrMonthValue("0.00");
            }
        }
        if(k < 4){
            double budget = 0.0;
            double progress = 0.0;
            //double monthqu = 0.0;

            //double monthqus = 0.0;
            double budgetss = 0.0;
            double progresss = 0.0;

            for(int i = 0;i<list.size();i++){
                SysCostItem sysCostItem = list.get(i);
                if (i == 0) {
                    name = list.get(i).getName();
                }

                if (name.equals(list.get(i).getName())) {
                    List<SysOrganization> list1 = organization.subList(4, organization.size());
                    // 数据
                    RsCostReportEntity budgetOrProgress = sysCostItemMapper.getBudgetOrProgress(reportId, list1, month, endMonth, sysCostItem.getId());
                    System.out.println(budgetOrProgress);
                    if(budgetOrProgress != null){
                        List<RsCostReportEntity> itemsChildrens = sysCostItem.getItemsChildrens();
                        for (RsCostReportEntity rsCostReportEntity : itemsChildrens){
                            // 预算
                            rsCostReportEntity.setBudget(budgetOrProgress.getBudget());
                            String budgets = df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getBudget()) / 10000));
                            budget += Double.parseDouble(budgets);
                            // 进度
                            rsCostReportEntity.setProgress(budgetOrProgress.getProgress());
                            if (!rsCostReportEntity.getProgress().equals("--")) {
                                progress += Double.parseDouble(df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getProgress()) / 10000)));
                            }

                            rsCostReportEntity.setCurrMonthValue(budgetOrProgress.getCurrMonthValue());
                            String format = df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrMonthValue())));
                            //monthqu += Double.parseDouble(format);
                        }
                    }
                    i++;
                }
                else {
                    Integer index = map.get(name);
                    // 不相等说明到了另一个类里
                    name = list.get(i).getName();
                    // 设置所属分类

                    RsCostReportEntity rsCostReportEntity = new RsCostReportEntity();
                    rsCostReportEntity.setBudget(new DecimalFormat("0.00").format(new BigDecimal(budget)));
                    budgetss += budget;
                    budget = 0.0;

                    rsCostReportEntity.setProgress(new DecimalFormat("0.00").format(new BigDecimal(progress)));
                    progresss += progress;
                    progress = 0.0;

                    List<RsCostReportEntity> costItemDatas = new ArrayList<>();
                    costItemDatas.add(rsCostReportEntity);
                    list.get(index).setItemsChildrens(costItemDatas);
                }
                if (list.size() == i) {
                    // 进入说明到了最后一个类里，对最后一个合计进行赋值
                    Integer index = map.get(name);
                    RsCostReportEntity rsCostReportEntity = new RsCostReportEntity();
                    rsCostReportEntity.setBudget(new DecimalFormat("0.00").format(new BigDecimal(budget)));
                    rsCostReportEntity.setProgress(new DecimalFormat("0.00").format(new BigDecimal(progress)));
                    List<RsCostReportEntity> costItemDatas = new ArrayList<>();
                    costItemDatas.add(rsCostReportEntity);
                    list.get(index).setItemsChildrens(costItemDatas);
                    budgetss += budget;
                    progresss += progress;
                    budget = 0.0;
                    progress = 0.0;
                    //monthqu = 0.0;
                }
            }
            organization.get(k).setBudget(new DecimalFormat("0.00").format(new BigDecimal(budgetss)));
            organization.get(k).setProgress(new DecimalFormat("0.00").format(new BigDecimal(progresss)));
        }
        else {
            double currMonthsorg = 0.0;

            double currMonths = 0.0;
            double budgets = 0.0;
            //list = getCostItem(reportId,orgCode);
            for (int i = 0; i < list.size(); ) {
                if (i == 0) {
                    name = list.get(i).getName();
                }
                if (name.equals(list.get(i).getName())) {
                    SysCostItem sysCostItem = list.get(i);
                    // 数据
                    List<RsCostReportEntity> costItemDatas = sysCostItemMapper.getCostItemDatas(reportId, code, month, endMonth, sysCostItem.getId());
                    double currMonth = 0.0;
                    double budget = 0.0;
                    double progress = 0.0;
                    // 遍历数据


                    for (RsCostReportEntity rsCostReportEntity : costItemDatas) {
                        //本月数
                        String currMonthValues = df.format(new BigDecimal(Double.parseDouble(rsCostReportEntity.getCurrMonthValue()) / 10000));
                        double currMonthValue = Double.parseDouble(currMonthValues);
                        currMonth += currMonthValue;
                    }
                    // 本月数
                    RsCostReportEntity rsCostReportEntity = new RsCostReportEntity();
                    currMonths += currMonth;

                    rsCostReportEntity.setCurrMonthValue(new DecimalFormat("0.00").format(new BigDecimal(currMonth)));

                    // 预算值
                    rsCostReportEntity.setBudget(budget + "");
                    budgets += budget;
                    // 进度
                    rsCostReportEntity.setProgress(progress + "%");
                    costItemDatas.clear();
                    costItemDatas.add(rsCostReportEntity);
                    //sysCostItem.setCurrMonthValue(currMonth+"");
                    sysCostItem.setItemsChildrens(costItemDatas);
                    i++;
                }
                else {
                    Integer index = map.get(name);
                    // 每个类的总合计（本月数）
                    list.get(index).setCurrMonthValue(new DecimalFormat("0.00").format(new BigDecimal(currMonths)));

                    // 不相等说明到了另一个类里
                    name = list.get(i).getName();
                    // 设置所属分类
                    //SysCostItem sysCostItem = new SysCostItem();
                    currMonthsorg += currMonths;
                    // 本月数
                    RsCostReportEntity rsCostReportEntity = new RsCostReportEntity();
                    rsCostReportEntity.setCurrMonthValue(new DecimalFormat("0.00").format(new BigDecimal(currMonths)));
                    List<RsCostReportEntity> costItemDatas = new ArrayList<>();
                    costItemDatas.add(rsCostReportEntity);
                    list.get(index).setItemsChildrens(costItemDatas);
                    currMonths = 0;
                }
                if (list.size() == i) {
                    // 进入说明到了最后一个类里，对最后一个合计进行赋值
                    Integer index = map.get(name);
                    currMonthsorg += currMonths;
                    // 每个类的总合计（本月数）

                    RsCostReportEntity rsCostReportEntity = new RsCostReportEntity();
                    rsCostReportEntity.setCurrMonthValue(new DecimalFormat("0.00").format(new BigDecimal(currMonths)));



                    List<RsCostReportEntity> costItemDatas = new ArrayList<>();
                    costItemDatas.add(rsCostReportEntity);
                    list.get(index).setItemsChildrens(costItemDatas);
                    //list.get(index).setCurrMonthValue(new DecimalFormat("0.00").format(new BigDecimal(currMonths)));
                    currMonths = 0;
                }
            }
            countyMonth += currMonthsorg;
            if(k != 14){
                countyMonthy += currMonthsorg;
            }
            organization.get(k).setCurrMonthValue(new DecimalFormat("0.00").format(new BigDecimal(currMonthsorg)));
        }
        organization.get(k).setItemsChildren(list);
        if(k >= 4){
            for(int j = 0;j<list.size();j++){
                String listName = list.get(j).getName();
                String listCname = list.get(j).getCname();
                for(int l = 0;l < lists.size();l++){
                    String sysName = lists.get(l).getName();
                    String sysCname = lists.get(l).getCname();
                    double v = 0.0;
                    /*if(listCname.equals("合计") && listName.equals("市自选划小成本")){*/
                    if(listName.equals(sysName) && listCname.equals(sysCname)){
                        if(lists.get(l).getItemsChildrens() == null){
                            List<RsCostReportEntity> itemsChildrens = new ArrayList<>();
                            RsCostReportEntity rsCostReportEntity = new RsCostReportEntity();
                            rsCostReportEntity.setCurrMonthValue("0.0");
                            itemsChildrens.add(rsCostReportEntity);
                            lists.get(l).setItemsChildrens(itemsChildrens);

                        }
                        if(lists.get(l).getItemsChildrens().get(0).getCurrMonthValue() != null){
                            String currMonthValue = lists.get(l).getItemsChildrens().get(0).getCurrMonthValue();
                            if("".equals(currMonthValue)){
                                currMonthValue = "0.0";
                            }
                            v = Double.parseDouble(df.format(new BigDecimal(currMonthValue)));
                        }
                        String temp = "";

                        double v2 = 0.0;
                        if(list.get(j).getItemsChildrens().get(0).getCurrMonthValue() != null ){
                            double v1 = Double.parseDouble(list.get(j).getItemsChildrens().get(0).getCurrMonthValue());
                            v2 = v;
                            temp = new DecimalFormat("0.00").format(new BigDecimal(v += v1));
                        }
                        if(lists.get(l).getItemsChildrens() != null){
                            lists.get(l).getItemsChildrens().get(0).setCurrMonthValue(temp);
                            if(k != 14) {
                                listy.get(l).getItemsChildrens().get(0).setCurrMonthValue(temp);
                            }
                        }
                        break;
                    }

                }
            }
        }
        dMap.put("countyMonth",countyMonth);
        dMap.put("countyMonthy",countyMonthy);
        return null;
    }
}

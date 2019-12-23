package com.gpdi.operatingunit.entity.costreport;

import lombok.Data;

/**
 * @desc:绩效考核
 */
@Data
public class Kpi {
    // 主键
    private int id;

    //区县名称
    private String district;

    //营服名称
    private String serviceName;

    //认领收入指标
    private String incomeIndicators;

    // 年份
    private String year;

    //年份增长率
    private String rateIncrease;

    //存量人工成本
    private String stockLaborCost;

    // 扣减存量成本
    private String stockCostDeducted;

    //增加成本
    private String addCost;

   //认领后人工基础成本
    private String laborBaseCostAfterClaim;

    // 核心产能累计实施
    private String cumulativeImplementation;

    //营服累计已实发
    private String cumulativeDistribution;

    //发放进度
    private String distributionProgress;

    //上限发放进度
    private String maxDistributionProgress;

    // 按上限空间资金金额
    private String spaceAmount;

    //按当前进度可清算金额
    private String settlementAmount;

    //累计发放小CEO资金金额
    private String ceoAmount;

    //累计应发存量成本
    private String stockCostPayable;

    //累计应发增量成本
    private String increasedCosts;

    //累计营服专项奖
    private String incrementalAward;

    //累计装维绩效奖
    private String maintenancePerformanceAward;

    //应发合计
    private String totalPay;

}

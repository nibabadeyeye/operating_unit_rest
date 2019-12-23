package com.gpdi.operatingunit.entity.home;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author Zhb
 * @date 2019-12-15 21:36:36
 */
@TableName("rs_home_number_data")
public class RsHomeNumberData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Integer id;
    /**
     * 顶级组织编码
     */
    private Long topOrgCode;
    /**
     * 组织编码
     */
    private Long orgCode;
    /**
     * 父级组织编码
     */
    private Long parentCode;
    /**
     * 组织编码路径
     */
    private String fullOrgCode;
    /**
     * 父级组织名称
     */
    private String parentOrgName;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 月份
     */
    private Integer month;
    /**
     * 收入完成值/收多少
     */
    private String incomeValue;
    /**
     * 收入预算
     */
    private String incomeBudget;
    /**
     * 收入进度
     */
    private String incomeProgress;
    /**
     * 成本完成值/花多少
     */
    private String costValue;
    /**
     * 成本预算
     */
    private String costBudget;
    /**
     * 成本进度
     */
    private String costProgress;
    /**
     * 利润/赚多少
     */
    private String profits;
    /**
     * 毛利率
     */
    private String profitsProgress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTopOrgCode() {
        return topOrgCode;
    }

    public void setTopOrgCode(Long topOrgCode) {
        this.topOrgCode = topOrgCode;
    }

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }

    public Long getParentCode() {
        return parentCode;
    }

    public void setParentCode(Long parentCode) {
        this.parentCode = parentCode;
    }

    public String getFullOrgCode() {
        return fullOrgCode;
    }

    public void setFullOrgCode(String fullOrgCode) {
        this.fullOrgCode = fullOrgCode;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getIncomeValue() {
        return incomeValue;
    }

    public void setIncomeValue(String incomeValue) {
        this.incomeValue = incomeValue;
    }

    public String getIncomeBudget() {
        return incomeBudget;
    }

    public void setIncomeBudget(String incomeBudget) {
        this.incomeBudget = incomeBudget;
    }

    public String getIncomeProgress() {
        return incomeProgress;
    }

    public void setIncomeProgress(String incomeProgress) {
        this.incomeProgress = incomeProgress;
    }

    public String getCostValue() {
        return costValue;
    }

    public void setCostValue(String costValue) {
        this.costValue = costValue;
    }

    public String getCostBudget() {
        return costBudget;
    }

    public void setCostBudget(String costBudget) {
        this.costBudget = costBudget;
    }

    public String getCostProgress() {
        return costProgress;
    }

    public void setCostProgress(String costProgress) {
        this.costProgress = costProgress;
    }

    public String getProfits() {
        return profits;
    }

    public void setProfits(String profits) {
        this.profits = profits;
    }

    public String getProfitsProgress() {
        return profitsProgress;
    }

    public void setProfitsProgress(String profitsProgress) {
        this.profitsProgress = profitsProgress;
    }
}

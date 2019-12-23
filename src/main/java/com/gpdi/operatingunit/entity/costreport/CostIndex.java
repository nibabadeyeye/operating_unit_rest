package com.gpdi.operatingunit.entity.costreport;
/**
 *
 * @desc: 成本报表
 *
 */
public class CostIndex {

    public CostIndex() {
    }

    public CostIndex(String company, String month, String yearlyBudget, String value) {
        this.company = company;
        this.month = month;
        this.yearlyBudget = yearlyBudget;
        this.value = value;
    }

    private int id;
    private String company;
    private String dept;
    private String month;
    private String yearlyBudget;
    private String budgetImplementationRate;
    private String timeSchedule;
    private String progressIsPoor;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYearlyBudget() {
        return yearlyBudget;
    }

    public void setYearlyBudget(String yearlyBudget) {
        this.yearlyBudget = yearlyBudget;
    }

    public String getBudgetImplementationRate() {
        return budgetImplementationRate;
    }

    public void setBudgetImplementationRate(String budgetImplementationRate) {
        this.budgetImplementationRate = budgetImplementationRate;
    }

    public String getTimeSchedule() {
        return timeSchedule;
    }

    public void setTimeSchedule(String timeSchedule) {
        this.timeSchedule = timeSchedule;
    }

    public String getProgressIsPoor() {
        return progressIsPoor;
    }

    public void setProgressIsPoor(String progressIsPoor) {
        this.progressIsPoor = progressIsPoor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CostIndex(int id, String company, String dept, String month, String yearlyBudget, String budgetImplementationRate, String timeSchedule, String progressIsPoor, String value) {
        this.id = id;
        this.company = company;
        this.dept = dept;
        this.month = month;
        this.yearlyBudget = yearlyBudget;
        this.budgetImplementationRate = budgetImplementationRate;
        this.timeSchedule = timeSchedule;
        this.progressIsPoor = progressIsPoor;
        this.value = value;
    }
}

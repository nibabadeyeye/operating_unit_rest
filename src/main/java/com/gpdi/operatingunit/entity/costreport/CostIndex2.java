package com.gpdi.operatingunit.entity.costreport;
/**
 *
 * @desc: 成本报表
 *
 */
public class CostIndex2 {


    private int id;
    private String company;
    private String dept;
    private String month;
    private String yearlyBudget;
  //  private String budgetImplementationRate;
  //  private String timeSchedule;
    private String name;

    private String nameValue;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CostIndex2() {
    }

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }

    public CostIndex2(int id, String company, String dept, String month, String yearlyBudget, String name, String nameValue, String progressIsPoor, String value) {
        this.id = id;
        this.company = company;
        this.dept = dept;
        this.month = month;
        this.yearlyBudget = yearlyBudget;
        this.name = name;
        this.nameValue = nameValue;
        this.progressIsPoor = progressIsPoor;
        this.value = value;
    }
}

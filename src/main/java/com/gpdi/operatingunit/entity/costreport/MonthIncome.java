package com.gpdi.operatingunit.entity.costreport;
/**
 *
 * @desc: 当月收入表
 */
public class MonthIncome {

    private int id;
    private String month;
    private String company;
    private String unit;
    private String district;
    private String serviceCenter;
    private int value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public MonthIncome(int id, String month, String company, String unit, String district, String serviceCenter, int value) {
        this.id = id;
        this.month = month;
        this.company = company;
        this.unit = unit;
        this.district = district;
        this.serviceCenter = serviceCenter;
        this.value = value;
    }

    public MonthIncome() {
    }
}

package com.gpdi.operatingunit.entity.costreport;
/**
 * @desc:坏账记录
 *
 */
public class BadCost {
    //月份
    private String month;
    //区公司
    private String district;
    //营服
    private String serviceCenter;
    //项目名称
    private String type;
    //金额
    private String value;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

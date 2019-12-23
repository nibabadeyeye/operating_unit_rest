package com.gpdi.operatingunit.entity.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@TableName("data_developmet")
public class DataDevelopment {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 月份
     */
    private Integer month;
    /**
     * 区公司
     */
    private String company;
    /**
     * 一次划小单元
     */
    private String firstHxUnit;
    /**
     * 区
     */
    private String district;
    /**
     * 营服
     */
    private String serviceCenter;
    /**
     * 移动发展量上年同期
     */
    private String mobileLast;
    /**
     * 移动发展量
     */
    private String mobile;
    /**
     * 宽带发展量上年同期
     */
    private String bandbroadLast;
    /**
     * 宽带发展量
     */
    private String bandbroad;
    /**
     * itv发展量上年同期
     */
    private String itvLast;
    /**
     * itv发展量
     */
    private String itv;
    /**
     * 移动出账上年同期
     */
    private String mobileCzLast;
    /**
     * 移动出账
     */
    private String mobileCz;
    /**
     * 宽带出账上年同期
     */
    private String bandbroadCzLast;
    /**
     * 宽带出账
     */
    private String bandbroadCz;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFirstHxUnit() {
        return firstHxUnit;
    }

    public void setFirstHxUnit(String firstHxUnit) {
        this.firstHxUnit = firstHxUnit;
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

    public String getMobileLast() {
        return mobileLast;
    }

    public void setMobileLast(String mobileLast) {
        this.mobileLast = mobileLast;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBandbroadLast() {
        return bandbroadLast;
    }

    public void setBandbroadLast(String bandbroadLast) {
        this.bandbroadLast = bandbroadLast;
    }

    public String getBandbroad() {
        return bandbroad;
    }

    public void setBandbroad(String bandbroad) {
        this.bandbroad = bandbroad;
    }

    public String getItvLast() {
        return itvLast;
    }

    public void setItvLast(String itvLast) {
        this.itvLast = itvLast;
    }

    public String getItv() {
        return itv;
    }

    public void setItv(String itv) {
        this.itv = itv;
    }

    public String getMobileCzLast() {
        return mobileCzLast;
    }

    public void setMobileCzLast(String mobileCzLast) {
        this.mobileCzLast = mobileCzLast;
    }

    public String getMobileCz() {
        return mobileCz;
    }

    public void setMobileCz(String mobileCz) {
        this.mobileCz = mobileCz;
    }

    public String getBandbroadCzLast() {
        return bandbroadCzLast;
    }

    public void setBandbroadCzLast(String bandbroadCzLast) {
        this.bandbroadCzLast = bandbroadCzLast;
    }

    public String getBandbroadCz() {
        return bandbroadCz;
    }

    public void setBandbroadCz(String bandbroadCz) {
        this.bandbroadCz = bandbroadCz;
    }
}

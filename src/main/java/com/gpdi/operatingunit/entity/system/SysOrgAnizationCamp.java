package com.gpdi.operatingunit.entity.system;

import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author：long
 * @Date：2019/10/30 11:22
 * @Description：区
 */
public class SysOrgAnizationCamp implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Long code;
    private String name;
    private String cname;
    private String alias;
    private String currMonthValue;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrMonthValue() {
        return currMonthValue;
    }

    public void setCurrMonthValue(String currMonthValue) {
        this.currMonthValue = currMonthValue;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    private List<SysCostItem> itemsChildren;

    private List<RsCostReportEntity> timeList;

    public List<SysCostItem> getItemsChildren() {
        return itemsChildren;
    }

    public void setItemsChildren(List<SysCostItem> itemsChildren) {
        this.itemsChildren = itemsChildren;
    }

    public List<RsCostReportEntity> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<RsCostReportEntity> timeList) {
        this.timeList = timeList;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.gpdi.operatingunit.entity.costreport;

import java.util.List;

/**
 *
 * 图形报表小环数据
 *
 */
public class SmallRing {
    //名称
    private String name;
    //数值
    private double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public SmallRing(String name, double value) {
        this.name = name;
        this.value = value;
    }

    private List<ReportShow> list;

    public List<ReportShow> getList() {
        return list;
    }

    public void setList(List<ReportShow> list) {
        this.list = list;
    }

    public SmallRing(String name, double value, List<ReportShow> list) {
        this.name = name;
        this.value = value;
        this.list = list;
    }
}

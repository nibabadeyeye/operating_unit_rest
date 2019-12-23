package com.gpdi.operatingunit.entity.costreport;

public class ReportShow {
    //成本项id
    private int id;
    //成本项名称
    private String name;
    //成本项金额
    private int value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ReportShow(int id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}

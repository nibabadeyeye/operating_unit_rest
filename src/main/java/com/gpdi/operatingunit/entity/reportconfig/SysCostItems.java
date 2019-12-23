package com.gpdi.operatingunit.entity.reportconfig;

/**
 * @Author：long
 * @Date：2019/11/1 15:00
 * @Description：
 */
public class SysCostItems {

    private Long id;
    private String name;
    private String cname;
    private int seq;
    private Long org_code;
    private Long report_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Long getOrg_code() {
        return org_code;
    }

    public void setOrg_code(Long org_code) {
        this.org_code = org_code;
    }

    public Long getReport_id() {
        return report_id;
    }

    public void setReport_id(Long report_id) {
        this.report_id = report_id;
    }
}

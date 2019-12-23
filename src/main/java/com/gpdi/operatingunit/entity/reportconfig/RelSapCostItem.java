package com.gpdi.operatingunit.entity.reportconfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gpdi.operatingunit.entity.system.RelSapCostItemSubitem;
import com.gpdi.operatingunit.entity.system.SysSapSubject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: SAP科目与成本项目关系表
 * @author: Lxq
 * @date: 2019/10/25 16:48
 */
@TableName("rel_sap_cost_item")
public class RelSapCostItem implements Serializable {

    /**
     * ID 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 组织编码
     */
    private Long orgCode;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 报表Id
     */
    private Integer reportId;
    /**
     * SAP科目
     */
    private Long sapCode;
    /**
     * 成本项目id
     */
    private Long costItemId;
    /**
     * 成本项目名称
     */
    private String costItem;
    /**
     * 处理描述
     */
    private String handlingDesc;
    /**
     * 操作
     */
    private String operation;
    /**
     * 是否有效：1是、0否
     */
    private Integer enable;
    /**
     * 创建人id
     */
    private Integer createOperId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人id
     */
    private Integer updateOperId;
    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private Long sid;

    @TableField(exist = false)
    private List<RelSapCostItemSubitem> subitemList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Long getSapCode() {
        return sapCode;
    }

    public void setSapCode(Long sapCode) {
        this.sapCode = sapCode;
    }

    public Long getCostItemId() {
        return costItemId;
    }

    public void setCostItemId(Long costItemId) {
        this.costItemId = costItemId;
    }

    public String getCostItem() {
        return costItem;
    }

    public void setCostItem(String costItem) {
        this.costItem = costItem;
    }

    public String getHandlingDesc() {
        return handlingDesc;
    }

    public void setHandlingDesc(String handlingDesc) {
        this.handlingDesc = handlingDesc;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getCreateOperId() {
        return createOperId;
    }

    public void setCreateOperId(Integer createOperId) {
        this.createOperId = createOperId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateOperId() {
        return updateOperId;
    }

    public void setUpdateOperId(Integer updateOperId) {
        this.updateOperId = updateOperId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public List<RelSapCostItemSubitem> getSubitemList() {
        return subitemList;
    }

    public void setSubitemList(List<RelSapCostItemSubitem> subitemList) {
        this.subitemList = subitemList;
    }
}

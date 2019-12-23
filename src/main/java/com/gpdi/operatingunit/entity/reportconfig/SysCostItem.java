package com.gpdi.operatingunit.entity.reportconfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gpdi.operatingunit.entity.system.SysSapSubject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 成本项目表
 * @author: Lxq
 * @date: 2019/10/25 14:48
 */
@TableName("sys_cost_item")
public class SysCostItem implements Serializable {

    /**
     * ID 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 成本项目名称
     */
    private String name;
    /**
     * 成本项目id路径
     */
    private String fullId;
    /**
     * 成本项目名称路径
     */
    private String fullName;
    /**
     * 组织编码
     */
    private Long orgCode;
    /**
     * 报表id
     */
    private Integer reportId;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 排序
     */
    private Integer seq;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 数据来源
     */
    private Integer dataFrom;
    /**
     * 数据来源描述
     */
    private String dataFromDesc;
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

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private String costType;
    @TableField(exist = false)
    private Integer length;
    @TableField(exist = false)
    private Integer handlingDesc;
    @TableField(exist = false)
    private Integer count;
    @TableField(exist = false)
    private String sapCodeAndName;
    @TableField(exist = false)
    private String cname;
    @TableField(exist = false)
    private String currMonthValue;
    @TableField(exist = false)
    private List<RsCostReportEntity> itemsChildrens;
    @TableField(exist = false)
    private List<SysCostItem> itemsChildren;

    public String getCurrMonthValue() {
        return currMonthValue;
    }

    public void setCurrMonthValue(String currMonthValue) {
        this.currMonthValue = currMonthValue;
    }

    public List<SysCostItem> getItemsChildren() {
        return itemsChildren;
    }

    public void setItemsChildren(List<SysCostItem> itemsChildren) {
        this.itemsChildren = itemsChildren;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public List<RsCostReportEntity> getItemsChildrens() {
        return itemsChildrens;
    }

    public void setItemsChildrens(List<RsCostReportEntity> itemsChildrens) {
        this.itemsChildrens = itemsChildrens;
    }

    @TableField(exist = false)
    private List<SysSapSubject> sapSubjectList;

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

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(Integer dataFrom) {
        this.dataFrom = dataFrom;
    }

    public String getDataFromDesc() {
        return dataFromDesc;
    }

    public void setDataFromDesc(String dataFromDesc) {
        this.dataFromDesc = dataFromDesc;
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

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getHandlingDesc() {
        return handlingDesc;
    }

    public void setHandlingDesc(Integer handlingDesc) {
        this.handlingDesc = handlingDesc;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<SysSapSubject> getSapSubjectList() {
        return sapSubjectList;
    }

    public void setSapSubjectList(List<SysSapSubject> sapSubjectList) {
        this.sapSubjectList = sapSubjectList;
    }

    public String getSapCodeAndName() {
        return sapCodeAndName;
    }

    public void setSapCodeAndName(String sapCodeAndName) {
        this.sapCodeAndName = sapCodeAndName;
    }

    @Override
    public String toString() {
        return "SysCostItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orgCode=" + orgCode +
                ", reportId=" + reportId +
                ", level=" + level +
                ", seq=" + seq +
                ", parentId=" + parentId +
                ", dataFrom=" + dataFrom +
                ", dataFromDesc='" + dataFromDesc + '\'' +
                ", enable=" + enable +
                ", createOperId=" + createOperId +
                ", createTime=" + createTime +
                ", updateOperId=" + updateOperId +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                ", costType='" + costType + '\'' +
                ", length=" + length +
                ", handlingDesc=" + handlingDesc +
                ", count=" + count +
                ", sapCodeAndName='" + sapCodeAndName + '\'' +
                ", sapSubjectList=" + sapSubjectList +
                '}';
    }
}

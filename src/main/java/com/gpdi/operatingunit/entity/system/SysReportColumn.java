package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * @Author: zyb
 * @Date: 2019/11/15 9:37
 */
@TableName("sys_report_column")
public class SysReportColumn {

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 报表id
     */
    private Integer reportId;
    /**
     *
     */
    private String orgCode;
    /**
     * 父级id
     */
    private Integer parentId;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 名称
     */
    private String name;
    /**
     * 属性值
     */
    private String prop;
    /**
     * 字段数据类型:string、int、number、date（日期格式的string）
     */
    private String dataType;
    /**
     * 数据来源
     */
    private String dataFrom;
    /**
     * 单位（如：元、万元、%等）
     */
    private String uom;
    /**
     * 合并行数量
     */
    private Integer rowspan;
    /**
     * 合并列数量
     */
    private Integer colspan;
    /**
     * 是否为可移动字段:0/1
     */
    private Integer movable;
    /**
     * 是否可用:0/1
     */
    private Integer enabled;
    /**
     * 是否可编辑
     */
    private Integer editable;
    /**
     * 排序
     */
    private Integer seq;
    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private Integer orgLevel;
    @TableField(exist = false)
    private String costType;


    @TableField(exist = false)
    private List<SysReportColumn> children;

    public List<SysReportColumn> getChildren() {
        return children;
    }

    public void setChildren(List<SysReportColumn> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(String dataFrom) {
        this.dataFrom = dataFrom;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public Integer getMovable() {
        return movable;
    }

    public void setMovable(Integer movable) {
        this.movable = movable;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getEditable() {
        return editable;
    }

    public void setEditable(Integer editable) {
        this.editable = editable;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }
}

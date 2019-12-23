package com.gpdi.operatingunit.entity.relation;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/11/14 17:39
 */

@TableName("rel_report_column_value")
public class RelReportColumnValue {

    /**
     *
     */
    @TableId
    private Long id;
    /**
     *
     */
    private Integer reportId;
    /**
     * 月份 201906 格式 int
     */
    private Integer month;
    /**
     * 区-组织编码
     */
    private String districtCode;
    /**
     * 中心-组织编码
     */
    private String centerCode;
    /**
     * 所属市编码
     */
    private Long topOrgCode;
    /**
     * 'nonentity'表示不存在prop属性
     */
    private String prop;
    /**
     * 列的值
     */
    private String value;
    /**
     * 修正值
     */
    private String changed;
    /**
     * 最终值（列的值+修正值）
     */
    private String finalValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 类型
     */
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public Long getTopOrgCode() {
        return topOrgCode;
    }

    public void setTopOrgCode(Long topOrgCode) {
        this.topOrgCode = topOrgCode;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChanged() {
        return changed;
    }

    public void setChanged(String changed) {
        this.changed = changed;
    }

    public String getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(String finalValue) {
        this.finalValue = finalValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.util.Date;

/**
 * 成本中心表
 *
 * @author Zhb
 * @date 2019-10-28 21:14:30
 */
@TableName("sys_cost_center")
public class SysCostCenter implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID 主键
     * JsonSerialize解决long精度丢失问题
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 成本中心编码
     */
    private String code;
    /**
     * 范围
     */
    private String scope;
    /**
     * 公司
     */
    private String company;
    /**
     * CCtC
     */
    private String cctc;
    /**
     * 负责人
     */
    private String head;
    /**
     * 负责的用户
     */
    private String responsibleUser;
    /**
     * 简要说明
     */
    private String intro;
    /**
     * 语言
     */
    private String language;
    /**
     * 有效期自
     */
    private String fromDate;
    /**
     * 有效期至
     */
    private String toDate;
    /**
     * 归属区局
     */
    private String belongDistrict;
    /**
     * 归属营服
     */
    private String belongCenter;
    /**
     * 顶级组织编码
     */
    private Long topOrgCode;
    /**
     * 组织编码
     */
    private Long orgCode;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCctc() {
        return cctc;
    }

    public void setCctc(String cctc) {
        this.cctc = cctc;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(String responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getBelongDistrict() {
        return belongDistrict;
    }

    public void setBelongDistrict(String belongDistrict) {
        this.belongDistrict = belongDistrict;
    }

    public String getBelongCenter() {
        return belongCenter;
    }

    public void setBelongCenter(String belongCenter) {
        this.belongCenter = belongCenter;
    }

    public Long getTopOrgCode() {
        return topOrgCode;
    }

    public void setTopOrgCode(Long topOrgCode) {
        this.topOrgCode = topOrgCode;
    }

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
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
}

package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * SAP科目表
 * 
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-10-27 21:17:01
 */
@TableName("sys_sap_subject")
public class SysSapSubject implements Serializable {

	/**
	 * SAP科目
	 */
	@TableId
	private Long code;
	/**
	 * SAP科目名称
	 */
	private String name;
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
	private Long orgCode;
	@TableField(exist = false)
	private Integer year;
	@TableField(exist = false)
	private Integer reportId;
	@TableField(exist = false)
	private Long sid;
	@TableField(exist = false)
	private String sname;

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

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	@Override
	public String toString() {
		return "SysSapSubject{" +
				"code=" + code +
				", name='" + name + '\'' +
				", enable=" + enable +
				", createOperId=" + createOperId +
				", createTime=" + createTime +
				", updateOperId=" + updateOperId +
				", updateTime=" + updateTime +
				", orgCode=" + orgCode +
				", year=" + year +
				", reportId=" + reportId +
				", sid=" + sid +
				", sname='" + sname + '\'' +
				'}';
	}
}

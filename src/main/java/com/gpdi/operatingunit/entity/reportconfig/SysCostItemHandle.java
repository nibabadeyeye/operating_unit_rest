package com.gpdi.operatingunit.entity.reportconfig;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.util.Date;

/**
 * 成本项目数据处理表
 * 
 * @author Zhb
 * @date 2019-11-03 20:11:52
 */
@TableName("sys_cost_item_handle")
public class SysCostItemHandle implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID 主键
	 */
	@TableId
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 成本项目名称
	 */
	private String name;
	/**
	 * 成本项id路径
	 */
	private String fullId;
	/**
	 * 成本项名称路径
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

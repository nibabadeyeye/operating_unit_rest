package com.gpdi.operatingunit.entity.reportconfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 简易边际利润表（用户）
 * 
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-12-02 22:34:46
 */
@Data
@TableName("t_simple_profits_report")
public class SimpleProfitsReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键（编号）
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 名称
	 */
	private String name;
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
	 * 所属市编码
	 */
	private Long topOrgCode;

	/**
	 * 账号名
	 */
	private String createOperUserName;

	@TableField(exist = false)
	private String username;

	@TableField(exist = false)
	private List<SimpleProfitsReportEntity> list;

	public List<SimpleProfitsReportEntity> getList() {
		return list;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setList(List<SimpleProfitsReportEntity> list) {
		this.list = list;
	}

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

	public Long getTopOrgCode() {
		return topOrgCode;
	}

	public void setTopOrgCode(Long topOrgCode) {
		this.topOrgCode = topOrgCode;
	}

	public String getCreateOperUserName() {
		return createOperUserName;
	}

	public void setCreateOperUserName(String createOperUserName) {
		this.createOperUserName = createOperUserName;
	}
}

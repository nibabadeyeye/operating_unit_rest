package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author zyb
 * @date 2019-11-27 21:57:54
 */
@TableName("sys_sap_account")
public class SysSapAccount implements Serializable {
	/**
	 * 
	 */
	@TableId(value = "id",type = IdType.AUTO)
	private Integer id;
	/**
	 * 所属市编码
	 */
	private Long topOrgCode;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 业务类别
	 */
	private String businessType;
	/**
	 * 账号类型
	 */
	private String accountType;
	/**
	 * 
	 */
	private Integer createId;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Integer updateId;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;

	@TableField(exist = false)
	private String city;
	@TableField(exist = false)
	private String oldPwd;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getTopOrgCode() {
		return topOrgCode;
	}

	public void setTopOrgCode(Long topOrgCode) {
		this.topOrgCode = topOrgCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	@Override
	public String toString() {
		return "SysSapAccount{" +
				"id=" + id +
				", topOrgCode=" + topOrgCode +
				", account='" + account + '\'' +
				", password='" + password + '\'' +
				", businessType='" + businessType + '\'' +
				", accountType='" + accountType + '\'' +
				", createId=" + createId +
				", createTime=" + createTime +
				", updateId=" + updateId +
				", updateTime=" + updateTime +
				", remark='" + remark + '\'' +
				", city='" + city + '\'' +
				", oldPwd='" + oldPwd + '\'' +
				'}';
	}
}

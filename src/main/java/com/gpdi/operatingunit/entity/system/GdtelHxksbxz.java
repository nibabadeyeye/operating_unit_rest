package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author zyb
 * @date 2019-12-08 21:26:19
 */
@TableName("gdtel_hxksbxz")
public class GdtelHxksbxz implements Serializable {
	/**
	 * 
	 */
	@TableId(value = "id",type = IdType.AUTO)
	private Integer id;
	/**
	 * 月份(例:1月)
	 */
	private String startDate;
	/**
	 * 
	 */
	private String endDate;
	/**
	 * 
	 */
	private String city;
	/**
	 * 
	 */
	private String sapAccount;
	/**
	 * 
	 */
	private String sapPassword;
	/**
	 * 
	 */
	private String analyzerAccount;
	/**
	 * 
	 */
	private String analyzerPassword;
	/**
	 * 
	 */
	private String oaAccount;
	/**
	 * 
	 */
	private String oaPassword;
	/**
	 * 
	 */
	private String email;
	/**
	 * 
	 */
	private Date date;
	/**
	 * 
	 */
	private String branch;
	/**
	 * 
	 */
	private String account;
	/**
	 * 
	 */
	private String serviceTem;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSapAccount() {
		return sapAccount;
	}

	public void setSapAccount(String sapAccount) {
		this.sapAccount = sapAccount;
	}

	public String getSapPassword() {
		return sapPassword;
	}

	public void setSapPassword(String sapPassword) {
		this.sapPassword = sapPassword;
	}

	public String getAnalyzerAccount() {
		return analyzerAccount;
	}

	public void setAnalyzerAccount(String analyzerAccount) {
		this.analyzerAccount = analyzerAccount;
	}

	public String getAnalyzerPassword() {
		return analyzerPassword;
	}

	public void setAnalyzerPassword(String analyzerPassword) {
		this.analyzerPassword = analyzerPassword;
	}

	public String getOaAccount() {
		return oaAccount;
	}

	public void setOaAccount(String oaAccount) {
		this.oaAccount = oaAccount;
	}

	public String getOaPassword() {
		return oaPassword;
	}

	public void setOaPassword(String oaPassword) {
		this.oaPassword = oaPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getServiceTem() {
		return serviceTem;
	}

	public void setServiceTem(String serviceTem) {
		this.serviceTem = serviceTem;
	}

	@Override
	public String toString() {
		return "GdtelHxksbxz{" +
				"id=" + id +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", city='" + city + '\'' +
				", sapAccount='" + sapAccount + '\'' +
				", sapPassword='" + sapPassword + '\'' +
				", analyzerAccount='" + analyzerAccount + '\'' +
				", analyzerPassword='" + analyzerPassword + '\'' +
				", oaAccount='" + oaAccount + '\'' +
				", oaPassword='" + oaPassword + '\'' +
				", email='" + email + '\'' +
				", date=" + date +
				", branch='" + branch + '\'' +
				", account='" + account + '\'' +
				", serviceTem='" + serviceTem + '\'' +
				'}';
	}
}

package com.gpdi.operatingunit.entity.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * SAP表
 * 
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-10-25 03:57:59
 */
@TableName("data_sap_list")
public class DataSapList implements Serializable {
	/**
	 * 
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 财年
	 */
	private String year;
	/**
	 * 月份
	 */
	private String month;
	/**
	 * 公司
	 */
	private String company;
	/**
	 * 
	 */
	private String centerId;
	/**
	 * 成本项
	 */
	private String tpye;
	/**
	 * per
	 */
	private String per;
	/**
	 * 参考凭证号
	 */
	private String credentialsNo;
	/**
	 * 凭证日期
	 */
	private String credentialsDate;
	/**
	 * 凭证类
	 */
	private String credentialsTpye;
	/**
	 * PK
	 */
	private String pk;
	/**
	 * SAP科目
	 */
	private String name;
	/**
	 * 年度/月份
	 */
	private String yearMonth;
	/**
	 * 参考码3
	 */
	private String custom3;
	/**
	 * 报表货币值
	 */
	private String currencyValue;
	/**
	 * 本币
	 */
	private String currency;
	/**
	 * 成本要素
	 */
	private String costElement;
	/**
	 * 成本要素描述
	 */
	private String costElementDesc;
	/**
	 * 利润中心
	 */
	private String profitCenter;
	/**
	 * 利润中心描述
	 */
	private String profitCenterDesc;
	/**
	 * 成本中心
	 */
	private String costCenter;
	/**
	 * 成本中心描述
	 */
	private String costCenterDesc;
	/**
	 * 功能范围
	 */
	private String scope;
	/**
	 * 装移
	 */
	private String zhuangyi;
	/**
	 * 用途
	 */
	private String type;
	/**
	 * 经济事项
	 */
	private String economicConsiderations;
	/**
	 * 供应商
	 */
	private String supplier;
	/**
	 * 合同号
	 */
	private String contractNo;
	/**
	 * 
	 */
	private String operator;
	/**
	 * 报账单号
	 */
	private String serialNum;
	/**
	 * 参考码1
	 */
	private String custom1;
	/**
	 * 参考码2
	 */
	private String custom2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCenterId() {
		return centerId;
	}

	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}

	public String getTpye() {
		return tpye;
	}

	public void setTpye(String tpye) {
		this.tpye = tpye;
	}

	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}

	public String getCredentialsNo() {
		return credentialsNo;
	}

	public void setCredentialsNo(String credentialsNo) {
		this.credentialsNo = credentialsNo;
	}

	public String getCredentialsDate() {
		return credentialsDate;
	}

	public void setCredentialsDate(String credentialsDate) {
		this.credentialsDate = credentialsDate;
	}

	public String getCredentialsTpye() {
		return credentialsTpye;
	}

	public void setCredentialsTpye(String credentialsTpye) {
		this.credentialsTpye = credentialsTpye;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getCustom3() {
		return custom3;
	}

	public void setCustom3(String custom3) {
		this.custom3 = custom3;
	}

	public String getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCostElement() {
		return costElement;
	}

	public void setCostElement(String costElement) {
		this.costElement = costElement;
	}

	public String getCostElementDesc() {
		return costElementDesc;
	}

	public void setCostElementDesc(String costElementDesc) {
		this.costElementDesc = costElementDesc;
	}

	public String getProfitCenter() {
		return profitCenter;
	}

	public void setProfitCenter(String profitCenter) {
		this.profitCenter = profitCenter;
	}

	public String getProfitCenterDesc() {
		return profitCenterDesc;
	}

	public void setProfitCenterDesc(String profitCenterDesc) {
		this.profitCenterDesc = profitCenterDesc;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getCostCenterDesc() {
		return costCenterDesc;
	}

	public void setCostCenterDesc(String costCenterDesc) {
		this.costCenterDesc = costCenterDesc;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getZhuangyi() {
		return zhuangyi;
	}

	public void setZhuangyi(String zhuangyi) {
		this.zhuangyi = zhuangyi;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEconomicConsiderations() {
		return economicConsiderations;
	}

	public void setEconomicConsiderations(String economicConsiderations) {
		this.economicConsiderations = economicConsiderations;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getCustom1() {
		return custom1;
	}

	public void setCustom1(String custom1) {
		this.custom1 = custom1;
	}

	public String getCustom2() {
		return custom2;
	}

	public void setCustom2(String custom2) {
		this.custom2 = custom2;
	}
}

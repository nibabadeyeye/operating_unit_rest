package com.gpdi.operatingunit.entity.reportconfig;

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
 * @date 2019-10-25 05:21:50
 */
@Data
@TableName("data_sap_list")
public class DataSapListEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 财年
	 */
	private Integer year;
	/**
	 * Per
	 */
	private String per;
	/**
	 * 公司
	 */
	private String company;
	/**
	 * 参考凭证号
	 */
	private String credentialsNo;
	/**
	 * 凭证类
	 */
	private String credentialsTpye;
	/**
	 * 凭证日期
	 */
	private String credentialsDate;
	/**
	 * PK
	 */
	private String pk;
	/**
	 * 报表货币值
	 */
	private String currencyValue;
	/**
	 * 本币
	 */
	private String currency;
	/**
	 * 利润中心
	 */
	private String profitCenter;
	/**
	 * 利润中心描述
	 */
	private String profitCenterDesc;
	/**
	 * 文本/摘要
	 */
	private String text;
	/**
	 * 成本中心
	 */
	private String costCenter;
	/**
	 * 成本中心描述
	 */
	private String costCenterDesc;
	/**
	 * 科目
	 */
	private String subject;
	/**
	 * 年度/月份
	 */
	private String yearMonth;
	/**
	 * 成本要素
	 */
	private Long costElement;
	/**
	 * 成本要素描述
	 */
	private String costElementDesc;
	/**
	 * 装移
	 */
	private String zhuangyi;
	/**
	 * 功能范围
	 */
	private String scope;
	/**
	 * 用途
	 */
	private String usage;
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
	 * 报账人
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
	/**
	 * 参考码3
	 */
	private String custom3;
	/**
	 * 组织编码
	 */
	private Long orgCode;
	/**
	 * 营服id
	 */
	private Long centerId;
	/**
	 * 月份
	 */
	private Integer month;
	/**
	 * 成本项
	 */
	private String costItem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCredentialsNo() {
		return credentialsNo;
	}

	public void setCredentialsNo(String credentialsNo) {
		this.credentialsNo = credentialsNo;
	}

	public String getCredentialsTpye() {
		return credentialsTpye;
	}

	public void setCredentialsTpye(String credentialsTpye) {
		this.credentialsTpye = credentialsTpye;
	}

	public String getCredentialsDate() {
		return credentialsDate;
	}

	public void setCredentialsDate(String credentialsDate) {
		this.credentialsDate = credentialsDate;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Long getCostElement() {
		return costElement;
	}

	public void setCostElement(Long costElement) {
		this.costElement = costElement;
	}

	public String getCostElementDesc() {
		return costElementDesc;
	}

	public void setCostElementDesc(String costElementDesc) {
		this.costElementDesc = costElementDesc;
	}

	public String getZhuangyi() {
		return zhuangyi;
	}

	public void setZhuangyi(String zhuangyi) {
		this.zhuangyi = zhuangyi;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
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

	public String getCustom3() {
		return custom3;
	}

	public void setCustom3(String custom3) {
		this.custom3 = custom3;
	}

	public Long getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}

	public Long getCenterId() {
		return centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getCostItem() {
		return costItem;
	}

	public void setCostItem(String costItem) {
		this.costItem = costItem;
	}
}

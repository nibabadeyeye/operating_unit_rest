package com.gpdi.operatingunit.entity.reportconfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 成本报表数据结果表
 *
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-10-29 21:57:40
 */
@TableName("rs_cost_report")
public class RsCostReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 报表id
	 */
	private Integer reportId;
	/**
	 * 组织编码
	 */
	private Long orgCode;
	/**
	 * 父级组织编码
	 */
	private Long parentOrgCode;
	/**
	 * 所属市编码
	 */
	private Long topOrgCode;
	/**
	 * 费用id
	 */
	private Long costItemId;
	/**
	 * 月份
	 */
	private Integer month;
	/**
	 * 预算值
	 */
	private String budget;
	/**
	 * 本月数
	 */
	private String currMonthValue;
	/**
	 * 上月数
	 */
	private String lastMonthValue;
	/**
	 * 比上月增减（万元）
	 */
	private String diffLastMonth;
	/**
	 * 比上月增减（%）
	 */
	private String percLastMonth;
	/**
	 * 本年累计数
	 */
	private String currYearValue;
	/**
	 * 上年累计数
	 */
	private String lastYearValue;
	/**
	 * 比上年增减（万元）
	 */
	private String diffLastYear;
	/**
	 * 比上年增减（%）
	 */
	private String percLastYear;
	/**
	 * 进度
	 */
	private String progress;
	/**
	 * 剩余预算
	 */
	private String remainingBudget;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

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

	public Long getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}

	public Long getParentOrgCode() {
		return parentOrgCode;
	}

	public void setParentOrgCode(Long parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getCurrMonthValue() {
		return currMonthValue;
	}

	public void setCurrMonthValue(String currMonthValue) {
		this.currMonthValue = currMonthValue;
	}

	public String getLastMonthValue() {
		return lastMonthValue;
	}

	public void setLastMonthValue(String lastMonthValue) {
		this.lastMonthValue = lastMonthValue;
	}

	public String getDiffLastMonth() {
		return diffLastMonth;
	}

	public void setDiffLastMonth(String diffLastMonth) {
		this.diffLastMonth = diffLastMonth;
	}

	public String getPercLastMonth() {
		return percLastMonth;
	}

	public void setPercLastMonth(String percLastMonth) {
		this.percLastMonth = percLastMonth;
	}

	public String getCurrYearValue() {
		return currYearValue;
	}

	public void setCurrYearValue(String currYearValue) {
		this.currYearValue = currYearValue;
	}

	public String getLastYearValue() {
		return lastYearValue;
	}

	public void setLastYearValue(String lastYearValue) {
		this.lastYearValue = lastYearValue;
	}

	public String getDiffLastYear() {
		return diffLastYear;
	}

	public void setDiffLastYear(String diffLastYear) {
		this.diffLastYear = diffLastYear;
	}

	public String getPercLastYear() {
		return percLastYear;
	}

	public void setPercLastYear(String percLastYear) {
		this.percLastYear = percLastYear;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getRemainingBudget() {
		return remainingBudget;
	}

	public void setRemainingBudget(String remainingBudget) {
		this.remainingBudget = remainingBudget;
	}

	public Long getCostItemId() {
		return costItemId;
	}

	public void setCostItemId(Long costItemId) {
		this.costItemId = costItemId;
	}

	public Long getTopOrgCode() {
		return topOrgCode;
	}

	public void setTopOrgCode(Long topOrgCode) {
		this.topOrgCode = topOrgCode;
	}

	@Override
	public String toString() {
		return "RsCostReportEntity{" +
				"id=" + id +
				", reportId=" + reportId +
				", orgCode=" + orgCode +
				", parentOrgCode=" + parentOrgCode +
				", topOrgCode=" + topOrgCode +
				", costItemId=" + costItemId +
				", month=" + month +
				", budget='" + budget + '\'' +
				", currMonthValue='" + currMonthValue + '\'' +
				", lastMonthValue='" + lastMonthValue + '\'' +
				", diffLastMonth='" + diffLastMonth + '\'' +
				", percLastMonth='" + percLastMonth + '\'' +
				", currYearValue='" + currYearValue + '\'' +
				", lastYearValue='" + lastYearValue + '\'' +
				", diffLastYear='" + diffLastYear + '\'' +
				", percLastYear='" + percLastYear + '\'' +
				", progress='" + progress + '\'' +
				", remainingBudget='" + remainingBudget + '\'' +
				'}';
	}
}

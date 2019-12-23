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
 * 简易边际利润表（详情）
 * 
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-12-02 22:34:46
 */
@Data
@TableName("t_simple_profit_report_details")
public class SimpleProfitReportDetailsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 编码
	 */
	private String coding;
	/**
	 * 创建id
	 */
	private String simpleProfitsReportId;
	/**
	 * 月均入网量
	 */
	private String monthlyAverageIntoNetwork;
	/**
	 * Arpu
	 */
	private String arpu;
	/**
	 * 12期保有率
	 */
	private String retentionRate;
	/**
	 * 从业人员数量
	 */
	private String numberOfPersonnel;
	/**
	 * 人均月薪
	 */
	private String averageMonthlySalary;
	/**
	 * 厅店面积
	 */
	private String area;
	/**
	 * 租金单价
	 */
	private String unitPriceOfRent;
	/**
	 * 用户id
	 */
	private Integer userId;

	@TableField(exist = false)
	private List<SimpleProfitReportDetailsEntity> chelList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getSimpleProfitsReportId() {
		return simpleProfitsReportId;
	}

	public void setSimpleProfitsReportId(String simpleProfitsReportId) {
		this.simpleProfitsReportId = simpleProfitsReportId;
	}

	public String getMonthlyAverageIntoNetwork() {
		return monthlyAverageIntoNetwork;
	}

	public void setMonthlyAverageIntoNetwork(String monthlyAverageIntoNetwork) {
		this.monthlyAverageIntoNetwork = monthlyAverageIntoNetwork;
	}

	public String getArpu() {
		return arpu;
	}

	public void setArpu(String arpu) {
		this.arpu = arpu;
	}

	public String getRetentionRate() {
		return retentionRate;
	}

	public void setRetentionRate(String retentionRate) {
		this.retentionRate = retentionRate;
	}

	public String getNumberOfPersonnel() {
		return numberOfPersonnel;
	}

	public void setNumberOfPersonnel(String numberOfPersonnel) {
		this.numberOfPersonnel = numberOfPersonnel;
	}

	public String getAverageMonthlySalary() {
		return averageMonthlySalary;
	}

	public void setAverageMonthlySalary(String averageMonthlySalary) {
		this.averageMonthlySalary = averageMonthlySalary;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getUnitPriceOfRent() {
		return unitPriceOfRent;
	}

	public void setUnitPriceOfRent(String unitPriceOfRent) {
		this.unitPriceOfRent = unitPriceOfRent;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<SimpleProfitReportDetailsEntity> getChelList() {
		return chelList;
	}

	public void setChelList(List<SimpleProfitReportDetailsEntity> chelList) {
		this.chelList = chelList;
	}
}

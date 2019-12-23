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
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-10-28 05:37:46
 */
@TableName("rel_sap_cost_item_subitem")
public class RelSapCostItemSubitem implements Serializable {

	/**
	 * 
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 *
	 */
	private Long parentId;
	/**
	 * 
	 */
	private Long orgCode;
	/**
	 * 
	 */
	private Integer year;
	/**
	 * 报表Id
	 */
	private Integer reportId;
	/**
	 * SAP科目
	 */
	private Long sapCode;
	/**
	 * 字段名称
	 */
	private String fieldName;
	/**
	 * 字段名称描述
	 */
	private String fieldNameDesc;
	/**
	 * 操作
	 */
	private String operation;
	/**
	 * 操作描述
	 */
	private String operationDesc;
	/**
	 * 参数
	 */
	private String params;

	@TableField(exist = false)
	private String operations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public Long getSapCode() {
		return sapCode;
	}

	public void setSapCode(Long sapCode) {
		this.sapCode = sapCode;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldNameDesc() {
		return fieldNameDesc;
	}

	public void setFieldNameDesc(String fieldNameDesc) {
		this.fieldNameDesc = fieldNameDesc;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperationDesc() {
		return operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getOperations() {
		return operations;
	}

	public void setOperations(String operations) {
		this.operations = operations;
	}

	@Override
	public String toString() {
		return "RelSapCostItemSubitem{" +
				"id=" + id +
				", parentId=" + parentId +
				", orgCode=" + orgCode +
				", year=" + year +
				", reportId=" + reportId +
				", sapCode=" + sapCode +
				", fieldName='" + fieldName + '\'' +
				", fieldNameDesc='" + fieldNameDesc + '\'' +
				", operation='" + operation + '\'' +
				", operationDesc='" + operationDesc + '\'' +
				", params='" + params + '\'' +
				", operations='" + operations + '\'' +
				'}';
	}
}

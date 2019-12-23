package com.gpdi.operatingunit.entity.reportconfig;

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
 * @date 2019-11-14 02:36:44
 */
@TableName("rel_org_report_column")
public class RelOrgReportColumnEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 顶级组织编码
	 */
	private Long topOrgCode;
	/**
	 * 报表id
	 */
	private Integer reportId;
	/**
	 * 展示的字段
	 */
	private String prop;
	/**
	 * 字段名称
	 */
	private String name;
	/**
	 * 排序
	 */
	private Integer seq;
	/**
	 * 状态：1正常、0失效、-1删除
	 */
	private Integer status;

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

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}

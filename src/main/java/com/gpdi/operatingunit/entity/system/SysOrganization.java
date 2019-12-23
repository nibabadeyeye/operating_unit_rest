package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gpdi.operatingunit.entity.reportconfig.RsCostReportEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 组织机构表
 * 
 * @author Zhb
 * @date 2019-10-28 21:14:30
 */
@TableName("sys_organization")
public class SysOrganization implements Serializable {

	public SysOrganization() {

	}

	private static final long serialVersionUID = 1L;

	public final static String ORG_LEVEL_CAT = "sys_organization.level";

	/**
	 * 组织编码
	 */
	@TableId
	private Long code;
	/**
	 * 顶级组织编码
	 */
	private Long topOrgCode;
	/**
	 * 组织名称
	 */
	private String name;
	/**
	 * 组织别名
	 */
	private String alias;
	/**
	 * 父级组织编码
	 */
	private Long parentCode;
	/**
	 * 组织编码路径
	 */
	private String fullCode;
	/**
	 * 组织名称路径
	 */
	private String fullName;
	/**
	 * 层级
	 */
	private Integer level;
	/**
	 * 排序
	 */
	private Integer seq;
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

	@TableField(exist = false)
	private List<SysOrgAnizationCamp> childrens;

	@TableField(exist = false)
	private List<RsCostReportEntity> childrensMonth;

	@TableField(exist = false)
	private List<SysOrganization> children;

	@TableField(exist = false)
	private String cname;

	@TableField(exist = false)
	private String currMonthValue;

	@TableField(exist = false)
	List<SysCostItem> itemsChildren;

	@TableField(exist = false)
	private String createOper;

	@TableField(exist = false)
	private String updateOper;

	@TableField(exist = false)
	private String budget;

	@TableField(exist = false)
	private String progress;

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public List<SysCostItem> getItemsChildren() {
		return itemsChildren;
	}

	public void setItemsChildren(List<SysCostItem> itemsChildren) {
		this.itemsChildren = itemsChildren;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCurrMonthValue() {
		return currMonthValue;
	}

	public void setCurrMonthValue(String currMonthValue) {
		this.currMonthValue = currMonthValue;
	}

	public List<SysOrganization> getChildren() {
		return children;
	}

	public void setChildren(List<SysOrganization> children) {
		this.children = children;
	}

	public List<RsCostReportEntity> getChildrensMonth() {
		return childrensMonth;
	}

	public void setChildrensMonth(List<RsCostReportEntity> childrensMonth) {
		this.childrensMonth = childrensMonth;
	}

	public List<SysOrgAnizationCamp> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<SysOrgAnizationCamp> childrens) {
		this.childrens = childrens;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public Long getTopOrgCode() {
		return topOrgCode;
	}

	public void setTopOrgCode(Long topOrgCode) {
		this.topOrgCode = topOrgCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Long getParentCode() {
		return parentCode;
	}

	public void setParentCode(Long parentCode) {
		this.parentCode = parentCode;
	}

	public String getFullCode() {
		return fullCode;
	}

	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getCreateOper() {
		return createOper;
	}

	public void setCreateOper(String createOper) {
		this.createOper = createOper;
	}

	public String getUpdateOper() {
		return updateOper;
	}

	public void setUpdateOper(String updateOper) {
		this.updateOper = updateOper;
	}
}

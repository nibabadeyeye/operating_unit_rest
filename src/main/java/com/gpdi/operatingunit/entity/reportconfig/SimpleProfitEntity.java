package com.gpdi.operatingunit.entity.reportconfig;

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
 * @date 2019-12-02 22:34:46
 */
@Data
@TableName("t_simple_profit")
public class SimpleProfitEntity implements Serializable {
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
	 * 名称
	 */
	private String name;
	/**
	 * 子节点
	 */
	private String childNode;
	/**
	 * 排序
	 */
	private String seq;

	@TableField(exist = false)
	private String chilName;

	public String getChilName() {
		return chilName;
	}

	public void setChilName(String chilName) {
		this.chilName = chilName;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChildNode() {
		return childNode;
	}

	public void setChildNode(String childNode) {
		this.childNode = childNode;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}
}

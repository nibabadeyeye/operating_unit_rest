package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 字典表
 * 
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-10-27 20:47:31
 */
@TableName("sys_dict")
public class SysDict implements Serializable {

	public final static String SYS_YES_NO_CAT = "sys.yes_no";
	public final static String SYS_STATUS_CAT = "sys.status";

	/**
	 * 
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 类型
	 */
	private String cat;
	/**
	 * 文本
	 */
	private String text;
	/**
	 * 值
	 */
	private String value;
	/**
	 * 排序
	 */
	private Integer seq;
	/**
	 * 
	 */
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

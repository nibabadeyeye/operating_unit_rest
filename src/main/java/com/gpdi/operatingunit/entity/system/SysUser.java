package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 用户表
 * @Author: Lxq
 * @Date: 2019-10-23 04:03:52
 */
@TableName("sys_user")
public class SysUser implements Serializable{

    /**
     * ID 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户名/登录名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 加密盐
     */
    private String salt;
    /**
     * 状态：1正常、0无效/锁定、-1删除
     */
    private Integer status;
    /**
     * 性别：1男、0女、-1未知性别
     */
    private Integer gender;
    /**
     * OA账号
     */
    private String oaAccount;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobileNo;
    /**
     * 固话
     */
    private String phoneNo;
    /**
     * 工号
     */
    private String staffNo;
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

    /**
     * 顶级组织编码
     */
    private Long topOrgCode;

    /**
     * 关系组织编码
     */
    private Long orgCode;

    /**
     *
     */
    @TableField(exist = false)
    private Integer[] roles;

    @TableField(exist = false)
    private String roleNames;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getOaAccount() {
        return oaAccount;
    }

    public void setOaAccount(String oaAccount) {
        this.oaAccount = oaAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
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

    public Long getTopOrgCode() {
        return topOrgCode;
    }

    public void setTopOrgCode(Long topOrgCode) {
        this.topOrgCode = topOrgCode;
    }

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }

    public Integer[] getRoles() {
        return roles;
    }

    public void setRoles(Integer[] roles) {
        this.roles = roles;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
}

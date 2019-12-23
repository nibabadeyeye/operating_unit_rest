package com.gpdi.operatingunit.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 *
 * @Author Lxq
 * @Date 2019-10-23 04:20:29
 **/
@TableName("sys_log")
public class SysLog implements Serializable {

    /**
     * ID 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 操作类型
     */
    private String operType;
    /**
     * 请求的URL
     */
    private String url;
    /**
     * 请求方法类型：post/get/put/delete
     */
    private String method;
    /**
     * 请求头
     */
    private String header;
    /**
     * body
     */
    private String body;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 请求的类名称
     */
    private String className;
    /**
     * 请求的方法名称
     */
    private String methodName;
    /**
     * 执行的sql
     */
    private String executeSql;
    /**
     * 执行时长(毫秒)
     */
    private Long executeTime;
    /**
     * 执行结果
     */
    private String result;
    /**
     * 错误信息
     */
    private String errorInfo;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 操作时间
     */
    private Date operTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getExecuteSql() {
        return executeSql;
    }

    public void setExecuteSql(String executeSql) {
        this.executeSql = executeSql;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }
}

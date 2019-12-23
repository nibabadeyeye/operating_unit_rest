package com.gpdi.operatingunit.entity.originaltabledisplay;

/**
 * @desc: 封装服务器响应信息
 */
public class ResponseData {
    //请求返回的状态码
    private int code;
    //请求返回的状态信息
    private String msg;
    //请求返回的状态
    private Object data;
    //系统凭证
    private String token;

    public ResponseData() {
    }

    public ResponseData(int code, String msg, String token) {
        this.code = code;
        this.msg = msg;
        this.token = token;
    }

    public ResponseData(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

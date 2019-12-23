package com.gpdi.operatingunit.config;

import java.util.Map;

/**
 * @Description:
 * @Author: Lxq
 * @Date: 2019/9/6 14:45
 */
public class R {

    private int status;
    private String msg;
    private Object data;
    private Map<String,Object> extra;

    public R() {
    }

    public R(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static R ok() {
        return new R(200, "ok", null);
    }

    public static R ok(String msg) {
        return new R(200, msg, null);
    }

    public static R ok(Object data) {
        return new R(200, "ok", data);
    }

    public static R ok(String msg, Object data) {
        return new R(200, msg, data);
    }

    public static R error(String msg) {
        return new R(500, msg, null);
    }

    public static R error(String msg, Object data) {
        return new R(500, msg, data);
    }

    public static R error(int code, String msg, Object data) {
        return new R(code, msg, data);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }
}

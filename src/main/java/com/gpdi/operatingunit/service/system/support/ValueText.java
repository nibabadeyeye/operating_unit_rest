package com.gpdi.operatingunit.service.system.support;

import java.io.Serializable;

/**
 * @author Zhb
 * @date 2019/10/30 18:55
 **/
public class ValueText implements Serializable{

    private Object value;
    private Object text;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }
}

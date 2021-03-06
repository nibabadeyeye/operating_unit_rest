package com.gpdi.operatingunit.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:解析监听器，每解析一行会回调invoke()方法。
 * 整个excel解析结束会执行doAfterAllAnalysed()方法
 * @Author: Lxq
 * @Date: 2019/10/22 9:19
 */
public class ExcelListener extends AnalysisEventListener {


    private List<Object> datas = new ArrayList<>();

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }

    /**
     * 逐行解析
     * object : 当前行的数据
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        //当前行
        // context.getCurrentRowNum()
        if (object != null) {
            datas.add(object);
        }
    }

    /**
     * 解析完所有数据后会调用该方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //解析结束销毁不用的资源
    }
}

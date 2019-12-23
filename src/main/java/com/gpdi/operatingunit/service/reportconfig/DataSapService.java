package com.gpdi.operatingunit.service.reportconfig;

import java.io.InputStream;

/**
 * @Author：long
 * @Date：2019/10/29 8:39
 * @Description：sap数据配置
 */
public interface DataSapService {

    int dataUpload(InputStream inputStream, InputStream sheetInputStream, String month);
}

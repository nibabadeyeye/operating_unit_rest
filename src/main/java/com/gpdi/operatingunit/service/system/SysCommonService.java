package com.gpdi.operatingunit.service.system;

import com.gpdi.operatingunit.service.system.support.ColumnInfo;

import java.util.List;

/**
 * @author Zhb
 * @date 2019/10/30 10:51
 **/
public interface SysCommonService {

    /**
     * 查询表字段信息
     *
     * @param tableName   表名称
     * @param tableSchema 数据库名称
     * @return 字段信息列表
     */
    List<ColumnInfo> queryColumnInfo(String tableName, String tableSchema);

}

package com.gpdi.operatingunit.utils;

import com.gpdi.operatingunit.entity.system.SysReportColumn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 生成表头
 * @Author: Lxq
 * @Date: 2019/7/22 11:09
 */
public class GeneralReportColumn {


    /**
     * 通用生成表格表头
     *
     * @param columnList
     * @return
     */
    public static List<SysReportColumn> getReportTitleColumn(List<SysReportColumn> columnList) {
        Map<Integer, List<SysReportColumn>> map = new HashMap<>();
        List<SysReportColumn> result = new ArrayList<>();
        for (SysReportColumn sysReportColumn : columnList) {
            if (sysReportColumn.getLevel() == 2) {
                result.add(sysReportColumn);
            } else {
                if (map.get(sysReportColumn.getParentId()) == null) {
                    List<SysReportColumn> list = new ArrayList<>();
                    list.add(sysReportColumn);
                    map.put(sysReportColumn.getParentId(), list);
                } else {
                    map.get(sysReportColumn.getParentId()).add(sysReportColumn);
                }
            }
        }
        for (SysReportColumn sysReportColumn : result) {
            sysReportColumn.setChildren(map.get(sysReportColumn.getId()));
        }
        return result;
    }

}

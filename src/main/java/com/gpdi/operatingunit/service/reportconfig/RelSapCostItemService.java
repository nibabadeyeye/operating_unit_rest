package com.gpdi.operatingunit.service.reportconfig;

import java.util.List;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/10/25 16:49
 */
public interface RelSapCostItemService {

    /**
     * SAP科目与成本项目关系数据导入
     *
     * @param list     excel数据
     * @param reportId 报表id
     * @return 影响行数
     */
    int relSapCostItemUpload(List<Object> list, Integer reportId);
}

package com.gpdi.operatingunit.dao.reportconfig;

import com.gpdi.operatingunit.entity.common.SapList;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zyb
 * @Date: 2019/10/25 16:15
 */
@Repository
public interface SapListMapper {
    /**
     * 获取sap的字段名
     * @return
     */
    List<SapList> getSapNameList();
}

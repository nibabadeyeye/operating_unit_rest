package com.gpdi.operatingunit.service.system;

import com.gpdi.operatingunit.entity.system.SysDict;

import java.util.List;
import java.util.Map;

/**
 * @author Zhb
 * @date 2019/11/8 13:09
 **/
public interface SysDictService {

    /**
     * 根据cat查询字典数据
     *
     * @param cat 类型
     * @return 字典数据列表
     */
    List<SysDict> queryByCat(String cat);

    /**
     * 根据cat查询字典数据
     *
     * @param cat 类型
     * @return 字典数据map
     */
    Map<String,String> queryMapByCat(String cat);

}

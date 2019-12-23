package com.gpdi.operatingunit.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.system.SysDict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典表
 *
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-10-27 20:47:31
 */
@Repository
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * 获取条件列表
     *
     * @return
     */
    List<SysDict> getSelections();

    /**
     * 根据cat获取字典列表
     *
     * @param cat 类型
     * @return 字典数据列表
     */
    List<SysDict> queryByCat(@Param("cat") String cat);

}

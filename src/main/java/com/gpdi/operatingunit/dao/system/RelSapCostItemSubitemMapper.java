package com.gpdi.operatingunit.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.system.RelSapCostItemSubitem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-10-27 21:28:13
 */
@Repository
public interface RelSapCostItemSubitemMapper extends BaseMapper<RelSapCostItemSubitem> {

    /**
     * 根据parentId删除匹配好的条件
     * @param sid
     * @return
     */
    int deleteSubitemByParentId(@Param("sid") Long sid);

    /**
     * 根据父级id找子级id
     * @param costItemList
     * @return
     */
    List<Integer> getIdsByItemId(@Param("list") List<Integer> costItemList);


}

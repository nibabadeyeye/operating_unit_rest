package com.gpdi.operatingunit.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.business.DataDevelopment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevelopmentMapper extends BaseMapper<DataDevelopment> {

    /**
     * 导入Excel List集合
     */
    void insertExcelList(List<DataDevelopment> list);


    /**
     * 获取业务量发展报表月份倒序集合
     *
     * @return
     */
    List<Integer> monthList();
}

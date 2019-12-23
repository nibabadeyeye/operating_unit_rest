package com.gpdi.operatingunit.dao.business;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/11/28 10:51
 */
@Repository
public interface IncomeMapper {

    /**
     * 获取收入展示报表月份倒序集合
     *
     * @return
     */
    List<Integer> monthList();
}

package com.gpdi.operatingunit.service.home.impl;

import com.gpdi.operatingunit.dao.home.RsHomeNumberDataDao;
import com.gpdi.operatingunit.entity.home.RsHomeNumberData;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.home.MainService;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Zhb
 * @date 2019/12/16 10:47
 **/
@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private RsHomeNumberDataDao rsHomeNumberDataDao;

    @Override
    public Map<String, Object> getData(Integer month, boolean firstTime) {
        Map<String, Object> map = new HashMap<>(2);
        SysUser sysUser = ShiroUtils.getUser();
        List<Integer> months = new ArrayList<>();
        if (firstTime) {
            //第一次读取，获取月份数据
            months = getMonths(sysUser.getTopOrgCode());
            if (months.size() == 0) {
                month = DateUtils.toYearMonth(new Date());
                months.add(month);
            } else {
                month = months.get(0);
            }
        }
        map.put("months", months);
        map.put("numberData", getNumberDataByMonth(sysUser.getTopOrgCode(),
                sysUser.getOrgCode(), month));
        return map;
    }

    @Override
    public RsHomeNumberData getNumberDataByMonth(Long topOrgCode, Long orgCode, Integer month) {
        return rsHomeNumberDataDao.getNumberDataByMonth(topOrgCode, orgCode, month);
    }

    @Override
    public List<Integer> getMonths(Long topOrgCode) {
        return rsHomeNumberDataDao.getMonths(topOrgCode);
    }
}

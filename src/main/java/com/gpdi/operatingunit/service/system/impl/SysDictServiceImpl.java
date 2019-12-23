package com.gpdi.operatingunit.service.system.impl;

import com.gpdi.operatingunit.dao.system.SysDictMapper;
import com.gpdi.operatingunit.entity.system.SysDict;
import com.gpdi.operatingunit.service.system.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhb
 * @date 2019/11/8 13:14
 **/
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public List<SysDict> queryByCat(String cat) {
        return sysDictMapper.queryByCat(cat);
    }

    @Override
    public Map<String, String> queryMapByCat(String cat) {
        List<SysDict> sysDicts = queryByCat(cat);
        Map<String, String> map = new HashMap<>(sysDicts.size());
        for (SysDict sysDict : sysDicts) {
            map.put(sysDict.getValue(), sysDict.getText());
        }
        return map;
    }
}

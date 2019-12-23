package com.gpdi.operatingunit.service.system.impl;

import com.gpdi.operatingunit.service.system.SysCommonService;
import com.gpdi.operatingunit.service.system.support.ColumnInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zhb
 * @date 2019/10/30 10:57
 **/
@Service
public class SysCommonServiceImpl implements SysCommonService {

    @Override
    public List<ColumnInfo> queryColumnInfo(String tableName, String tableSchema) {

        return null;
    }

}

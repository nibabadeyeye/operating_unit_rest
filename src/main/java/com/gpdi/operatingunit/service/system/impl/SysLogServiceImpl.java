package com.gpdi.operatingunit.service.system.impl;

import com.gpdi.operatingunit.dao.system.SysLogMapper;
import com.gpdi.operatingunit.entity.system.SysLog;
import com.gpdi.operatingunit.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 系统日志 服务实现类
 * @Author: Lxq
 * @Date: 2019/10/21 11:33
 */
@Service
public class SysLogServiceImpl implements SysLogService {


    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void insert(SysLog sysLog) {
        sysLogMapper.insert(sysLog);
    }
}

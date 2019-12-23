package com.gpdi.operatingunit.service.quartz.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gpdi.operatingunit.dao.quartz.QuartzJobMapper;
import com.gpdi.operatingunit.entity.quartz.QuartzJob;
import com.gpdi.operatingunit.service.quartz.QuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/10/28 17:20
 */
@Service
@Transactional
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private QuartzJobMapper quartzJobMapper;

    @Override
    public List<QuartzJob> queryActiveQuartzJob() {
        QueryWrapper<QuartzJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return quartzJobMapper.selectList(queryWrapper);
    }

    @Override
    public List<QuartzJob> queryQuartzJob() {
        QueryWrapper<QuartzJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        return quartzJobMapper.selectList(queryWrapper);
    }

    @Override
    public int updateQuartzJobStatus(QuartzJob quartzJob) {
        return quartzJobMapper.updateById(quartzJob);
    }

    @Override
    public QuartzJob getQuartzJobById(Integer id) {
        return quartzJobMapper.selectById(id);
    }

}

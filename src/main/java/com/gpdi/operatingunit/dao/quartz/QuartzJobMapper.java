package com.gpdi.operatingunit.dao.quartz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.quartz.QuartzJob;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/10/28 17:21
 */
@Repository
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {
}

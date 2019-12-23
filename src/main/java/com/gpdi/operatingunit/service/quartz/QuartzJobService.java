package com.gpdi.operatingunit.service.quartz;

import com.gpdi.operatingunit.entity.quartz.QuartzJob;

import java.util.List;

/**
 * @description:
 * @author: Lxq
 * @date: 2019/10/28 17:18
 */
public interface QuartzJobService {

    /**
     * 获取开启了的定时任务
     *
     * @return
     */
    List<QuartzJob> queryActiveQuartzJob();

    /**
     * 获取所有定时任务
     *
     * @return
     */
    List<QuartzJob> queryQuartzJob();

    /**
     * 修改定时任务状态
     *
     * @param quartzJob
     * @return
     */
    int updateQuartzJobStatus(QuartzJob quartzJob);

    /**
     * 根据id查找定时任务
     *
     * @param id
     * @return
     */
    QuartzJob getQuartzJobById(Integer id);

}

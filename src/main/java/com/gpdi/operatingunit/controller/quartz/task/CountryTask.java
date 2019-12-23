package com.gpdi.operatingunit.controller.quartz.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @description: 区县报表任务调度
 * @author: Lxq
 * @date: 2019/10/29 11:15
 */
public class CountryTask extends QuartzJobBean{
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("区县报表任务调度");
        System.out.println("动态的定时任务执行时间："+new Date().toLocaleString());
    }
}

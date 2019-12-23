package com.gpdi.operatingunit.controller.quartz;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.quartz.QuartzJob;
import com.gpdi.operatingunit.service.quartz.QuartzJobService;
import com.gpdi.operatingunit.utils.QuartzUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @description: 任务调度
 * @author: Lxq
 * @date: 2019/10/28 17:11
 */
@RestController
@RequestMapping("/quartz/")
@Api(description = "任务调度控制器")
public class QuartzController {

    /**
     * 注入任务调度
     */
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzJobService quartzJobService;


    @RequestMapping(value = "/getJobList", method = {RequestMethod.GET})
    @ApiOperation("查询所有任务列表信息")
    public R getJobList() {
        List<QuartzJob> quartzJobs = quartzJobService.queryQuartzJob();
        return R.ok(quartzJobs);
    }
    @RequestMapping("/createJob")
    public String createJob() {
        try {
            QuartzJob quartzBean = new QuartzJob();
            //进行测试所以写死
            quartzBean.setJobClass("com.gpdi.quartzTask.MyTask1");
            quartzBean.setJobName("test1");
            quartzBean.setCronExpression("*/1 * * * * ?");
            QuartzUtils.createScheduleJob(scheduler, quartzBean);
        } catch (ClassNotFoundException e) {
            System.out.println("定时任务类路径出错：请输入类的绝对路径");
        } catch (SchedulerException e) {
            System.out.println("创建定时任务出错：" + e.getMessage());
        }
        return "创建成功";
    }

    @RequestMapping(value = "/pauseJob", method = {RequestMethod.POST})
    @ApiOperation("停止定时任务")
    public R pauseJob(String jobName, Integer id) {
        try {
            // 暂停任务
            QuartzUtils.pauseScheduleJob(scheduler, jobName);
            // 修改状态值
            QuartzJob quartzJob = new QuartzJob();
            quartzJob.setId(id);
            quartzJob.setStatus(0);
            quartzJobService.updateQuartzJobStatus(quartzJob);
        } catch (Exception e) {
            return R.ok("error");
        }
        return R.ok("success");

    }


    @RequestMapping(value = "/resumJob", method = {RequestMethod.POST})
    public R resumJob(String jobName, Integer id, String jobClass, String cronExpression) {
        try {
            // 判断是否存在该定时任务
            Boolean flag = true;
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
            JobKey job = new JobKey(jobName);
            if (jobKeys.contains(job)) {
                flag = false;
            }
            if (flag) {
                // 创建新的任务
                QuartzJob quartzJob = new QuartzJob();
                quartzJob.setJobClass(jobClass);
                quartzJob.setJobName(jobName);
                quartzJob.setCronExpression(cronExpression);
                QuartzUtils.createScheduleJob(scheduler, quartzJob);
                quartzJob.setStatus(1);
                quartzJob.setId(id);
                quartzJobService.updateQuartzJobStatus(quartzJob);

            } else {
                // 重启
                QuartzUtils.resumeScheduleJob(scheduler, jobName);
                // 修改状态值
                QuartzJob quartzJob = new QuartzJob();
                quartzJob.setId(id);
                quartzJob.setStatus(1);
                quartzJobService.updateQuartzJobStatus(quartzJob);
            }

        } catch (Exception e) {
            return R.ok("error");
        }
        return R.ok("success");
    }

    @RequestMapping(value = "/runOnceJob", method = {RequestMethod.POST})
    public R runOnceJob(String jobName, String jobClass, String cronExpression,Integer id) {
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
            JobKey jobKey = new JobKey(jobName);
            if (jobKeys.contains(jobKey)){
                QuartzUtils.runOnce(scheduler, jobName);
            }else {
                QuartzJob quartzBean = new QuartzJob();
                quartzBean.setJobClass(jobClass);
                quartzBean.setJobName(jobName);
                quartzBean.setCronExpression(cronExpression);
                // 创建
                QuartzUtils.createScheduleJob(scheduler, quartzBean);
                // 停止
                QuartzUtils.pauseScheduleJob(scheduler,quartzBean.getJobName());
                //执行一次
                QuartzUtils.runOnce(scheduler, jobName);

            }
        } catch (Exception e) {
            return R.ok("error");
        }
        return R.ok("success");
    }

    @RequestMapping(value = "/updateJob", method = {RequestMethod.POST})
    public R updateJob(String jobName, Integer id, String jobClass, String cronExpression) throws SchedulerException {
        QuartzJob quartzJobById = quartzJobService.getQuartzJobById(id);
        boolean validExpression = CronExpression.isValidExpression(cronExpression);
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        if (validExpression) {
            JobKey jobKey = new JobKey(jobName);
            // 是否已经被创建
            if (jobKeys.contains(jobKey)){
                // 是否是运行状态
                QuartzJob quartzJob = new QuartzJob();
                quartzJob.setId(id);
                quartzJob.setJobName(jobName);
                quartzJob.setJobClass(jobClass);
                quartzJob.setCronExpression(cronExpression);
                if(quartzJobById.getStatus() == 1){
                    QuartzUtils.updateScheduleJob(scheduler,quartzJob);
                }else {
                    QuartzUtils.updateScheduleJob(scheduler,quartzJob);
                    QuartzUtils.pauseScheduleJob(scheduler,quartzJob.getJobName());
                }
                quartzJobService.updateQuartzJobStatus(quartzJob);
            }else {
                // 还没创建该任务，直接修改数据库数据
                QuartzJob quartzJob = new QuartzJob();
                quartzJob.setId(id);
                quartzJob.setCronExpression(cronExpression);
                quartzJobService.updateQuartzJobStatus(quartzJob);
            }
            return R.ok("success");
        }else {
            return R.ok("cornError");
        }
    }

}

package com.gpdi.operatingunit.entity.quartz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @description: 定时任务
 * @author: Lxq
 * @date: 2019/10/28 17:16
 */
@TableName("quartz_job")
public class QuartzJob {

    /**
     * 定时任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 定时任务名称
     */
    private String jobName;
    /**
     * 定时任务执行类
     */
    private String jobClass;
    /**
     * 定时任务状态
     */
    private Integer status;
    /**
     * 定时任务表达式
     */
    private String cronExpression;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}

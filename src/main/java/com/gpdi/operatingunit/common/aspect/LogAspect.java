package com.gpdi.operatingunit.common.aspect;

import com.google.gson.Gson;
import com.gpdi.operatingunit.common.annotation.Log;
import com.gpdi.operatingunit.entity.system.SysLog;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.system.SysLogService;
import com.gpdi.operatingunit.utils.HttpContextUtils;
import com.gpdi.operatingunit.utils.IPUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Description:系统日志，切面处理类
 * @Author: Lxq
 * @Date: 2019/10/21 11:31
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;


    @Pointcut("@annotation(com.gpdi.operatingunit.common.annotation.Log)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        saveSysLog(point, time);
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        Log log = method.getAnnotation(Log.class);
        if (log != null) {
            //注解上的描述
            sysLog.setOperType(log.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            if ("用户登录".equals(log.value())) {
                //只保存第一个参数
                String params = new Gson().toJson(args[0]);
                sysLog.setParams(params);
            } else {
                //保存全部参数
                String params = new Gson().toJson(args);
                sysLog.setParams(params);
            }
        } catch (Exception e) {

        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));

        //用户名
        SysUser sysUser = ShiroUtils.getUser();
        sysLog.setUserId(sysUser.getId());

        sysLog.setExecuteTime(time);
        sysLog.setOperTime(new Date());
        //保存系统日志
        sysLogService.insert(sysLog);
    }


}

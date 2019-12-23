package com.gpdi.operatingunit.exception;

import com.gpdi.operatingunit.config.R;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: Controller层的全局异常统一处理
 * @author: Lxq
 * @date: 2019/10/30 16:40
 */
@RestControllerAdvice
public class GlobalControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常
     */
    @ExceptionHandler(DefinitionException.class)
    public R handleRRException(DefinitionException e){
        Map<String,Object> result = new HashMap<>();
        result.put("code", e.getCode());
        result.put("msg", e.getMsg());
        R r = new R();
        r.setStatus(e.getCode());
        r.setMsg(e.getMessage());
        r.setData(result);
        return r;
    }


    @ExceptionHandler(AuthorizationException.class)
    public R handleAuthorizationException(AuthorizationException e){
        logger.error(e.getMessage(), e);
        return R.error("没有权限，请联系管理员授权");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public R handleUnauthorizedException(UnauthorizedException e){
        logger.error(e.getMessage(), e);
        return R.error("您没有当前操作的权限");
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        logger.error(e.getMessage(), e);
        Map<String,Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("msg", e.getMessage());
        return R.error("未知异常,请联系管理员",result);
    }
}

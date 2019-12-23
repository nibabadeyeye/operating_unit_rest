package com.gpdi.operatingunit.common.annotation;

import java.lang.annotation.*;

/**
 * @Description: 系统日志注解
 * @Author: Lxq
 * @Date: 2019/10/21 11:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    String value() default "";
}

package com.gpdi.operatingunit.datasources.annotation;

import java.lang.annotation.*;

/**
 * @Description: 多数据源注解
 * @Author: Lxq
 * @Date: 2019/10/18 11:10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default "";
}

package com.ducheng.multi.redis.annotation;

import com.ducheng.multi.redis.autoconf.MultiRedisProperties;

import java.lang.annotation.*;

/**
 * 自定义注解选择数据源
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RdbSelect {

    /**
     *  数据库配置数据源的名称
     * @return
     */
    String dataSource() default MultiRedisProperties.DEFAULT;

    /**
     * 默认数据库
     * @return
     */
    int db() default 0 ;
}

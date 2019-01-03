package com.weixiao.smart.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-01 20:53.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface HessianService {
    /**
     * hessian服务名称
     * @return
     */
    String name() default "";
}

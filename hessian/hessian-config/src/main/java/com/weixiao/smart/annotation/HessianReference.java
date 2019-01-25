package com.weixiao.smart.annotation;

import com.weixiao.smart.reference.HessianReferenceAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-04 08:51.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Import(HessianReferenceAnnotationBeanPostProcessor.class)
public @interface HessianReference {
    /**
     * hessian RPC url
     *
     * @return
     */
    String serviceUrl() default "";

    String interfaceName() default "";

    String name() default "";


}

package com.weixiao.smart.context.annotation;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;

import java.util.Set;

import static org.springframework.context.annotation.AnnotationConfigUtils.registerAnnotationConfigProcessors;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-04 10:34.
 */
public class HessianReferenceBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {


    public HessianReferenceBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
        registerAnnotationConfigProcessors(registry);
    }

    public HessianReferenceBeanDefinitionScanner(BeanDefinitionRegistry registry, Environment environment) {
        this(registry, false, environment);
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }
}

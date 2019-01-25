package com.weixiao.smart.register;

import com.weixiao.smart.context.annotation.HessianComponentScan;
import com.weixiao.smart.annotation.HessianService;
import com.weixiao.smart.context.annotation.PackagesToScanUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-02 23:44.
 */
@Deprecated
public class HessianComponentScanRegister2 implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Set<String> packagesToScan = PackagesToScanUtil.getPackagesToScan(importingClassMetadata);
        registerHessianServiceBean(packagesToScan, registry);
    }



    /**
     * initializer  HessianServiceBean
     * @see HessianService
     * @param packagesToScan
     * @param registry
     */
    private void registerHessianServiceBean(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
    }

}

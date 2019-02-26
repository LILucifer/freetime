package com.weixiao.smart.context.annotation;

import lombok.extern.slf4j.Slf4j;
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
 * @Created 2019-01-04 09:19.
 */
@Slf4j
public class HessianComponentScanRegister implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Set<String> packagesToScan = getPackagesToScan(annotationMetadata);
        log.info("packagesToScan = {}", packagesToScan);
    }
    /**
     *  get packages of component scan
     * @param annotationMetadata
     * @return
     */
    private Set<String> getPackagesToScan(AnnotationMetadata annotationMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(annotationMetadata.getAnnotationAttributes(HessianComponentScan.class.getName()));
        String[] basePackages = annotationAttributes.getStringArray("basePackages");
        Class<?>[] basePackageClasses = annotationAttributes.getClassArray("basePackageClasses");
        Set<String> packagesToScan = new LinkedHashSet<String>();
        packagesToScan.addAll(Arrays.asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        if (packagesToScan.isEmpty()) {
            return Collections.singleton(ClassUtils.getPackageName(annotationMetadata.getClassName()));
        }
        return packagesToScan;
    }
}

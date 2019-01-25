package com.weixiao.smart.context.annotation;

import com.weixiao.smart.reference.HessianReferenceAnnotationBeanPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
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
        Set<String> packagesToScan = PackagesToScanUtil.getPackagesToScan(annotationMetadata);
        log.info("packagesToScan = {}", packagesToScan);
        registerReferenceAnnotationBeanPostProcessor(beanDefinitionRegistry);
    }

    /**
     * Registers {@link HessianReferenceAnnotationBeanPostProcessor} into {@link BeanFactory}
     *
     * @param registry {@link BeanDefinitionRegistry}
     */
    private void registerReferenceAnnotationBeanPostProcessor(BeanDefinitionRegistry registry) {

        // Register @Reference Annotation Bean Processor
        registerInfrastructureBean(registry,
                HessianReferenceAnnotationBeanPostProcessor.BEAN_NAME, HessianReferenceAnnotationBeanPostProcessor.class);

    }

    /**
     * Register Infrastructure Bean
     *
     * @param beanDefinitionRegistry {@link BeanDefinitionRegistry}
     * @param beanType               the type of bean
     * @param beanName               the name of bean
     */
    public void registerInfrastructureBean(BeanDefinitionRegistry beanDefinitionRegistry,
                                                  String beanName,
                                                  Class<?> beanType) {

        if (!beanDefinitionRegistry.containsBeanDefinition(beanName)) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
        }

    }


}

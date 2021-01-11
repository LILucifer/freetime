package com.weixiao.smart.feigndefine.configure;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author lishixiang0925@126.com.
 * @description feign 相关注解自动装配能力
 * @Created 2021-01-12 06:51.
 */

public class FeignRegister implements ImportBeanDefinitionRegistrar , EnvironmentAware {

    //step1:扫描自定义注解的Bean 并获取使用注解的Bean 并获取注解上的属性
    //step2:为每个使用注解的Bean创建一个动态代理
    //step3:将创建动态代理的Bean注册到spring IOC 容器中

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //扫描目标注解(@FeignClient  @FeignGet) 并做相应的装载处理
        registerFeignRequest(registry);

    }

    private void registerFeignRequest(BeanDefinitionRegistry registry) {
        getScanner();
    }

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;
    }
}

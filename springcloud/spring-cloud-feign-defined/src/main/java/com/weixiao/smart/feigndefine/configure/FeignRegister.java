package com.weixiao.smart.feigndefine.configure;

import com.weixiao.smart.feigndefine.annotation.FeignClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Set;

/**
 * @author lishixiang0925@126.com.
 * @description feign 相关注解自动装配能力
 * @Created 2021-01-12 06:51.
 */

public class FeignRegister implements ImportBeanDefinitionRegistrar , EnvironmentAware  , ResourceLoaderAware , BeanClassLoaderAware , BeanFactoryAware {

    //step1:扫描自定义注解的Bean 并获取使用注解的Bean 并获取注解上的属性
    //step2:为每个使用注解的Bean创建一个动态代理
    //step3:将创建动态代理的Bean注册到spring IOC 容器中

    private Environment environment;
    private ResourceLoader resourceLoader;
    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //扫描目标注解(@FeignClient  @FeignGet) 并做相应的装载处理
        registerFeignRequest(registry);

    }

    private void registerFeignRequest(BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider classScanner = getScanner();
//        scanner.setEnvironment(environment);
        classScanner.setResourceLoader(resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(FeignClient.class);
        classScanner.addIncludeFilter(annotationTypeFilter);

        String basePackages = "com.weixiao.smart.feigndefine";
        Set<BeanDefinition> beanDefinitionSet = classScanner.findCandidateComponents(basePackages);

        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                String className = beanDefinition.getBeanClassName();
                AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                //创建代理Bean对象 并注册到Spring IOC 容器中
                ((DefaultListableBeanFactory)this.beanFactory).registerBeanDefinition(className , );

            }
        }


    }

    private ClassPathScanningCandidateComponentProvider getScanner() {

        return new ClassPathScanningCandidateComponentProvider(false, this.environment){
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                if(beanDefinition.getMetadata().isInterface()){
                    try {
                        Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(),classLoader);
                        return !target.isAnnotation();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return super.isCandidateComponent(beanDefinition);
            }
        };
    }

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

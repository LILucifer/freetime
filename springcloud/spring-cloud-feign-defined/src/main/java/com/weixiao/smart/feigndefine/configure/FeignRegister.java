package com.weixiao.smart.feigndefine.configure;

import com.weixiao.smart.feigndefine.annotation.FeignClient;
import com.weixiao.smart.feigndefine.annotation.FeignGet;
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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
        try {
            registerFeignRequest(registry);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void registerFeignRequest(BeanDefinitionRegistry registry)  throws ClassNotFoundException {
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
                ((DefaultListableBeanFactory)this.beanFactory).registerSingleton(className ,  createBeanDefinition(annotatedBeanDefinition));

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

    //穿件动态代理
    private Object createBeanDefinition(AnnotatedBeanDefinition annotatedBeanDefinition) throws ClassNotFoundException {
        AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
        String className = annotatedBeanDefinition.getBeanClassName();
        Class<?> target = Class.forName(metadata.getClassName());
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //通过获取注解
                FeignClient feignClient = target.getAnnotation(FeignClient.class);
                String baseUrl = feignClient.baseUrl();
                if (!StringUtils.isEmpty(baseUrl) && method.getAnnotation(FeignGet.class) != null) {
                    FeignGet feignGet = method.getAnnotation(FeignGet.class);
                    String url = feignGet.url();
                    String requesturl = baseUrl + url;
                    String result = new RestTemplate().getForObject(requesturl, String.class);
                    return result;
                }
                throw new IllegalAccessException("不符合要求");
            }
        };


        return Proxy.newProxyInstance(this.classLoader,new Class[]{target} , invocationHandler);
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

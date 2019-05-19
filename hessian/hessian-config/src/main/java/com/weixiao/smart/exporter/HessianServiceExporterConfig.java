package com.weixiao.smart.exporter;

import com.weixiao.smart.annotation.HessianService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (handler HessianService Export when bean was Initialization successful)
 * @Created 2019-01-03 11:18.
 */
@Slf4j
@Component
public class HessianServiceExporterConfig implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.beanFactory= (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(HessianService.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
            hessianServiceExporterRegisterHandler(entry);
        }
    }

    private void hessianServiceExporterRegisterHandler(Map.Entry<String, Object> entry) {
        Class<? extends Object> clazz = entry.getValue().getClass();
        Class<? extends Object>[] interfaces = clazz.getInterfaces();
        Class<?> interfaceClass = null;
        if (interfaces.length > 0) {
            interfaceClass = interfaces[0];
        }
        Assert.notNull(interfaceClass,
                "@HessianService interfaceClass() or interfaceName() or interface class must be present!");
        Assert.isTrue(interfaceClass.isInterface(),
                "The type that was annotated @HessianService is not an interface!");

        String hessianServiceName = null;
        HessianService hessianService = entry.getValue().getClass().getAnnotation(HessianService.class);
        if (hessianService != null) {
            hessianServiceName = hessianService.name();
            if (StringUtils.isEmpty(hessianServiceName)) {
                //replacing "Impl" with "" in the service name (entry.key)
                hessianServiceName = entry.getKey().replace("Impl", "");
            }
            hessianServiceName = "/" + hessianServiceName;
        }
        Assert.notNull(hessianServiceName,"hessianServiceName can not null!");

        registerExportBean(hessianServiceName ,entry.getValue(),interfaceClass);
    }

    /**
     * register the bean of HessianServiceProxyExporter in container
     * @param hessianServiceName
     * @param Service
     * @param interfaceClass
     */
    private void registerExportBean(String hessianServiceName ,Object Service, Class<?> interfaceClass) {
        AbstractBeanDefinition beanProxyDefinition = applicationContext.getBean(hessianServiceName,AbstractBeanDefinition.class);
        if (beanProxyDefinition == null) {//IOC容器中没有注册 hessianServiceName 对应的Bean
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(HessianServiceProxyExporter.class);
            beanProxyDefinition = beanDefinitionBuilder.getBeanDefinition();
            MutablePropertyValues propertyValues = new MutablePropertyValues();
            propertyValues.addPropertyValue("service", Service);
            propertyValues.addPropertyValue("serviceInterface", interfaceClass);
            beanProxyDefinition.setPropertyValues(propertyValues);
            //HessianServiceProxyExporter 注册到IOC容器中
            beanFactory.registerBeanDefinition(hessianServiceName,beanProxyDefinition);
        }
        log.info("HessianService was initialized : {}  {}", hessianServiceName, interfaceClass.getName());
    }
}

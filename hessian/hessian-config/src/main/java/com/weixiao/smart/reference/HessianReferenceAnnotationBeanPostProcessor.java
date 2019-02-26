package com.weixiao.smart.reference;

import com.weixiao.smart.annotation.HessianReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;

/**
 * @author lishixiang0925@126.com.
 * @description (初始化Hessian RPC service Proxy reference)
 * @Created 2019-01-04 09:01.
 */
@Slf4j
public class HessianReferenceAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
        implements MergedBeanDefinitionPostProcessor, PriorityOrdered, ApplicationContextAware, BeanFactoryAware, DisposableBean, EnvironmentAware {

    private static final String HESSIAN_SERVICE_URL = "hessian.config.serviceUrl";
    /**
     * The bean name of {@link HessianReferenceAnnotationBeanPostProcessor}
     */
    public static String BEAN_NAME = "hessianReferenceAnnotationBeanPostProcessor";


    private ApplicationContext applicationContext;
    private DefaultListableBeanFactory beanFactory;
    private ConfigurableEnvironment environment;
    private final ConcurrentMap<String, InjectionMetadata> injectionMetadataCache =
            new ConcurrentHashMap<String, InjectionMetadata>(256);

    private final ConcurrentMap<String, Object> referenceBeansCache =
            new ConcurrentHashMap<String, Object>();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory");
        }
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException {
        getServiceUrl();
        InjectionMetadata metadata = findReferenceMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of @HessianReference dependencies failed", ex);
        }
        return pvs;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (beanType != null) {
            getServiceUrl();
            InjectionMetadata metadata = findReferenceMetadata(beanName, beanType, null);
            metadata.checkConfigMembers(beanDefinition);
        }
    }

    private InjectionMetadata findReferenceMetadata(String beanName, Class<?> clazz, PropertyValues pvs) {
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
        // Quick check on the concurrent map first, with minimal locking.
        InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz)) {
            synchronized (this.injectionMetadataCache) {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                    if (metadata != null) {
                        metadata.clear(pvs);
                    }
                    try {
                        metadata = buildReferenceMetadata(clazz);
                        this.injectionMetadataCache.put(cacheKey, metadata);
                    } catch (NoClassDefFoundError err) {
                        throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() +
                                "] for reference metadata: could not find class that it depends on", err);
                    }
                }
            }
        }
        return metadata;
    }

    /**
     * @param beanClass
     * @return
     */
    private InjectionMetadata buildReferenceMetadata(final Class<?> beanClass) {

        final List<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();

        elements.addAll(findFieldReferenceMetadata(beanClass));

        elements.addAll(findMethodReferenceMetadata(beanClass));

        return new InjectionMetadata(beanClass, elements);

    }

    /**
     * Finds {@link InjectionMetadata.InjectedElement} Metadata from annotated {@link HessianReference @HessianReference} fields
     *
     * @param beanClass The {@link Class} of Bean
     * @return non-null {@link List}
     */
    private List<InjectionMetadata.InjectedElement> findFieldReferenceMetadata(final Class<?> beanClass) {

        final List<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();

        ReflectionUtils.doWithFields(beanClass, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

                HessianReference reference = findReferenceAnnotation(field);

                if (reference != null) {

                    if (Modifier.isStatic(field.getModifiers())) {
                        log.warn("@HessianReference annotation is not supported on static fields: " + field);
                        return;
                    }

                    elements.add(new ReferenceFieldElement(field, reference));
                }

            }
        });

        return elements;

    }


    /**
     * Finds {@link InjectionMetadata.InjectedElement} Metadata from annotated {@link HessianReference @Reference} methods
     *
     * @param beanClass The {@link Class} of Bean
     * @return non-null {@link List}
     */
    private List<InjectionMetadata.InjectedElement> findMethodReferenceMetadata(final Class<?> beanClass) {

        final List<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();

        ReflectionUtils.doWithMethods(beanClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {

                Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);

                if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
                    return;
                }

                HessianReference reference = findReferenceAnnotation(bridgedMethod);

                if (reference != null && method.equals(ClassUtils.getMostSpecificMethod(method, beanClass))) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        log.warn("@Reference annotation is not supported on static methods: " + method);
                        return;
                    }
                    if (method.getParameterTypes().length == 0) {
                        log.warn("@Reference  annotation should only be used on methods with parameters: " +
                                method);
                    }
                    PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, beanClass);
                    elements.add(new ReferenceMethodElement(method, pd, reference));
                }
            }
        });

        return elements;

    }

    private HessianReference findReferenceAnnotation(AccessibleObject accessibleObject) {

        if (accessibleObject.getAnnotations().length > 0) {
            HessianReference reference = getMergedAnnotation(accessibleObject, HessianReference.class);
            return reference;
        }

        return null;

    }

    /**
     * {@link HessianReference} {@link Field} {@link InjectionMetadata.InjectedElement}
     */
    private class ReferenceFieldElement extends InjectionMetadata.InjectedElement {

        private final Field field;

        private final HessianReference reference;

        protected ReferenceFieldElement(Field field, HessianReference reference) {
            super(field, null);
            this.field = field;
            this.reference = reference;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {

            Class<?> referenceClass = field.getType();
            String referenceName = field.getName();
            Object referenceBean = buildReferenceBean(referenceName, reference, referenceClass);

            ReflectionUtils.makeAccessible(field);

            field.set(bean, referenceBean);

        }

    }

    /**
     * {@link HessianReference} {@link Method} {@link InjectionMetadata.InjectedElement}
     */
    private class ReferenceMethodElement extends InjectionMetadata.InjectedElement {

        private final Method method;

        private final HessianReference reference;

        protected ReferenceMethodElement(Method method, PropertyDescriptor pd, HessianReference reference) {
            super(method, pd);
            this.method = method;
            this.reference = reference;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {

            Class<?> referenceClass = pd.getPropertyType();

            Object referenceBean = buildReferenceBean(beanName, reference, referenceClass);

            ReflectionUtils.makeAccessible(method);

            method.invoke(bean, referenceBean);

        }

    }


    private Object buildReferenceBean(String referenceBeanName, HessianReference reference, Class<?> referenceClass) throws Exception {

        //String referenceBeanCacheKey = generateReferenceBeanCacheKey(reference, referenceClass);

        String referenceBeanCacheKey = referenceClass.getName();

        Object referenceBean = referenceBeansCache.get(referenceBeanCacheKey);
        if (referenceBean == null) {


            String serviceUrlMethod = serviceUrl + "/" + referenceBeanName;
            try {
                referenceBean = applicationContext.
                        getBean(referenceBeanCacheKey);
            } catch (BeansException e) {
                if (e instanceof NoSuchBeanDefinitionException) {
                    registerExportBean(referenceBeanCacheKey, serviceUrlMethod, referenceClass);
                    referenceBean = applicationContext.
                            getBean(referenceBeanCacheKey);
                } else {
                    e.printStackTrace();
                }
            }
            referenceBeansCache.putIfAbsent(referenceBeanCacheKey, referenceBean);

        }


        return referenceBean;
    }

    /**
     * 获取 serviceURL
     */
    private static String serviceUrl = null;

    private void getServiceUrl() {
        if (StringUtils.isEmpty(serviceUrl)) {
            serviceUrl = "http://127.0.0.1:8080";
            MutablePropertySources sources = environment.getPropertySources();
            String temp = "applicationConfig: [classpath:/application.yaml]";

            Iterator<PropertySource<?>> iterator = sources.iterator();
            while (iterator.hasNext()) {
                PropertySource<?> source = iterator.next();
                if (source.getName().contains("applicationConfig") && source.containsProperty(HESSIAN_SERVICE_URL)) {
                    serviceUrl = String.valueOf(source.getProperty(HESSIAN_SERVICE_URL));
                    break;
                }
            }
        }
    }

    /**
     * Generate a cache key of {@link HessianReferenceProxyFactoryBean}
     *
     * @param reference {@link HessianReference}
     * @param beanClass {@link Class}
     * @return
     */
    private static String generateReferenceBeanCacheKey(HessianReference reference, Class<?> beanClass) {

        String interfaceName = resolveInterfaceName(reference, beanClass);

        String key = "/" + interfaceName;

        return key;

    }

    private static String resolveInterfaceName(HessianReference reference, Class<?> beanClass)
            throws IllegalStateException {

        String interfaceName;
        if (!"".equals(reference.interfaceName())) {
            interfaceName = reference.interfaceName();
        } else if (beanClass.isInterface()) {
            interfaceName = beanClass.getName();
        } else {
            throw new IllegalStateException(
                    "The @Reference undefined interfaceClass or interfaceName, and the property type "
                            + beanClass.getName() + " is not a interface.");
        }

        return interfaceName;

    }

    /**
     * register the bean of HessianReferenceProxyFactoryBean in container
     *
     * @param hessianReferenceBeanName
     * @param serviceUrl
     * @param interfaceClass
     */
    private void registerExportBean(String hessianReferenceBeanName, String serviceUrl, Class<?> interfaceClass) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(HessianReferenceProxyFactoryBean.class);
        AbstractBeanDefinition beanProxyDefinition = beanDefinitionBuilder.getBeanDefinition();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("serviceUrl", serviceUrl);
        propertyValues.addPropertyValue("serviceInterface", interfaceClass);
        beanProxyDefinition.setPropertyValues(propertyValues);
        beanFactory.registerBeanDefinition(hessianReferenceBeanName, beanProxyDefinition);
        log.info("HessianReference was initialized : beanName = {}  interfaceClass = {}", hessianReferenceBeanName, interfaceClass.getName());
    }


    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public void destroy() throws Exception {
        injectionMetadataCache.clear();
        referenceBeansCache.clear();
        log.info(getClass() + " was destroying!");

    }
}

package com.weixiao.smart.reference;

import com.weixiao.smart.HessianProperties;
import com.weixiao.smart.annotation.HessianReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
public class HessianReferenceProxyRegister extends InstantiationAwareBeanPostProcessorAdapter
        implements MergedBeanDefinitionPostProcessor, PriorityOrdered, ApplicationContextAware,BeanFactoryAware  , DisposableBean {
    @Autowired
    private HessianProperties hessianProperties;

    private ConfigurableListableBeanFactory beanFactory;

    private ApplicationContext applicationContext;
    private final ConcurrentMap<String, InjectionMetadata> injectionMetadataCache =
            new ConcurrentHashMap<String, InjectionMetadata>(256);

    private final ConcurrentMap<String, HessianReferenceProxyFactoryBean> referenceBeansCache =
            new ConcurrentHashMap<String, HessianReferenceProxyFactoryBean>();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory");
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException {

        InjectionMetadata metadata = findReferenceMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of @Reference dependencies failed", ex);
        }
        return pvs;
    }
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (beanType != null) {
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

            Object referenceBean = buildReferenceBean( beanName,reference, referenceClass);

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

            Object referenceBean = buildReferenceBean(beanName ,reference, referenceClass);

            ReflectionUtils.makeAccessible(method);

            method.invoke(bean, referenceBean);

        }

    }


    private Object buildReferenceBean(String referenceBeanName ,HessianReference reference, Class<?> referenceClass) throws Exception {

        String referenceBeanCacheKey = generateReferenceBeanCacheKey(reference, referenceClass);

        HessianReferenceProxyFactoryBean referenceBean = referenceBeansCache.get(referenceBeanCacheKey);

        if (referenceBean == null) {

            referenceBean = new HessianReferenceProxyFactoryBean();
            referenceBean.setServiceUrl(hessianProperties.getServiceUrl() + "/" + referenceBeanName);
            referenceBean.setServiceInterface(referenceClass);

            referenceBeansCache.putIfAbsent(referenceBeanCacheKey, referenceBean);

        }


        return referenceBean;
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






//
//    private ClassLoader classLoader;
//    private Environment environment;
//    private ResourceLoader resourceLoader;
//
//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
//        Set<String> packagesToScan = PackagesToScanUtil.getPackagesToScan(annotationMetadata);
//        registerHessianReferenceBean(packagesToScan, registry);
//
//
//    }
//
//    /**
//     * Registers Beans whose classes was annotated {@link com.weixiao.smart.annotation.HessianReference}
//     *
//     * @param packagesToScan The base packages to scan
//     * @param registry       {@link BeanDefinitionRegistry}
//     */
//    private void registerHessianReferenceBean(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
//        HessianReferenceBeanDefinitionScanner hessianReferenceScanner =
//                new HessianReferenceBeanDefinitionScanner(registry, environment);
//        hessianReferenceScanner.addIncludeFilter(new AnnotationTypeFilter(HessianReference.class));
//
//        for (String packageToScan : packagesToScan) {
//
//            Set<BeanDefinitionHolder> beanDefinitionHolders = hessianReferenceScanner.doScan(packageToScan);
//
//            for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
//                registerHessianBean(beanDefinitionHolder, registry);
//            }
//
//            log.info(beanDefinitionHolders.size() + " annotated @HessianReference Components { " +
//                    beanDefinitionHolders +
//                    " } were scanned under package[" + packageToScan + "]");
//        }
//
//    }
//
//    /**
//     * @param beanDefinitionHolder
//     * @param registry
//     */
//    private void registerHessianBean(BeanDefinitionHolder beanDefinitionHolder, BeanDefinitionRegistry registry) {
//
//    }
//
//    private AbstractBeanDefinition buildServiceBeanDefinition(Class<?> interfaceClass,
//                                                              String annotatedServiceBeanName) {
//        BeanDefinitionBuilder beanDefinitionBuilder = rootBeanDefinition(HessianReferenceProxyFactoryBean.class)
//                .addPropertyValue("serviceUrl", hessianProperties.getServiceUrl() + "/" + annotatedServiceBeanName)
//                .addPropertyValue("serviceInterface", interfaceClass);
//        return beanDefinitionBuilder.getBeanDefinition();
//
//    }
//
//
//    @Override
//    public void setBeanClassLoader(ClassLoader classLoader) {
//        this.classLoader = classLoader;
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.environment = environment;
//    }
//
//    @Override
//    public void setResourceLoader(ResourceLoader resourceLoader) {
//        this.resourceLoader = resourceLoader;
//    }


    //    private ApplicationContext applicationContext;
//    private Set<String> packagesToScan;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Map<String, Object> hessianReferenceBeanMap = applicationContext.getBeansWithAnnotation(HessianReference.class);
//        log.info("hessianReferenceBeanMap = {}", hessianReferenceBeanMap);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//        packagesToScan = PackagesToScanUtil.packagesToScan;
//    }
}

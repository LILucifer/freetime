package com.weixiao.smart.reference;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-04 11:24.
 */
public class HessianReferenceProxyFactoryBean extends HessianReferenceClientInterceptor implements FactoryBean<Object> {

    private Object serviceProxy;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
        this.setReadTimeout(30*1000);
    }

    @Override
    public Object getObject()  {
        return this.serviceProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return getServiceInterface();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}


package com.weixiao.smart.reference;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.remoting.caucho.HessianClientInterceptor;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-04 11:16.
 */
@Slf4j
public class HessianReferenceClientInterceptor extends HessianClientInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //log.info("hessian request Method =  {} , Arguments = {}", invocation.getMethod() , invocation.getArguments());
        if (invocation instanceof ReflectiveMethodInvocation) {
            ReflectiveMethodInvocation reflectiveMethodInvocation = (ReflectiveMethodInvocation) invocation;
            log.info("hessian request = {} {} ,  Arguments = {}" , reflectiveMethodInvocation.getProxy() ,
                    invocation.getMethod().getName(),invocation.getArguments());
        }
        Object invoke = super.invoke(invocation);
        String result = String.valueOf(invoke);
        log.info("hessian response = {}", result);
        return invoke;
    }

}

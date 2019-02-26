package com.weixiao.smart.reference;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
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
        log.info("start hessian RPC ---------");
        Object invoke = super.invoke(invocation);
        String result = String.valueOf(invoke);
        log.info("end hessian RPC ---------");
        return invoke;
    }

}

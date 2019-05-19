package com.weixiao.smart.aspect.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-04-20 10:24.
 */
@Aspect
@Slf4j
@Component
public class InfoHandlerAspect {

    @Pointcut("execution( * com.weixiao.smart.aspect.controller..*.*(..))")
    public void logAspect(){
        log.info("this is pointCut");
    }

    @Before("logAspect()")
    public  void doBefore(JoinPoint joinPoint){
        log.info("doBefore this method args = {}", joinPoint.getArgs());
    }

    @AfterReturning("execution(* com.weixiao.smart.aspect..*Service.*(..))")
    public void afterRun(){
        log.info("afterRun");
    }
}

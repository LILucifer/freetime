package com.weixiao.smart.aspect.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author lishixiang0925@126.com.
 * @description AOP测试，获取pointCut方法参数，方法名，类名
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
        log.info("doBefore");
        log.info("doBefore this method target = {}", joinPoint.getTarget());
        log.info("doBefore this method args = {}", joinPoint.getArgs());

    }
    @Around("execution(* com.weixiao.smart.aspect..*Service.*(..))")
    public void AroundRun(ProceedingJoinPoint joinPoint){
        log.info("AroundRun");
        try {
            log.info("AroundRun this method target = {}", joinPoint.getTarget().getClass().toString());
            log.info("AroundRun this method args = {}", joinPoint.getArgs());
            log.info("AroundRun this method result = {}", joinPoint.proceed().toString());
            log.info("AroundRun this method toShortString = {}", joinPoint.toShortString());
            log.info("AroundRun this method toShortString = {}", joinPoint.toLongString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @AfterReturning("execution(* com.weixiao.smart.aspect..*Service.*(..))")
    public void afterRun(JoinPoint joinPoint){
        log.info("afterRun");
    }
}

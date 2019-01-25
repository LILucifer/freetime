package com.weixiao.smart;

import com.caucho.hessian.client.HessianProxyFactory;
import com.weixiao.smart.annotation.HessianReference;
import com.weixiao.smart.context.annotation.HessianComponentScan;
import com.weixiao.smart.reference.HessianReferenceProxyFactoryBean;
import com.weixiao.smart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-12-31 22:02.
 */
@SpringBootApplication
@HessianComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public HessianReferenceProxyFactoryBean helloClient() {
        HessianReferenceProxyFactoryBean factory = new HessianReferenceProxyFactoryBean();
        factory.setServiceUrl("http://127.0.0.1:8080/userService");
        factory.setServiceInterface(IUserService.class);
        return factory;
    }

//    @HessianReference
//    private IUserService userService;


//    @Configuration
//    class HessianClientConfig {
//        @Bean
//        public  IUserService getHessian() throws Exception{
//            HessianProxyFactory hessianProxy = new HessianProxyFactory();
//            //是否支持重载方法
//            hessianProxy.setOverloadEnabled(true);
//            IUserService hessianService = (IUserService)hessianProxy.create(Class.forName("com.weixiao.smart.service.IUserService"),
//                    "http://localhost:8080/userService");
//            return hessianService;
//        }
//    }

}

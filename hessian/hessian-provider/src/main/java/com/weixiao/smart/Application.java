package com.weixiao.smart;

import com.weixiao.smart.exporter.HessianServiceProxyExporter;
import com.weixiao.smart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianServiceExporter;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-12-31 21:04.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Autowired
//    private IUserService userService;
//    @Bean(name = "/userService")
//    public HessianServiceExporter initHessian(){
//        HessianServiceExporter exporter = new HessianServiceProxyExporter();
//        exporter.setService(userService);
//        exporter.setServiceInterface(userService.getClass().getInterfaces()[0]);
//        return exporter;
//    }


}

package com.weixiao.smart.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-01-21 11:46.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        long starttime = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
        System.out.println("启动耗时："+(System.currentTimeMillis()-starttime));

        //珍珠奶茶
        Berverage berverage1 = new PearlsMilkTea();
        System.out.println(berverage1.getDescription() + " cost = " + berverage1.Cost());

        //绿茶加牛奶
        Berverage berverage = new GreenTea();
        berverage = new Milk(berverage);
        System.out.println(berverage.getDescription()+"cost = "+berverage.Cost());
    }
}

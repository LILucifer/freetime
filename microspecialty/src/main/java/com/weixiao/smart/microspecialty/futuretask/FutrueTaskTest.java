package com.weixiao.smart.microspecialty.futuretask;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-07-27 00:07.
 */
@Slf4j
public class FutrueTaskTest {

    public void tetRunable(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable executor ");
            }
        };
        runnable.run();//并未开启新的线程，而是普通的Runnable对象实例
    }

    public static void main(String[] args) {

    }
}

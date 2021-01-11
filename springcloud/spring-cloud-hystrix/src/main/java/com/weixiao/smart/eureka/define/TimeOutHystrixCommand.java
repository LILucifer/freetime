package com.weixiao.smart.eureka.define;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-10 00:10.
 */
public class TimeOutHystrixCommand {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        RandomTimeOutCommand randomTimeOutCommand = new RandomTimeOutCommand();

        //future 接收返回结果
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {

                return randomTimeOutCommand.run();
            }
        });

        //捕获超时断路
        Random random = new Random();
        int timeOut = random.nextInt(130);
        String result = null;
        try {
            result = future.get(timeOut, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            result = randomTimeOutCommand.fallBack();
        }

        System.out.printf("command result  = { %s } " , result);
        executorService.shutdown();

    }


    public static class RandomTimeOutCommand implements Command<String> {

        @Override
        public String run() {
            Random random = new Random();
            int sleepTime = random.nextInt(150);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "run succeed";
        }

        @Override
        public String fallBack() {
            return "响应超时了！！！";
        }
    }
}

package com.weixiao.smart.test.timer;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-22 14:53.
 */
public class Mytask extends TimerTask {
    @Override
    public void run() {
        System.out.println("taks excution time " + new Date());
    }
}

package com.weixiao.smart.microspecialty.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-04-05 16:20.
 */
public class ListBlockQueue {
    //存放队列数据
    private List<Object> list = new ArrayList<Object>();
    private ReentrantLock lock = new ReentrantLock();
    private Condition putCondition = lock.newCondition();
    private Condition takeCondition = lock.newCondition();



}

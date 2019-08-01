package com.weixiao.smart.microspecialty.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-08-01 07:56.
 */
public class SmartReentrantReadWriteLock {
    private AtomicInteger readCount = new AtomicInteger(0);
    private AtomicInteger writeCount = new AtomicInteger(0);
}

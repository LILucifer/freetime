package com.weixiao.smart.microspecialty.fork;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-07-25 22:34.
 */
public class SmartCountDownLatch {

    private Sync sync;

    public SmartCountDownLatch(int count) {
        sync = new Sync(count);
    }

    public void countDown() {
        sync.releaseShared(1);
    }
    public  void await(){
        sync.acquireShared(1);
    }

    class Sync extends AbstractQueuedSynchronizer{
        public Sync(int count) {
            setState(count);
        }

        //加锁
        @Override
        protected int tryAcquireShared(int arg) {
            //只有当state=1 时加锁成功
            return getState() == 0 ? 1 : -1;
        }

        //释放锁
        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;) {
                int state = getState();
                if (state == 0) {
                    return false;
                }
                int nextState = state - 1;
                //CAS 更新state的值
                if (compareAndSetState(state, nextState)) {
                    return nextState==0;
                }
            }
        }
    }
}

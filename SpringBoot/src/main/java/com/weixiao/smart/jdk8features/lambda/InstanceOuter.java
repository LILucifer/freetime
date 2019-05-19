package com.weixiao.smart.jdk8features.lambda;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-06 10:21.
 */
public class InstanceOuter {
    public InstanceOuter(int xx) { x = xx; }

    private int x;

    class InstanceInner {
        public void printSomething() {
            System.out.println("The value of x in my outer is " + x);
        }
    }
}


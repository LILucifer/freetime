package com.weixiao.smart.test.waitAndNotify;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-21 17:42.
 */
public class InheritableThreadLocalTest extends InheritableThreadLocal {


    @Override
    protected Object initialValue() {
        return "this is a test value for InhertableThreadLocal";
    }

    @Override
    protected Object childValue(Object parentValue) {
        return parentValue + "------";
    }
}




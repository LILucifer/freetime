package com.weixiao.smart.test;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-01-21 21:30.
 */
public class Milk extends CondimentDecorator {
    Berverage berverage;
    public Milk( Berverage berverage) {
        this.berverage = berverage;
    }

    public Milk() {
    }

    public String getDescription() {
        return berverage.getDescription()+"+milk";
    }

    public int Cost() {
        return berverage.Cost()+3;
    }
}

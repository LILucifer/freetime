package com.weixiao.smart.test;

/**
 * @author lishixiang@echinetech.com.
 * @description (饮料店抽象基类)
 * @Created 2018-01-21 21:07.
 */
public abstract class Berverage {//饮料店抽象基类
    public String description;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract int Cost();
}

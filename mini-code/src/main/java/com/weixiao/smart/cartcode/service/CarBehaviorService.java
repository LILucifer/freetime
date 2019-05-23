package com.weixiao.smart.cartcode.service;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/5/23 17:13
 */
public interface CarBehaviorService {
    /**
     * 车移动
     *
     * @param command 移动指令
     */
    void move(String command);

    /**
     * 获取车所在位置X坐标
     *
     * @return
     */
    int getPositionX();

    /**
     * 获取车所在位置Y轴坐标
     *
     * @return
     */
    int getPositionY();

    /**
     * 获取车的方向
     *
     * @return
     */
    String getOrientation();

}

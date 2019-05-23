package com.weixiao.smart.cartcode.model;

import static com.weixiao.smart.cartcode.constants.ThresholdConstant.THRESHOLD_X;
import static com.weixiao.smart.cartcode.constants.ThresholdConstant.THRESHOLD_Y;

/**
 * @author lishixiang
 * @Title:
 * @Description: car object
 * @date 2019/5/23 17:23
 */
public class Car {
    private int positionX = 1;
    private int positionY = 1;
    private String orientation;

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        if (positionX <= THRESHOLD_X) {
            throw new RuntimeException("X 坐标方位越界！请调整方向后再前进");
        }
        this.positionX = positionX;
    }

    public int getPositionY() {
        if (positionY <= THRESHOLD_Y) {
            throw new RuntimeException("X 坐标方位越界！请调整方向后再前进");
        }
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    public void reSet() {
        this.positionX = 1;
        this.positionX = 1;
    }

    @Override
    public String toString() {
        return "Car{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", orientation='" + orientation + '\'' +
                '}';
    }
}

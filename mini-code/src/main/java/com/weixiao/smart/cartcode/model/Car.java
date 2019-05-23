package com.weixiao.smart.cartcode.model;

/**
 * @author lishixiang
 * @Title:
 * @Description: car object
 * @date 2019/5/23 17:23
 */
public class Car {
    private int positionX;
    private int positionY;
    private String orientation;

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
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

    @Override
    public String toString() {
        return "Car{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", orientation='" + orientation + '\'' +
                '}';
    }
}

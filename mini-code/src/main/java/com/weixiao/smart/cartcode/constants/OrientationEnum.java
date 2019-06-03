package com.weixiao.smart.cartcode.constants;

/**
 * @author lishixiang0925@126.com.
 * @description 方位与其方向移动的坐标
 * @Created 2019-05-23 19:51.
 */
public enum OrientationEnum {
    //Y position
    NORTH("north", 1),
    SOUTH("south", -1),

    //X position
    EAST("east", 1),
    WEST("west", -1),;
    private String orientation;
    private int movePosition;
    private OrientationEnum clockwise;


    public String getOrientation() {
        return orientation;
    }

    public int getMovePosition() {
        return movePosition;
    }

    public OrientationEnum getClockwise() {
        return clockwise;
    }

    OrientationEnum(String orientation, int movePosition) {
        this.orientation = orientation;
        this.movePosition = movePosition;
    }
}

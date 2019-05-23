package com.weixiao.smart.cartcode.service.impl;

import com.weixiao.smart.cartcode.constants.OrientationEnum;
import com.weixiao.smart.cartcode.model.Car;
import com.weixiao.smart.cartcode.service.CarBehaviorService;

import static com.weixiao.smart.cartcode.constants.OrientationEnum.*;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/5/23 17:21
 */
public abstract class AbstractCarBehaviorService implements CarBehaviorService {
    private Car car;

    public AbstractCarBehaviorService(Car car) {
        this.car = car;
    }

    public AbstractCarBehaviorService() {
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    @Override
    public void move(String command) {
        if (car == null) {
            throw new RuntimeException("car could not be null!");
        }
        if ("clockwise".equals(command)) {
            clockwiseRotation();
        } else if ("moveForward".equals(command)) {
            moveForward();
        } else {
            throw new RuntimeException("command was wrong!");
        }

    }

    /**
     * 顺时针旋转
     */
    private void clockwiseRotation() {
        switch (car.getOrientation()) {
            case "north":
                car.setOrientation(EAST.getOrientation());
                break;
            case "south":
                car.setOrientation(WEST.getOrientation());
                break;
            case "east":
                car.setOrientation(SOUTH.getOrientation());
                break;
            case "west":
                car.setOrientation(NORTH.getOrientation());
                break;

        }
    }

    /**
     * 向前移动
     */
    private void moveForward() {
        for (OrientationEnum anEnum : OrientationEnum.values()) {
            if (anEnum.getOrientation().equals(car.getOrientation())) {
                if (NORTH.getOrientation().equals(car.getOrientation()) ||
                        SOUTH.getOrientation().equals(car.getOrientation())) {
                    //Y 方向移动
                    car.setPositionY(car.getPositionY() + anEnum.getMovePosition());
                } else {
                    //X 方向移动
                    car.setPositionX(car.getPositionX() + anEnum.getMovePosition());
                }
            }
        }
    }

    @Override
    public int getPositionX() {
        if (car == null) {
            throw new RuntimeException("car could not be null!");
        }
        return car.getPositionX();
    }

    @Override
    public int getPositionY() {
        if (car == null) {
            throw new RuntimeException("car could not be null!");
        }
        return car.getPositionY();
    }

    @Override
    public String getOrientation() {
        if (car == null) {
            throw new RuntimeException("car could not be null!");
        }
        return car.getOrientation();
    }


}

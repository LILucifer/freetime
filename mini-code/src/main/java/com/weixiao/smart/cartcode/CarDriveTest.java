package com.weixiao.smart.cartcode;

import com.weixiao.smart.cartcode.model.Car;
import com.weixiao.smart.cartcode.service.CarBehaviorService;
import com.weixiao.smart.cartcode.service.impl.AbstractCarBehaviorService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lishixiang0925@126.com.
 * @description 测试
 * @Created 2019-05-23 20:28.
 */
@Slf4j
public class CarDriveTest {

    public static void main(String[] args) {
        //初始化car方位 orientation = north ;  X = 1 ;Y = 1
        Car car = new Car();
        car.setOrientation("north");
        CarBehaviorService carBehaviorService = new AbstractCarBehaviorService() {
        };
        carBehaviorService.setCar(car);

        //step1:顺时针旋转
        carBehaviorService.move("clockwise");
        log.info("positionX = {} , positionY = {} , orientation = {}",
                carBehaviorService.getPositionX(), carBehaviorService.getPositionY(), carBehaviorService.getOrientation());

        //step2:向前移动
        //复位 north
        car.setOrientation("north");
        car.reSet();
        carBehaviorService.move("moveForward");
        log.info("positionX = {} , positionY = {} , orentation = {}",
                carBehaviorService.getPositionX(), carBehaviorService.getPositionY(), carBehaviorService.getOrientation());

        //step3:向前移动
        //复位 east
        car.setOrientation("east");
        car.reSet();
        carBehaviorService.move("moveForward");
        log.info("positionX = {} , positionY = {} , orentation = {}",
                carBehaviorService.getPositionX(), carBehaviorService.getPositionY(), carBehaviorService.getOrientation());

        //step4:向前移动
        //复位 west
        try {
            car.setOrientation("west");
            car.reSet();
            carBehaviorService.move("moveForward");
            log.info("positionX = {} , positionY = {} , orentation = {}",
                    carBehaviorService.getPositionX(), carBehaviorService.getPositionY(), carBehaviorService.getOrientation());
        } catch (Exception e) {
            log.info("",e);
        }

        //step5:向前移动
        //复位 east
        car.setOrientation("east");
        car.reSet();
        carBehaviorService.move("moveForward");
        carBehaviorService.move("moveForward");
        log.info("positionX = {} , positionY = {} , orentation = {}",
                carBehaviorService.getPositionX(), carBehaviorService.getPositionY(), carBehaviorService.getOrientation());
    }
}

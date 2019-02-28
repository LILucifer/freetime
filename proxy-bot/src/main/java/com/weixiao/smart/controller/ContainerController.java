package com.weixiao.smart.controller;

import com.weixiao.smart.container.ContainerUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-09 17:57.
 */
@RestController
@RequestMapping("/container")
public class ContainerController {

    @RequestMapping("/targetContainerInfo")
    public ConcurrentHashMap getTargetContainerInfo(){
        return ContainerUtils.getControllerMap();
    }

}

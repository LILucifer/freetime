package com.weixiao.smart.service.impl;

import com.weixiao.smart.annotation.HessianService;
import com.weixiao.smart.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-12-30 11:26.
 */
@Slf4j
@HessianService(name = "userService")
public class UserServiceImpl implements IUserService{
    @Override
    public String getUserName(String userId) {
        log.info("this method is IUserService.getUserName");
        return "i am nick";
    }

}

package com.weixiao.smart.ebidding.configure;

import com.weixiao.smart.ebidding.entity.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lishixiang0925@126.com.
 * @description (全局异常处理)
 * @Created 2019-03-01 21:06.
 */
@ControllerAdvice(basePackages = "com.weixiao.smart.ebidding.controller")
public class GlobalExceptionHandler {


    /**
     * 默认全局异常处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result globalExceptionHandler(Exception e) {
        e.printStackTrace();
        //自定义异常处理
        return Result.builder().message("test Exception").resCode("00000").build();
    }


}

package com.weixiao.smart.ebidding;

import com.weixiao.smart.ebidding.rabbitmq.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(程序主入口)
 * @date 2019/2/25 15:23
 */
@Slf4j
@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}

package com.weixiao.smart.Main;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(自动发布服务)
 * @date 2018/10/11 14:59
 */
//@Component
//@EnableScheduling
public class AutoStart {

    /**
     * 定时任务检查服务重启
     */
    @Scheduled(cron = "0 0/2 * * * ? ")
    public void checkRestart(){
        System.out.println("checkRestart at " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
        System.exit(0);

    }

    /**
     * 服务重启
     */
    @PreDestroy
    public void reStart(){
        System.out.println("gdsp was stoped");
    }

    @PostConstruct
    public void construct(){
        System.out.println("gdsp was constructed");
    }
}

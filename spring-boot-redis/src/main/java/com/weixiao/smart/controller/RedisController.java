package com.weixiao.smart.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.weixiao.smart.redis.jedis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/2/21 0:12
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;
    private final String STUDENT_KEY = "STUDENT_KEY";
    private final String STUDENT_JEDIS_KEY = "STUDENT_JEDIS_KEY";
    @RequestMapping("/getStudent")
    public String getStudent(){
        //此处许多业务处理.....
        Object redisValue = redisTemplate.opsForValue().get(STUDENT_KEY);
        //检查Redis缓存中是否有值,有则直接返回。无则初始化后存入Redis
        if (redisValue == null) {
            String studentInfo = "spring-boot-data-redis {name:tom , age:18 , nickName:hello} ";
            redisTemplate.opsForValue().set(STUDENT_KEY, studentInfo ,600, TimeUnit.SECONDS);
            return studentInfo;
        }else{
            //此处许多业务处理.....
            return "student info from redis " + redisValue.toString();
        }
    }

    @RequestMapping("/getStudentFromJedis")
    public String getStudentFromJedis(){
        //此处许多业务处理.....
        String redisValue = RedisUtils.get(STUDENT_JEDIS_KEY);
        //检查Redis是否有值,有则直接返回。无则初始化后存入Redis
        if (redisValue == null) {
            String studentInfo = "Jedis {name:tom , age:18 , nickName:hello} ";
            RedisUtils.set(STUDENT_JEDIS_KEY, studentInfo,600);
            return studentInfo;
        }else{
            //此处许多业务处理.....
            return "student info from redis(jedis) " + redisValue;
        }
    }


    private final String SESSION_ID = "SESSION_ID";
    @RequestMapping("/setSession")
    public String setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute(SESSION_ID, 1000);
            return session.getId() + " set SESSION_ID = 1000 succeed";
        }
        return "session is null";
    }

    @RequestMapping("/getSession")
    public String getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            return session.getId()+" get SESSION_ID = "+session.getAttribute("SESSION_ID").toString();
        }
        return null;
    }
}

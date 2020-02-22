package com.weixiao.smart.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/2/14 11:22
 */
@Component
@Data
@ConfigurationProperties(prefix = "jedis")
public class JedisProperties {
    //redis 集群节点
    private String nodes;
    //redis 连接超时时间
    private int connectionTimeout;
    private int soTimeout;
    private String password;
    //连接池中最大连接数
    private int maxTotal;
    //连接池中的最大空闲连接
    private int maxIdle;
    //连接池中最小空闲连接
    private int minIdle;
    private int maxAttempts;
    private int maxWaitMillis;
    private int minEvictableIdleTimeMillis;
}

package com.weixiao.smart.configuration;

import com.weixiao.smart.configuration.properties.JedisProperties;
import com.weixiao.smart.redis.jedis.RedisUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/2/20 16:59
 */
@Configuration
public class JedisConfiguration {


    @Autowired
    private JedisProperties jedisProperties;
    @Bean
    public JedisCluster intJedisCluster(){
        //读取yaml 配置文件中的Redis 集群节点配置
        String[] serverArray = jedisProperties.getNodes().split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        //连接池参数设置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(jedisProperties.getMaxIdle());
        poolConfig.setMaxTotal(jedisProperties.getMaxTotal());
        poolConfig.setMinIdle(jedisProperties.getMinIdle());
        poolConfig.setMaxWaitMillis(jedisProperties.getMaxWaitMillis());
        poolConfig.setMinEvictableIdleTimeMillis(jedisProperties.getMinEvictableIdleTimeMillis());
        //初始化jedis 客户端
        JedisCluster jedisCluster = new JedisCluster(nodes ,
                jedisProperties.getConnectionTimeout(),
                jedisProperties.getSoTimeout(),
                jedisProperties.getMaxAttempts(),poolConfig);
        return jedisCluster;
    }
    /**
     * 初始化RedisUtils 工具类
     * @return
     */
    @Bean
    public RedisUtils initRedisUtil(){
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.setJedisCluster(intJedisCluster());
        redisUtils.init();
        return redisUtils;
    }
}

package com.weixiao.smart.configuration;

import com.weixiao.smart.configuration.properties.JedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/2/18 11:00
 */
@Configuration
@EnableRedisHttpSession
public class RedisHttpSessionConfiguration {

    @Autowired
    private JedisProperties jedisProperties;


    @Bean
    public JedisConnectionFactory connectionFactory() {
        String[] serverArray = jedisProperties.getNodes().split(",");
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(Arrays.asList(serverArray));

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(jedisProperties.getMaxIdle());
        poolConfig.setMaxTotal(jedisProperties.getMaxTotal());
        poolConfig.setMinIdle(jedisProperties.getMinIdle());
        poolConfig.setMaxWaitMillis(jedisProperties.getMaxWaitMillis());
        poolConfig.setMinEvictableIdleTimeMillis(jedisProperties.getMinEvictableIdleTimeMillis());
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, poolConfig);
        return jedisConnectionFactory;
    }


}

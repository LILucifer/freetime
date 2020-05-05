package com.weixiao.smart.configuration;

import com.weixiao.smart.configuration.properties.JedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;

/**
 * @author lishixiang
 * @Title:
 * @Description: redis 相关初始化
 * @date 2020/2/18 11:00
 */
@Configuration
@EnableRedisHttpSession //开启spring session + redis   org.springframework.session.data.redis.RedisOperationsSessionRepository
@EnableCaching //开启spring cache注解功能  org.springframework.cache.jcache.interceptor.CacheResultInterceptor
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

    /**
     * 配置Spring Cache注解功能
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
        return cacheManager;
    }


    /**
     * 可以替换Spring boot RedisTemplate bean
     * @param redisConnectionFactory
     * @return
     */
    @Bean("stringKeyRedisTemplate")
    public RedisTemplate initRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 可以配置对象的转换规则，比如使用json格式对object进行存储。
        // Object --> 序列化 --> 二进制流 --> redis-server存储
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }




}

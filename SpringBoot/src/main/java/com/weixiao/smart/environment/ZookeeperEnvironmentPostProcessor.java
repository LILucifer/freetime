package com.weixiao.smart.environment;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description 增强扩展Spring功能，让Spring启动支持读取Zookeeper配置信息到Environment
 * @Created 2019-11-30 11:41.
 */
@Slf4j
public class ZookeeperEnvironmentPostProcessor implements EnvironmentPostProcessor {


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String zookeeperUrl = environment.getProperty("zookeeper.url");
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zookeeperUrl, new RetryOneTime(2000));
        //启动与zookeeper连接
        zkClient.start();
        log.info("this application Function expanded  at Environment !");
        try {
            Map<String, Object> configMap = new HashMap<>();
            //step1:读取zookeeper 中的zk_config节点中的配置信息
            List<String> configurationPaths = zkClient.getChildren().forPath("/zk_config");
            for (String path : configurationPaths) {
                byte[] value = zkClient.getData().forPath("/zk_config/" + path);
                configMap.put(path, new String(value));
            }
            //step2:将zookeeper 读取到的配置信息 加载到Spring Environment 中
            MapPropertySource propertySource = new MapPropertySource("zkConfigurations", configMap);
            environment.getPropertySources().addLast(propertySource);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

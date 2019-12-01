package com.weixiao.smart.environment;

import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author lishixiang0925@126.com.
 * @description 监听zookeeper 中zk_config 节点下的变更并修改
 * @Created 2019-11-30 17:15.
 */
@Component
@Data
public class ZookeeperConfigWatch {

    @Value("${zookeeper.url}")
    private String zookeeperUrl;

    private List<ConfigurationCenterUpdate> configurationCenterUpdates;

    public ZookeeperConfigWatch(List<ConfigurationCenterUpdate> configurationCenterUpdates) {
        this.configurationCenterUpdates = configurationCenterUpdates;
    }

    /**
     * 加载此Bean时初始化 zookeeper 监听器
     */
    @PostConstruct
    public void initWatch() {
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zookeeperUrl, new RetryOneTime(2000));
        //启动与zookeeper连接
        zkClient.start();

        try {
            //zookeeper 监听器
            TreeCache treeCache = new TreeCache(zkClient, "/zk_config");
            treeCache.start();
            treeCache.getListenable().addListener(new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                    switch (event.getType()) {
                        case NODE_UPDATED:
                            //修改对应的Bean属性
                            //Bean 实现修改配置信息的接口 updateProperties(String configName , String newValue)
                            //updateProperties : 1、识别Bean，存在则修改，不存在忽略 （这样就可以做到统配修改所有的配置信息了）
                            for (ConfigurationCenterUpdate configurationCenterUpdate : configurationCenterUpdates) {
                                configurationCenterUpdate.updateProperties(event.getData().getPath(), new String(event.getData().getData()));
                            }
                            break;
                        default:
                            break;

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

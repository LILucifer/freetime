package com.weixiao.smart.distribution.lock;

import com.weixiao.smart.environment.ConfigurationCenterUpdate;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author lishixiang0925@126.com.
 * @description zookeeper实现分布式锁---惊群效应尚待解决
 * @Created 2019-12-05 22:15.
 */
@Slf4j
public class DistributionLock implements Lock {


    private CuratorFramework zkClient;
    private TreeCache treeCache;
    private String lockPath;
    private static String lockPathValue = "distribution_value";



    public DistributionLock(String lockPath) {
        if (lockPath == null || lockPath == "") {
            throw new RuntimeException("distributionLock lockPath don't allowed value was null !");
        }
        this.lockPath = "/distribution/"+lockPath;
        try {
            zkClient = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new RetryOneTime(2 * 1000));
            zkClient.start();
            //zookeeper 监听器
            treeCache = new TreeCache(zkClient, this.lockPath);
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            //获取锁失败 阻塞进入等待
            waitForLock();
            lock();
        }
    }

    private void waitForLock(){
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            TreeCacheListener treeCacheListener = new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                    //log.info("path = {}  value()", event.getData().getPath(),new String(event.getData().getData()));
                    switch (event.getType()) {
                        case NODE_REMOVED:
                            countDownLatch.countDown();
                            break;
                        default:
                            break;

                    }
                }
            };
            treeCache.getListenable().addListener(treeCacheListener);
            Stat stat = zkClient.checkExists().forPath(lockPath);
            if (stat != null) {
                //节点存在 ,则阻塞
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            treeCache.getListenable().removeListener(treeCacheListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            //创建临时节点
            zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(lockPath, lockPathValue.getBytes());
        } catch (KeeperException.NodeExistsException e){
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            //节点创建失败 等待锁
            return false;
        }
        //节点创建成功 获取锁
        return true;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            zkClient.delete().forPath(lockPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}

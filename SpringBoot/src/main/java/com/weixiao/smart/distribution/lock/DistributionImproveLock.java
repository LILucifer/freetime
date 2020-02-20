package com.weixiao.smart.distribution.lock;

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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author lishixiang0925@126.com.
 * @description zookeeper实现分布式锁---解决好了惊群效应  还有一个问题需解决：可重入锁的问题
 * <p>
 * 利用临时顺序节点来实现分布式锁
 * 获取锁：取排队号（创建自己的临时顺序节点），然后判断自己是否是最小号，如是，则获得锁；不是，则注册前一节点的watcher,阻塞等待
 * 释放锁：删除自己创建的临时顺序节点
 * @Created 2019-12-05 22:15.
 */
@Slf4j
public class DistributionImproveLock implements Lock {


    private CuratorFramework zkClient;
    private TreeCache treeCache;
    private String lockPath;
    private String currentPath;
    private String beforePath;
    private static String lockPathValue = "distribution_value";


    public DistributionImproveLock(String lockPath) {
        if (lockPath == null || lockPath == "") {
            throw new RuntimeException("distributionLock lockPath don't allowed value was null !");
        }
        this.lockPath = "/distribution/" + lockPath;
        try {
            zkClient = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new RetryOneTime(2 * 1000));
            zkClient.start();

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

    private void waitForLock() {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            //zookeeper 监听器
            if (treeCache == null) {
                treeCache = new TreeCache(zkClient, this.beforePath);
                treeCache.start();
            }
            TreeCacheListener treeCacheListener = new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                    //log.info("path = {}  value()", event.getData().getPath(),new String(event.getData().getData()));
                    switch (event.getType()) {
                        case NODE_REMOVED:
                            log.info("临时节点删除了-----=-");
                            countDownLatch.countDown();
                            break;
                        default:
                            break;

                    }
                }
            };
            treeCache.getListenable().addListener(treeCacheListener);
            Stat stat = zkClient.checkExists().forPath(beforePath);
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
            if (this.currentPath == null) {
                currentPath = zkClient.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(lockPath+"/", lockPathValue.getBytes());
            }
            //获取所有子节点
            List<String> lockPathChild = zkClient.getChildren().forPath(this.lockPath);
            Collections.sort(lockPathChild);

            //判断当前节点是否是最小的
            if (currentPath.equals(lockPath + "/" + lockPathChild.get(0))) {
                return true;
            }else {
                //获取前一个节点信息，
                // 得到字节的索引号
                int curIndex = lockPathChild.indexOf(currentPath.substring(lockPath.length() + 1));
                beforePath = lockPath + "/" + lockPathChild.get(curIndex - 1);
            }
            return false;

        } catch (KeeperException.NodeExistsException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            //节点创建失败 等待锁
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            zkClient.delete().forPath(currentPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}

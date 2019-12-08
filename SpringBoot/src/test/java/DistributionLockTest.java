import com.weixiao.smart.SpringBootApplication;
import com.weixiao.smart.distribution.lock.DistributionImproveLock;
import com.weixiao.smart.distribution.lock.DistributionLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-12-07 16:28.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SpringBootApplication.class})
public class DistributionLockTest {


    @Test
    public void testDistributionLock() {

        int currency = 50;
        CountDownLatch countDownLatch = new CountDownLatch(currency);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(currency);
        for (int i = 0; i < currency; i++) {
            System.out.println(i);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                        //log.info("order number ---- {}", getOrderNo());
                        log.info("order number ---- {}", getOrderNoImprove());
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Map<String, Integer> orderMap = new HashMap<>();
    private Integer getOrderNo() {
        Lock distributionLock = new DistributionLock("orderMapNo");
        Integer orderInt = null;
        try {
            distributionLock.lock();
            orderInt = orderMap.get("orderNo");
            if (orderInt == null) {
                orderInt = 0;
            }else{
                orderInt += 1;
            }
            orderMap.put("orderNo", orderInt);
        } finally {
            distributionLock.unlock();
        }
        return orderInt;
    }
    private Integer getOrderNoImprove() {
        Lock distributionLock = new DistributionImproveLock("orderMapNo");
        Integer orderInt = null;
        try {
            distributionLock.lock();
            orderInt = orderMap.get("orderNo");
            if (orderInt == null) {
                orderInt = 0;
            }else{
                orderInt += 1;
            }
            orderMap.put("orderNo", orderInt);
        } finally {
            distributionLock.unlock();
        }
        return orderInt;
    }

}

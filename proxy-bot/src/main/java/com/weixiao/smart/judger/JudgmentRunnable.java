package com.weixiao.smart.judger;

import com.weixiao.smart.container.Constants;
import com.weixiao.smart.container.ContainerUtils;
import com.weixiao.smart.entity.ProxyIpModel;
import com.weixiao.smart.quartz.ProxyIpJsoupQuartz;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author lishixiang0925@126.com.
 * @description (根据判定规则验证proxy IP 是否可用)
 * @Created 2019-01-18 10:39.
 */
@Slf4j
public class JudgmentRunnable implements Runnable {


    private ProxyIpModel model;
    private CountDownLatch countDownLatch;

    public JudgmentRunnable(ProxyIpModel model, CountDownLatch countDownLatch) {
        this.model = model;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        if (ProxyIpJsoupQuartz.isNeedClosePreTask) {
            countDownLatch.countDown();
            return;
        }
        method();
        countDownLatch.countDown();
        //conditionAwait();

    }

    public void method() {

        int i = 1;
        while (i <= Constants.FIRST_CHECK_TIME) {
            executedJudgment();
            i++;
        }
        if (verifySuccess(3)) {
            ContainerUtils.addControllerMap(model.getIp(), model);
            log.info("id : {}  was successed", this.model.getIp());
            return;
        }

        while (i <= Constants.AGAIN_CHECK_TIME) {
            executedJudgment();
            i++;
        }
        if (model.getSuccessCount() >= 5) {
            ContainerUtils.addControllerMap(model.getIp(), model);
            log.info("id : {}  was successed", this.model.getIp());
            return;
        }
        ContainerUtils.removeJsoupResultMap(model.getIp());
    }

    /**
     * 验证proxy id 是否可用，并记录相应的验证结果（验证频率 ）
     *
     * @see com.weixiao.smart.container.Constants#FIXED_RATE
     */
    private void executedJudgment() {
        if (Judgment.judgeStrategy(model)) {
            model.increaseSuccessCount();
        } else {
            model.increaseFailCount();
        }
//        try {
//            Thread.sleep(Constants.FIXED_RATE);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 判定proxy id 是否可用 ，可用则添加到controllerMap中
     *
     * @param standardSuccessCount successCount>standardSuccessCount视为可用
     */
    private boolean verifySuccess(int standardSuccessCount) {
        if (model.getSuccessCount() >= standardSuccessCount) {
            ContainerUtils.addControllerMap(model.getIp(), model);
            return true;
        }
        //失败次数为 4 、5 ，成功次数 1 、0  视为无效proxy ip ； 失败次数3 ，成功次数2 继续验证
//        if (model.getFailCount() > 3) {
//            return false;
//        }
        return false;
    }

}

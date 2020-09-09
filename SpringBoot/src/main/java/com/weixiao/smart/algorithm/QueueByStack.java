package com.weixiao.smart.algorithm;

import java.util.Stack;

/**
 * @author lishixiang0925@126.com.
 * @description 由两个栈组成的队列--队列特性：先进先出
 * @Created 2020-07-30 08:04.
 */
public class QueueByStack {

    private Stack<Integer> pushData = new Stack<>();
    private Stack<Integer> popData = new Stack<>();


    public Integer push(Integer item) {
        return pushData.push(item);
    }

    public Integer pop() {
        // 先将已入栈(popData)的数据全部输出
        if (popData.isEmpty()) {
            while (!pushData.isEmpty()) {
                popData.push(pushData.pop());
            }
        }

        if (!popData.isEmpty()) {
            return popData.pop();
        }
        return null;
    }

    public boolean isEmpty() {
        if (popData.isEmpty() && pushData.isEmpty()) {
            return true;
        }
        return false;
    }


}

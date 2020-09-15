package com.weixiao.smart.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

/**
 * @author lishixiang0925@126.com.
 * @description (设计一个有getMin功能的栈)
 * @Created 2020-07-20 21:41.
 */
@Slf4j
public class MinValueStack {
    //数据存放栈
    private Stack<Integer> dataStack = new Stack<>();
    //最小值存放栈
    private Stack<Integer> minDataStack = new Stack<>();




    public Integer push(Integer item) {
        if (dataStack.isEmpty()) {
            //第一个值 直接压入minDataStack
            minDataStack.push(item);
        }else{
            Integer minValue = minDataStack.peek();
            if (item - minValue > 0) {
                //newNum > minDataStack 中的最小值
                minDataStack.push(minValue);
            }else{
                minDataStack.push(item);
            }
        }
        return dataStack.push(item);
    }

    public synchronized Integer pop() {
        if (dataStack.isEmpty()) {
            return 0;
        }
        log.info("pop value = {} , min value ={} " , dataStack.peek() , minDataStack.peek());
        minDataStack.pop();
        return dataStack.pop();
    }

    public synchronized Integer peek() {
        return dataStack.peek();
    }

    public Integer getMin() {
        return minDataStack.peek();
    }

}

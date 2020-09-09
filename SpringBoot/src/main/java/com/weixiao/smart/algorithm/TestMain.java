package com.weixiao.smart.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-07-20 20:50.
 */
@Slf4j
public class TestMain {
    public static void main(String[] args) {
        //testMinValueStack();
//        testQueueStack();
//        testReverseData();
        testSortByStack();

    }

    public static  void testMinValueStack() {
        MinValueStack minValueStack = new MinValueStack();
        minValueStack.push(3);
        minValueStack.push(6);
        minValueStack.push(2);
        minValueStack.push(1);
        minValueStack.push(8);
        minValueStack.push(2);

        minValueStack.pop();
        minValueStack.pop();
        minValueStack.pop();
        minValueStack.pop();
    }
    public static  void testQueueStack() {
        QueueByStack queueByStack = new QueueByStack();
        queueByStack.push(3);
        queueByStack.push(6);
        queueByStack.push(2);
        queueByStack.push(1);
        queueByStack.push(8);
        queueByStack.push(2);


        print(queueByStack.pop());
        queueByStack.push(10);
        queueByStack.push(11);
        while (!queueByStack.isEmpty()) {
            print(queueByStack.pop());
        }
    }

    public static  void print(Object object) {
        log.info(object.toString());
    }

    public static void testReverseData(){
        Stack<Integer> dataStack = new Stack<>();
        dataStack.push(3);
        dataStack.push(6);
        dataStack.push(2);
        dataStack.push(1);
        dataStack.push(8);
        dataStack.push(2);
        RecursionStack.reverse(dataStack);
        while (!dataStack.isEmpty()) {
            print(dataStack.pop());
        }
    }

    public static void testSortByStack() {
        Stack<Integer> dataStack = new Stack<>();
        dataStack.push(3);
        dataStack.push(6);
        dataStack.push(2);
        dataStack.push(1);
        dataStack.push(8);
        dataStack.push(2);
        SortByStack.sortByStack(dataStack);
        while (!dataStack.isEmpty()) {
            print(dataStack.pop());
        }
    }

}

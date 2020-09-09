package com.weixiao.smart.algorithm;

import java.util.Stack;

/**
 * @author lishixiang0925@126.com.
 * @description (如何仅用递归函数和栈操作逆序一个栈)
 * @Created 2020-08-03 23:12.
 */
public  class RecursionStack {


    private static Integer getLastElement(Stack<Integer> data) {
        int value = data.pop();
        if (data.isEmpty()) {
            return value;
        }
        int lastElement= getLastElement(data);
        data.push(value);
        return lastElement;
    }

    public static void reverse(Stack<Integer> data) {
        if (data.isEmpty()) {
            return ;
        }
        int lastValue = getLastElement(data);
        reverse(data);
        data.push(lastValue);
    }



}

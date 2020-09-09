package com.weixiao.smart.algorithm;

import java.util.Stack;

/**
 * @author lishixiang0925@126.com.
 * @description 用一个栈实现另一个栈的排序
 * @Created 2020-08-06 23:56.
 */
public class SortByStack {

    public static void sortByStack(Stack<Integer> target) {
        Stack<Integer> temp = new Stack<>();

        while (!target.isEmpty()) {
            Integer curValue = target.pop();
            while (!temp.isEmpty() && temp.peek() < curValue) {
                target.push(temp.pop());
            }
            temp.push(curValue);
        }
        while (!temp.isEmpty()) {
            target.push(temp.pop());
        }
    }
}

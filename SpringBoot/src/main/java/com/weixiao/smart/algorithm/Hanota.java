package com.weixiao.smart.algorithm;

import java.util.Stack;

/**
 * @author lishixiang0925@126.com.
 * @description 汉诺塔问题
 * @Created 2020-09-09 07:15.
 */
public class Hanota {
    Stack<Integer> left = new Stack<>();
    Stack<Integer> mid = new Stack<>();
    Stack<Integer> right = new Stack<>();


    public void hanotaResove(int level) {
        for (int i = 0 ; i<level;i++) {
            left.push(level - i);
        }
        moveStep();

    }

    private void moveStep() {
        if()
    }


}

package com.weixiao.smart.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

/**
 * @author lishixiang0925@126.com.
 * @description 汉诺塔问题
 * @Created 2020-09-09 07:15.
 */
@Slf4j
public class Hanota {
    public enum MoveAction {
        NO, LM, MR, RM, ML;
    }


    public int hanotaResove(int level) {
        Stack<Integer> left = new Stack<>();
        Stack<Integer> mid = new Stack<>();
        Stack<Integer> right = new Stack<>();

        for (int i = 0; i < level; i++) {
            left.push(level - i);
        }
//        MoveAction recode = {MoveAction.NO};//此处写法有误，enum 编译后是 static 类型的，故此不能重新赋值
        MoveAction[] recode = {MoveAction.NO};
        int step = 0;
        while (right.size() != level) {
            //判断是否走 LM
            step +=moveStep(recode, MoveAction.ML, MoveAction.LM, left, mid , "left" , "mid");
            //判断是否走 MR
            step +=moveStep(recode, MoveAction.RM, MoveAction.MR, mid, right , "mid" , "right");
            //判断是否走 RM
            step +=moveStep(recode, MoveAction.MR, MoveAction.RM, right, mid , "right" , "mid");
            //判断是否走 ML
            step +=moveStep(recode, MoveAction.LM, MoveAction.ML, mid, left , "mid" , "left");
        }

        while (!right.isEmpty()) {
            log.info("hanota:{}",right.pop());

        }

        return step;

    }

    private int moveStep(MoveAction[] recode, MoveAction pre, MoveAction now, Stack<Integer> popStack, Stack<Integer> putStack , String from , String to) {

        if (recode[0] != pre) {
            if ((!popStack.isEmpty()&&putStack.isEmpty())||(popStack.size()>0&&popStack.peek() < putStack.peek())) {
                log.info("move {} from {} to {}" ,popStack.peek() , from , to );
                putStack.push(popStack.pop());

                recode[0] = now;
                return 1;
            }
        }
        return 0;
    }


}

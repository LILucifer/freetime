package com.weixiao.smart.minicode;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

/**
 * JDK1.8
 * @author lishixiang0925@126.com.
 * @description Mini Code Test
 * @Created 2019-05-18 23:22.
 */
@Slf4j
public class MiniCodeTest {

    /**
     * arithmetic main enter
     */
    private void mainExecute() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            if (printFizzBuzz(i)) {
            } else if (printFizz(i)) {
            } else if (printBuzz(i)) {
            } else {
                log.info("{}", i);
            }
        });
    }

    /**
     * 被3整除并被整除 打印FizzBuzz
     *
     * @param number 检测数字
     * @return true:打印，false:未打印
     */
    private boolean printFizzBuzz(int number) {
        if (number % 3 == 0 && number % 5 == 0) {
            log.info("FizzBuzz");
            return true;
        }
        return false;
    }

    /**
     * 被3整除打印Fizz
     *
     * @param number 检测数字
     * @return true:打印，false:未打印
     */
    private boolean printFizz(int number) {
        if (number % 3 == 0) {
            log.info("Fizz");
            return true;
        }
        return false;
    }

    /**
     * 被5整除打印Buzz
     *
     * @param number 检测数字
     * @return true:打印，false:未打印
     */
    private boolean printBuzz(int number) {
        if (number % 5 == 0) {
            log.info("Buzz");
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        MiniCodeTest test = new MiniCodeTest();
        test.mainExecute();
    }
}

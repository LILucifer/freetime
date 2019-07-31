package com.weixiao.smart.microspecialty.set;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-07-24 22:00.
 */
@Slf4j
public class SetTest {


    public void printHashSetElement(){
        Set<Integer> stringSet = new HashSet<Integer>();

        for (int i = 0 ; i<100000;i++) {
            stringSet.add(i );
        }
        for (Integer i : stringSet) {
            System.out.println(i);
        }

    }

    public static void main(String[] args) {
        SetTest setTest = new SetTest();
        setTest.printHashSetElement();
    }
}

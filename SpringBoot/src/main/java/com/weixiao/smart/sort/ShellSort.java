package com.weixiao.smart.sort;

/**
 * @author lishixiang@echinetech.com.
 * @description (希尔排序)
 * @Created 2018-06-07 21:42.
 */
public class ShellSort {

    public void shellSort(int[] att) {
        if (att != null && att.length > 0) {
            for (int grap = att.length / 2; grap > 0; grap /= 2) { //获取增量值，
                int k ;
               for(int j = grap ; j<att.length; j++ ) {//分组 间隔grap 做插入排序
                   int temp = att[j];
                   for( k = j ;k>0&&att[k]<att[k-grap];k-=grap)
                       att[k] = att[k-grap];
                   att[k] = temp;
               }
                printArray(att);
            }
        }
    }

    public void printArray(int[] att) {
        if (att != null && att.length > 0) {
            for (int i : att) {
                System.out.print(i+",");
            }
            System.out.println("");
        }
    }
}

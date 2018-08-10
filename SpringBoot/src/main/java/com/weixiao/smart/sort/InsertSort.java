package com.weixiao.smart.sort;

/**
 * @author lishixiang@echinetech.com.
 * @description (插入排序)
 * @Created 2018-06-07 20:32.
 */
public class InsertSort {


    public void insertSort1(int[] att){
        if (att != null && att.length > 0) {
            for(int i = 0 ; i<att.length;i++) {
                for(int j = i+1;j<att.length;j++) {
                    if (att[i] > att[j]) {
                        int temp = att[i];
                        att[i] = att[j];
                        att[j] = temp;
                    }
                }
                printArray(att);
            }
        }
    }

    public void insertSort2(int[] att) {
        if (att != null && att.length > 0) {
            int j ;
            for(int i = 1 ; i<att.length;i++) {
                int temp = att[i];
                for (j = i;j>0&&temp<att[j-1];j--) {
                    att[j] = att[j - 1];
                }
                att[j] = temp;
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

package com.weixiao.smart.sort;

import java.util.Arrays;

/**
 * @author lishixiang0925@126.com.
 * @description (归并排序)
 * @Created 2018-06-10 10:42.
 */
public class MergeSort {

    public void mergeSort(int[] att){
        int[] temp = new int[att.length];
        sort(att, temp, 0, att.length - 1);
        System.out.println("result"+Arrays.toString(att));
    }

    /**
     * 归并排序
     */
    public void sort(int[] att, int[] temp, int left, int right) {
        if (left < right) {
            int mid = (left + right)/2;
            sort(att, temp, left, mid);//对左部分数组排序
            sort(att, temp, mid + 1, right);//对左部分数组排序
            merge(att, temp, left, mid, right);//合并左右两个有序的数组
        }
    }

    /**
     * 将两个有序的数组合并
     */
    public void merge(int[] att , int[]temp , int left , int mid , int right){
        int l = left; //左序列指针开始下标
        int r = mid+1;//右序列指针开始下标
        int t = 0;//临时数组开始下标

        while (l <= mid && r <= right) {//两序列数组比较，并合并在临时数组中
            if (att[l] < att[r]) {
                temp[t++] = att[l++];
            }else{
                temp[t++] = att[r++];
            }
        }
        while (l <= mid) {//将左序列剩余元素拷贝到临时数组
            temp[t++] = att[l++];
        }
        while (r <= right) {//将右序列剩余元素拷贝到临时数组
            temp[t++] = att[r++];
        }
        //将已合并好的临时数组（有序） 拷贝到原数组中
        t = 0;
        while (left <= right) {
            att[left++] = temp[t++];
        }
    }
}

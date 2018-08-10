package com.weixiao.smart.sort;

import java.util.Arrays;

/**
 * @author lishixiang@echinetech.com.
 * @description (堆排序实现)
 * @Created 2018-06-05 21:26.
 */
public class HeapSort {

    public HeapSort(boolean isMinHean, int[] att) {
        for (int i = att.length/2 -1 ;i>=0 ; i-- ) {//从最后一个有叶子节点的位置开始调整 ,此步骤的时间复杂度为 N
            percolateDown(att,i ,att.length);
        }
        //构建成堆后，符合堆的性质，但未必是有序的数组，后续还要做堆的调整才能得到有序的数组
        System.out.println(Arrays.toString(att) + "------");
        for (int i = att.length -1 ; i>0 ; i--) {//构建成大顶堆或小顶堆后 首节点与尾节点值交换并重新对堆进行调整（最后结果是有序的数组） ， 此步骤的时间复杂度为 logN
            int temp = att[0];
            att[0] = att[i];
            att[i] = temp;
            percolateDown(att,0,i);// 重新对堆进行调整

        }
        //堆排序的时间复杂度为：O(NlogN)
        System.out.println(Arrays.toString(att));
    }

    /**
     * 调整元素位置，构建小顶堆
     * @param att
     * @param i
     * @param length
     */
    public void percolateDown(int[] att, int i, int length) {
        int temp = att[i];
        for(int k = getLeftChild(i);k<length;k = k*2+1) {
            if (k +1< length && att[k] > att[k+1]) {//如果左子节点大于右子节点，则k指向右子节点
                k++;
            }
            if (temp > att[k]) {//如果父节点大于子节点，子节点值赋给父节点
                att[i] = att[k];
                i = k ;
            }else{
                break;
            }

        }
        att[i] = temp;

    }
    private int getLeftChild(int i) {
        return 2 * i + 1;
    }


    /**
     * 构建小顶堆
     */
    private void buildMinHeap(int[] att) {

    }

    private void percolateDown(int hole) {

    }



    /**
     * 构建大顶堆
     */
    private void buildMaxHeap(int[] att ) {

    }
}

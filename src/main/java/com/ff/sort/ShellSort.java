package com.ff.sort;

import java.util.Arrays;

/**
 * 希尔排序
 * 先设置增量，将相同增量之间的元素进行插入排序。
 * 然后减少增量，逐渐排序所有的元素。
 * 因为前一个增量下，整体上已经有序了，所以减少增量后的循环中，插入排序的效率会很高。
 */
public class ShellSort {

    public static void main(String[] args) {
        int[] data = new int[]{84,83,88,87,61,50,70,60,80,99};
        System.out.println(Arrays.toString(data));
        shellSort(data);
        System.out.println(Arrays.toString(data));
    }

    public static void shellSort(int[] data) {
        for (int gap = data.length /2; gap >0 ; gap=(gap/2)) {
            for (int i = gap; i < data.length; i++) {
                int j = i;
                int cur = data[i];
                while (j - gap >= 0 && cur < data[j-gap]) {
                    data[j] = data[j-gap];
                    j = j - gap;
                }
                data[j] = cur;
            }
        }
    }
}

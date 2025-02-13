package com.ff.sort;

import java.util.Arrays;

/**
 * 快速排序
 */
public class FastSort {

    public static void main(String[] args) {
//        int[] data = new int[]{47,50,48};
        Integer[] data = new Integer[]{47, 50, 48};
        fastSort(data, 0, data.length-1);
        System.out.println(Arrays.toString(data));
    }

    public static <E extends Comparable<E>> E[] fastSort(E[] data, int left, int right){
        if (right - left < 1) {
            return data;
        }

        // 以第一个元素作为基准，左右两边分区
        int pivot = left;
        int leftIndex = pivot + 1;
        for (int i = left + 1; i <= right; i++) {
            if (data[i].compareTo(data[pivot]) < 0) {
                swap(data, i, leftIndex);
                leftIndex++;
            }
        }
        // 基准移到中间去
        swap(data, pivot, leftIndex-1);
        pivot = leftIndex - 1;

        // 左右分区递归
        fastSort(data, left, pivot);
        fastSort(data, pivot+1, right);

        return data;
    }

    private static <E> void swap(E[] data, int a, int b) {
        E temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }
}

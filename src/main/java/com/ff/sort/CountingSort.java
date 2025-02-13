package com.ff.sort;

import java.util.Arrays;

/**
 * 计数排序
 */
public class CountingSort {

    public static void main(String[] args) {

        int[] data = new int[]{3,44,38,47,15,36,26,27,2,46,4,19,50,48};
        countingSort(data, 2, 50);
        System.out.println(Arrays.toString(data));
    }

    public static void countingSort(int[] data, int min, int max) {
        int[] bucket = new int[max-min+1];
        int sortIndex = 0;

        for (int i = 0; i < data.length; i++) {
            bucket[data[i]-min]++;
        }

        for (int i = 0; i < bucket.length; i++) {
            while (bucket[i] > 0) {
                data[sortIndex++] = i+min;
                bucket[i]--;
            }
        }
    }
}

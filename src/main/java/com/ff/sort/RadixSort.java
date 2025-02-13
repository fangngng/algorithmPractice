package com.ff.sort;

import java.util.Arrays;

/**
 * 基数排序
 */
public class RadixSort {

    public static void main(String[] args) {

        int[] data = new int[]{3,44,38,47,15,36,26,27,2,46,4,19,50,48};
        radixSort(data);
        System.out.println(Arrays.toString(data));
    }

    public static void radixSort(int[] data) {
        // 取最大值
        int max = getMax(data);
        // 取最大位数
        int maxDigit = getMaxDigit(max);

        int mod = 10;
        int dev = 1;
        for (int i = 0; i < maxDigit; i++, mod*=10, dev*=10) {
            int[][] bucket = new int[10][data.length];
            int[] bucketSize = new int[10];
            // 计数排序
            for (int j = 0; j < data.length; j++) {
                int bucketIndex = (data[j]% mod)/dev;
                bucket[bucketIndex][bucketSize[bucketIndex]] = data[j];
                bucketSize[bucketIndex]++;
            }
            //合并各个分组
            int pos = 0;
            for (int k = 0; k < bucket.length; k++) {
                for (int j = 0; j < bucketSize[k]; j++) {
                    data[pos++] = bucket[k][j];
                }
            }
        }

    }

    private static int getMaxDigit(int max) {
        int mod = 10;
        int maxDigit = 1;
        while (max / mod > 0) {
            maxDigit++;
            mod*=10;
        }
        return maxDigit;
    }

    private static int getMax(int[] data) {
        int max = data[0];
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= max) {
                max = data[i];
            }
        }
        return max;
    }
}

package com.ff.sort;

import java.util.Arrays;

/**
 * 桶排序
 */
public class BucketSort {

    public static void main(String[] args) {

        int[] data = new int[]{3,44,38,47,15,36,26,27,2,46,4,19,50,48};
        bucketSort(data, 5);
        System.out.println(Arrays.toString(data));
    }

    public static void bucketSort(int[] data, int bucketSize) {
        int defaultSize = 5;
        if (bucketSize <= 0) {
            bucketSize = defaultSize;
        }

        // 找出最小值和最大值
        int min = data[0];
        int max = data[0];
        for (int i = 0; i < data.length; i++) {
            if (data[i] < min) {
                min = data[i];
            }else if(data[i] >= max){
                max = data[i];
            }
        }

        int bucketCount = (max-min) / bucketSize + 1;
        // 桶是二维数组
        int[][] bucket = new int[bucketCount][data.length];
        int[] bucketDataSize = new int[bucketCount];


        // 将数据分布到桶中，并记录每个桶中元素数量
        for (int i = 0; i < data.length; i++) {
            int bucketIndex = (data[i] - min) / bucketSize;
            bucket[bucketIndex][bucketDataSize[bucketIndex]] = data[i];
            bucketDataSize[bucketIndex]++;
        }

        int sortIndex = 0;
        // 每个桶单独排序
        for (int i = 0; i < bucketCount; i++) {
            bucket[i] = FastSort.fastSort(bucket[i], 0, bucketDataSize[i]-1);
            for (int j = 0; j < bucketDataSize[i]; j++) {
                data[sortIndex++] = bucket[i][j];
            }
        }
    }
}

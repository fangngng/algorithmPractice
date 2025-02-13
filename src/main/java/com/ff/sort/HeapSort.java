package com.ff.sort;

import java.util.Arrays;
import java.util.List;

/**
 * 堆排序
 */
public class HeapSort {

    public static void main(String[] args) {

        int[] data = new int[]{3,44,38,47,15,36,26,27,2,46,4,19,50,48};
        heapSort(data);
        System.out.println(Arrays.toString(data));
    }

    public static void heapSort(int[] data) {
        buildMaxHeap(data);

        System.out.println(Arrays.toString(data));

        int len = data.length;
        // 将堆首与堆尾交换，堆数量减1，重新平衡堆
        for (int i = data.length/2 + 1; i > 0; i--) {
            swap(data, 0, data.length-1);
            len--;
            heapify(data, i, len);
        }

    }

    public static void buildMaxHeap(int[] data) {
        for (int i = data.length/2 + 1; i >= 0; i--) {
            heapify(data, i, data.length);
        }
    }


    public static void heapify(int[] data, int i, int len) {
        int leftChild = 2*i + 1;
        int rightChild = 2*i + 2;
        int largest = i;

        if(leftChild < len && data[leftChild] > data[largest]){
            largest = leftChild;
        }
        if(rightChild < len && data[rightChild] > data[largest]){
            largest = rightChild;
        }

        if (largest != i) {
            swap(data, largest, i);
            heapify(data, largest, len);
        }
    }

    private static void swap(int[] data, int e1, int e2) {
        int temp = data[e1];
        data[e1] = data[e2];
        data[e2] = temp;
    }
}

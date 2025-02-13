package com.ff.sort;

import java.util.Arrays;
import java.util.Collections;

/**
 * 归并排序
 */
public class MergeSort {

    public static void main(String[] args) {
        int[] data = new int[]{3,44,38,47,15,36,26,27,2,46,4,19,50,48};
        int[] result = mergeSort(data);
        System.out.println(Arrays.toString(result));
    }

    public static int[] mergeSort(int[] data){
        if (data.length < 2) {
            return data;
        }
        int n = data.length / 2;
        int[] left = Arrays.copyOfRange(data, 0, n);
        int[] right = Arrays.copyOfRange(data, n, data.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length+right.length];
        int i = 0,j = 0;
        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                result[i + j] = left[i];
                i++;
            }else{
                result[i+j] = right[j];
                j++;
            }
        }
        while (i < left.length) {
            result[i+j] = left[i];
            i++;
        }
        while (j < right.length) {
            result[i+j] = right[j];
            j++;
        }
        return result;
    }
}

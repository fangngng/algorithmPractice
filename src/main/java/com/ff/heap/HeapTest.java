package com.ff.heap;

import java.util.Arrays;
import java.util.List;

public class HeapTest {

    public static void main(String[] args) {
        MaxHeap<Integer> maxHeap = new MaxHeap<>();
        maxHeap.add(1);
        maxHeap.add(2);
        maxHeap.add(3);
        maxHeap.add(4);
        maxHeap.add(5);
        maxHeap.add(6);
        maxHeap.print();
        maxHeap.popup();
        maxHeap.popup();
        maxHeap.print();

        List<Integer> arr = Arrays.asList(1,3,5,2,6,8,7,4);
        MaxHeap<Integer> maxHeap1 = new MaxHeap<>();
        maxHeap1.buildMaxHeap(arr);
        maxHeap1.print();
    }
}

package com.ff.heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MaxHeap<E> {

    List<E> data;

    public MaxHeap(){
        data = new ArrayList<>(8);
    }

    public void buildMaxHeap(List<E> data) {
        this.data = data;
        for (int i = data.size()/2 + 1; i >= 0; i--) {
            heapify(this.data, i);
        }
    }

    public void add(E e) {
        if(data.isEmpty()) {
            data.add(e);
            return;
        }

        data.add(e);

        int index = data.size()-1;
        int cur = index;
        int i = (index - 1) / 2;
        while(i >= 0 && cur > 0) {
            if (compare(e, data.get(i)) > 0) {
                swap(data, cur, i);
                cur = i;
                i = (cur-1)/2;
            }
        }
    }

    /**
     * 取出堆顶的元素
     */
    public E popup() {
        E e = data.get(0);
        swap(data, 0, data.size()-1);
        data.remove(data.size() - 1);
        // 从堆顶开始调整堆
        heapify(data, 0);
        return e;
    }

    public void heapify(List<E> data, int i) {
        int leftChild = 2*i + 1;
        int rightChild = 2*i + 2;
        int largest = i;

        if(leftChild < data.size() && compare(data.get(leftChild), data.get(largest)) > 0){
            largest = leftChild;
        }
        if(rightChild < data.size() && compare(data.get(rightChild), data.get(largest)) > 0){
            largest = rightChild;
        }

        if (largest != i) {
            swap(data, largest, i);
            heapify(data, largest);
        }

    }

    private void swap(List<E> data, int e1, int e2) {
        E temp = data.get(e1);
        data.set(e1, data.get(e2));
        data.set(e2, temp);
    }


    private Comparator<E> comparator;

    int compare(E e1, E e2) {
        if (comparator == null) {
            return ((Comparable<E>)e1).compareTo(e2);
        }
        return comparator.compare(e1, e2);
    }

    public void print(){
        System.out.println(data.toString());
    }

}

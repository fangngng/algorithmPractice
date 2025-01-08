package com.ff.tree.binarytree;

public class BinarySearchTreeTest {

    public static void main(String[] args) {
        BinarySearchTree<Integer> bt = new BinarySearchTreeIm<>();
        bt.add(3);
        bt.add(4);
        bt.add(2);
        bt.add(1);
        bt.add(5);
        System.out.println(bt.size());
        bt.print();
    }

}

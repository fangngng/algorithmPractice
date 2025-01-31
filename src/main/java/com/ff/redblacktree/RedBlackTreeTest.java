package com.ff.redblacktree;

import com.ff.tree.binarytree.redblack.RedBlackTree;

public class RedBlackTreeTest {

    public static void main(String[] args) {
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
        redBlackTree.add(1);
        redBlackTree.add(2);
        redBlackTree.add(3);
        redBlackTree.add(4);
        redBlackTree.add(5);
        redBlackTree.add(6);
        redBlackTree.add(7);
        redBlackTree.add(8);
        redBlackTree.add(9);
        redBlackTree.add(10);
        redBlackTree.add(11);
        redBlackTree.add(12);
        redBlackTree.print();
        System.out.println("----------------------------------");

        redBlackTree.delete(redBlackTree.find(8));
        redBlackTree.print();
        System.out.println("----------------------------------");
    }
}

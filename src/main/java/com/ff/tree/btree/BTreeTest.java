package com.ff.tree.btree;

public class BTreeTest {

    public static void main(String[] args) {
        BTree<Integer> bTree = new BTree();
        bTree.add(1);
        bTree.add(2);
        bTree.add(3);
        bTree.add(4);
        bTree.add(5);
        bTree.add(6);
        bTree.add(7);
        bTree.add(8);
        bTree.add(9);
        bTree.add(10);
        bTree.add(11);
        bTree.add(12);
        bTree.add(13);
        bTree.add(14);
        bTree.add(15);
        bTree.add(16);
        bTree.add(17);
        bTree.add(18);
        bTree.print();
        System.out.println("add end-----------------------");
        bTree.remove(22);
        bTree.print();
        System.out.println("22-----------------------");
        bTree.remove(1);
        bTree.print();
        System.out.println("1-----------------------");
        bTree.remove(2);
        bTree.print();
        System.out.println("2-----------------------");
        bTree.remove(3);
        bTree.print();
        System.out.println("3-----------------------");
        bTree.remove(16);
        bTree.print();
        System.out.println("16-----------------------");
        bTree.remove(17);
        bTree.print();
        System.out.println("17-----------------------");
        bTree.remove(18);
        bTree.print();
        System.out.println("18-----------------------");
        bTree.remove(15);
        bTree.print();
        System.out.println("15-----------------------");
        bTree.remove(14);
        bTree.print();
        System.out.println("14-----------------------");
        bTree.remove(13);
        bTree.print();
        System.out.println("13-----------------------");
        bTree.remove(7);
        bTree.print();
        System.out.println("7-----------------------");
        bTree.remove(5);
        bTree.print();
        System.out.println("5-----------------------");
        bTree.remove(6);
        bTree.print();
        System.out.println("6-----------------------");
        bTree.remove(11);
        bTree.print();
        System.out.println("11-----------------------");
        bTree.remove(10);
        bTree.print();
        System.out.println("10-----------------------");
        bTree.remove(12);
        bTree.print();
        System.out.println("12-----------------------");
        bTree.remove(8);
        bTree.print();
        System.out.println("8-----------------------");
        bTree.remove(9);
        bTree.print();
        System.out.println("9-----------------------");
        bTree.remove(4);
        bTree.print();
        System.out.println("4-----------------------");
    }
}

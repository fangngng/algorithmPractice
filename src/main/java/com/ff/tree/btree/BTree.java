package com.ff.tree.btree;

import com.ff.tree.binarytree.BinarySearchTree;
import com.ff.tree.binarytree.Node;

import java.util.Comparator;

public class BTree<E> {

    BNode<E> root;

    int size = 0;

    public static int m = 4;

    boolean flag = false;

    public int size() {
        return size;
    }

    public Boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        this.root = null;
        this.size = 0;
    }

    public BNode<E> find(E e){
        if (root == null) {
            return null;
        }

        BNode<E> node = root;
        BNode<E> parent = null;

        while (node != null) {
            int i = 0;
            while (i < node.usedSize) {
                if (compare(node.elements.get(i), e) < 0) {
                    i++;
                } else if (compare(node.elements.get(i), e) > 0) {
                    break;
                }else {
                    flag = true;
                    return null;
                }
            }
            parent = node;
            node = node.sub.get(i);
        }

        return parent;
    }

    private Comparator<E> comparator;

    int compare(E e1, E e2) {
        if (comparator == null) {
            return ((Comparable<E>)e1).compareTo(e2);
        }
        return comparator.compare(e1, e2);
    }

    public boolean add(E element) {
        if (root == null) {
            root = new BNode<>(m, null);
            root.elements.set(0, element);
            root.usedSize ++;
        }

        BNode<E> insertP = find(element);
        if (flag) {
            return false;
        }

        int i = insertP.usedSize - 1;
        for (; i >= 0; i--) {
            if (compare(insertP.elements.get(i), element) >= 0) {
                insertP.elements.set(i+1, insertP.elements.get(i));
            }else{
                break;
            }
        }

        insertP.elements.set(i+1, element);
        insertP.usedSize++;

        if (insertP.usedSize < m) {
            return true;
        }else{
            split(insertP);
            return true;
        }
    }

    /**
     * 分裂
     * @param node
     */
    private void split(BNode<E> node) {

    }

    public void remove(Node<E> node) {

    }

    public Boolean contains(E element) {
        return null;
    }

    public Node<E> node(E element) {
        return null;
    }

    public void print() {

    }
}

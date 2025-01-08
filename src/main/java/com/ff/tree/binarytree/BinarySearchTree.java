package com.ff.tree.binarytree;

public interface BinarySearchTree<E> {

    int size();

    Boolean isEmpty();

    void clear();

    void add(E element);

    void remove(Node<E> node);

    Boolean contains(E element);

    Node<E> node(E element);

    void print();
}

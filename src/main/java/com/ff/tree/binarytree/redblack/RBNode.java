package com.ff.tree.binarytree.redblack;

public class RBNode<E extends Comparable<E>> {

    boolean black;

    RBNode<E> left;

    RBNode<E> right;

    RBNode<E> parents;

    E element;

    public RBNode(E element, RBNode<E> parents) {
        this.parents = parents;
        this.element = element;
        black = false;
        left = null;
        right = null;
    }
}

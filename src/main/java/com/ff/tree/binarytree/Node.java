package com.ff.tree.binarytree;

public class Node <E>{

    Node<E> parents;

    Node<E> left;

    Node<E> right;

    E element;

    public Node(E element, Node<E> parents){
        this.element = element;
        this.parents = parents;
    }

    public Boolean isLeaf(){
        return left == null && right == null;
    }

    public Boolean haveTwoChildren(){
        return left != null && right != null;
    }
}

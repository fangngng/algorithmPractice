package com.ff.tree.binarytree;

public class BinaryTree<E> {

    private Node<E> root;

    public BinaryTree(Node<E> root){
        this.root = root;
    }

    public void addLeftNode(Node<E> node, Node<E> parents){
        node.parents = parents;
        parents.left = node;
    }

    public void addRightNode(Node<E> node, Node<E> parents){
        node.parents = parents;
        parents.right = node;
    }

}

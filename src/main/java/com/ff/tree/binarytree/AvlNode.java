package com.ff.tree.binarytree;

public class AvlNode<E> extends Node<E>{

    int height;

    public AvlNode(E element, AvlNode<E> parents) {
        super(element, parents);
        updateHeight();
    }

    public void updateHeight(){
        int leftHeight = left == null ? 0 : ((AvlNode<E>)left).height;
        int rightHeight = right == null ? 0 : ((AvlNode<E>)right).height;
        height = 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * 平衡因子
     * @return
     */
    public int balanceFactor(){
        int leftHeight = left == null ? 0 : ((AvlNode<E>)left).height;
        int rightHeight = right == null ? 0 : ((AvlNode<E>)right).height;
        return leftHeight - rightHeight;
    }

    public boolean isBalanced(){
        return Math.abs(balanceFactor()) <= 1;
    }

    public Node<E> higherChild(){
        int leftHeight = left == null ? 0 : ((AvlNode<E>)left).height;
        int rightHeight = right == null ? 0 : ((AvlNode<E>)right).height;
        if(leftHeight > rightHeight){
            return left;
        }
        if (rightHeight > leftHeight) {
            return right;
        }
        return super.isLeaf()?left:right;
    }
}

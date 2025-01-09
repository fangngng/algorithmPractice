package com.ff.tree.binarytree;

public class AvlTree<E> extends BinarySearchTreeIm<E> implements BinarySearchTree<E>{

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public Boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Node<E> add(E element) {
        Node<E> add = addNode(element);
        afterAdd(add);
        return add;
    }

    public Node<E> addNode(E element) {
        super.elementNotNullCheck(element);

        if (root == null) {
            root = new AvlNode<>(element, null);
            size ++;
            return root;
        }

        size++;
        Node<E> node = root;
        Node<E> parent = null;
        int cmp = 0;
        while (node != null) {

            parent = node;
            cmp = compare(element, node.element);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            }else{
                node.element = element;
                return node;
            }
        }

        AvlNode<E> newNode = new AvlNode<>(element, (AvlNode<E>)parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else if (cmp < 0){
            parent.left = newNode;
        }else{

        }
        return newNode;
    }

    /**
     * 添加节点后，需要平衡下
     * @param node
     */
    public void afterAdd(Node<E> node){
        while ((node = node.parents) != null) {
            if (((AvlNode<E>) node).isBalanced()) {
                updateHeight((AvlNode<E>) node);
            }else{
                // 恢复平衡
                reBalanced(node);
            }
        }
    }

    public void reBalanced(Node<E> grand) {
        Node<E> parent = ((AvlNode<E>) grand).higherChild();
        Node<E> child = ((AvlNode<E>) parent).higherChild();

        if (grand.isLeftChild()) {
            if (parent.isLeftChild()) {
                // LL
                rotateRight(grand);
            }else{
                // LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        }else{
            if (parent.isLeftChild()) {
                // RL
                rotateRight(parent);
                rotateLeft(grand);
            }else{
                // RR
                rotateLeft(grand);
            }
        }
    }

    @Override
    public void remove(Node<E> node) {
        super.remove(node);
        afterRemove(node);
    }

    public void afterRemove(Node<E> node) {
        while ((node = node.parents) != null) {
            if (((AvlNode<E>) node).isBalanced()) {
                updateHeight((AvlNode<E>) node);
            }else{
                reBalanced(node);
            }
        }
    }

    @Override
    public Boolean contains(E element) {
        return super.contains(element);
    }

    @Override
    public Node<E> node(E element) {
        return super.node(element);
    }

    @Override
    public void print() {
        super.print();
    }

    /**
     * 左旋 - RR
     * @param grand
     */
    public void rotateLeft(Node<E> grand){

        Node<E> p = grand.right;
        Node<E> child = p.left;

        // 旋转，更新子节点信息
        grand.right = child;
        p.left = grand;

        // 更新几个节点的父节点信息
        afterRotate(grand, p, child);
    }

    public void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        if (grand.isLeftChild()) {
            grand.parents.left = parent;
        } else if (grand.isRightChild()) {
            grand.parents.right = parent;
        } else {
            root = parent;
        }

        if (child != null) {
            child.parents = grand;
        }

        parent.parents = grand.parents;
        grand.parents = parent;

        // 更新高度
        updateHeight((AvlNode<E>) grand);
        updateHeight((AvlNode<E>) parent);
    }

    public void updateHeight(AvlNode<E> node) {
        node.updateHeight();
    }

    /**
     * 右旋 - LL
     * @param grand
     */
    public void rotateRight(Node<E> grand) {

        Node<E> p = grand.left;
        // 需移动的子节点
        Node<E> child = p.right;

        grand.left = child;
        p.right = grand;

        // 更新几个节点的父节点信息
        afterRotate(grand, p, child);
    }


}

package com.ff.tree.binarytree;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class BinarySearchTreeIm<E> implements BinarySearchTree<E>{

    int size = 0;

    Node<E> root;

    @Override
    public int size() {
        return size;
    }

    @Override
    public Boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Node<E> add(E element) {
        elementNotNullCheck(element);

        if (root == null) {
            root = new Node<E>(element, null);
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

        Node<E> newNode = new Node<>(element, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else if (cmp < 0){
            parent.left = newNode;
        }else{

        }
        return newNode;
    }

    @Override
    public void remove(Node<E> node) {
        if (root == null) {
            return;
        }

        size--;
        if (node.haveTwoChildren()) {
            // 度为2
            Node<E> sNode = this.sucessor(node);
            node.element = sNode.element;
            // 删除sNode
            node = sNode;
        }

        // 下面删除度为1或0的节点，或者度为2的节点的后继节点
        if (node.isLeaf()) {
            // 度为0
            if (node.parents == null) {
                root = null;
                return;
            }
            if(node == node.parents.left){
                node.parents.left = null;
            }else{
                node.parents.right = null;
            }
        }else{
            // 度为1
            Node<E> replaceNode = node.left != null ? node.left : node.right;
            if (node.parents == null) {
                root = replaceNode;
            }else if(node == node.parents.left){
                node.parents.left = replaceNode;
            }else{
                node.parents.right = replaceNode;
            }
        }

    }

    // 前驱节点
    public Node<E> predecessor(Node<E> node){
        if (node == null) {
            return null;
        }

        Node<E> p = node.left;
        if (p != null) {
            // 存在左树，则先驱节点在左子树的最右侧
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }else{
            // 不存在左子树，则先驱节点在父节点中
            while (node.parents != null && node == node.parents.left) {
                // 找到父节点不为空，节点在父节点的右子树中，则此父节点即前驱节点
                node = node.parents;
            }
            return node.parents;
        }
    }

    // 后继节点
    public Node<E> sucessor(Node<E> node){
        if (node == null) {
            return null;
        }

        Node<E> p = node.right;
        if (p != null) {
            // 存在右树，则后继节点在右子树的最左侧
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }else{
            // 不存在右子树，则先驱节点在父节点中
            while (node.parents != null && node == node.parents.right) {
                // 找到父节点不为空，节点为父节点的左子树，则父节点为后继节点
                node = node.parents;
            }
            return node.parents;
        }
    }

    @Override
    public Boolean contains(E element) {
        return node(element) != null;
    }

    public Node<E> node(E element) {
        if (element == null) {
            return null;
        }
        Node<E> node = root;
        while (node != null) {
            int cmp = compare(element, node.element);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else{
                return node;
            }
        }

        return null;
    }

    void elementNotNullCheck(E element) {
        if (element == null) {
            throw new RuntimeException("element can not be null");
        }
    }

    private Comparator<E> comparator;

    int compare(E e1, E e2) {
        if (comparator == null) {
            return ((Comparable<E>)e1).compareTo(e2);
        }
        return comparator.compare(e1, e2);
    }

    @Override
    public void print(){
        if (root == null) {
            System.out.println("empty");
            return;
        }

        // 层次遍历
        Queue<Node<E>> q = new LinkedBlockingQueue<>(size);
        q.offer(root);
        while (!q.isEmpty()) {
            Node<E> poll = q.poll();
            System.out.println(poll.element);
            if (poll.left != null) {
                q.offer(poll.left);
            }
            if (poll.right != null) {
                q.offer(poll.right);
            }
        }
    }
}

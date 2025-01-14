package com.ff.tree.btree;

import com.ff.tree.binarytree.BinarySearchTree;
import com.ff.tree.binarytree.Node;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
        size++;
        if (root == null) {
            root = new BNode<>(m, null);
            root.elements.set(0, element);
            root.usedSize ++;
            return true;
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
        // 复制一个新节点出来
        BNode<E> newNode = new BNode<>(m);
        int mid = node.elements.size()/2;
        int j = 0;
        for (int i = mid+1; i < node.usedSize; i++) {
            newNode.elements.set(j, node.elements.get(i));
//            newNode.usedSize++;
            newNode.sub.set(j, node.sub.get(i));
            if (newNode.sub.get(j) != null) {
                newNode.sub.get(j).parents = newNode;
            }
            node.elements.set(i, null);
            node.sub.set(i, null);
            j++;
        }
        newNode.sub.set(j, node.sub.get(node.usedSize));
        node.sub.set(node.usedSize, null);
        if (newNode.sub.get(j) != null) {
            newNode.sub.get(j).parents = newNode;
        }

        // 更新节点数据
        newNode.usedSize = j;
        newNode.parents = node.parents;
        node.usedSize = node.usedSize - j - 1;

        if (node == root) {
            // 新建一个root节点出来
            root = new BNode<>(m);
            root.elements.set(0, node.elements.get(mid));
            node.elements.set(mid, null);
            root.sub.set(0, node);
            root.sub.set(1, newNode);
            root.sub.get(0).parents = root;
            root.sub.get(1).parents = root;
            root.usedSize ++;
            return;
        }else{
            // 将中间节点上提
            BNode<E> parents = node.parents;
            int end = parents.usedSize - 1;
            E midVal = node.elements.get(mid);
            node.elements.set(mid, null);
            for (; end >= 0; end--) {
                if (compare(parents.elements.get(end), midVal) >= 0) {
                    parents.elements.set(end + 1, parents.elements.get(end));
                    parents.sub.set(end + 2, parents.sub.get(end+1));
                }else{
                    break;
                }
            }
            parents.elements.set(end + 1, midVal);
            parents.sub.set(end+2, newNode);
            parents.usedSize++;

            if(parents.usedSize >= m){
               split(parents);
            }

        }
    }

    public void remove(E element) {
        if (root == null) {
            return ;
        }

        BNode<E> findNode = find(element);
        if (findNode == null) {
            return;
        }

        // 如果是叶子节点，则直接删除掉元素
        if (isLeafNode(findNode)) {
            int i = 0;
            for (; i < findNode.usedSize; i++) {
                if (compare(findNode.elements.get(i), element) == 0) {
                    break;
                }
            }
            for (int j = i; j < findNode.usedSize - 1; j++) {
                findNode.elements.set(j, findNode.elements.get(j + 1));
            }
        }else{
            // 如果不是叶子节点，则
        }

    }

    private boolean isLeafNode(BNode<E> node){
        if (node == null || node.usedSize == 0) {
            return false;
        }
        if (node.sub.isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean contains(E element) {
        return null;
    }

    public Node<E> node(E element) {
        return null;
    }

    public void print() {
        if (root == null) {
            System.out.println("empty");
            return;
        }

        // 层次遍历
        Queue<BNode<E>> q = new LinkedBlockingQueue<>(size);
        q.offer(root);
        while (!q.isEmpty()) {
            BNode<E> poll = q.poll();
            System.out.println(poll.elements);
            for (int i = 0; i < poll.usedSize + 1; i++) {
                if (poll.sub.get(i) != null) {
                    q.offer(poll.sub.get(i));
                }
            }
        }
    }
}

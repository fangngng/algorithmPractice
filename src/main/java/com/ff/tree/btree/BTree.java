package com.ff.tree.btree;

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

    /**
     * 添加元素
     * 从叶子节点中开始添加，然后往上分裂，直到root节点
     * 如果没有root节点，则以当前元素k构建root节点
     * 如果有root节点，找到元素要插入的节点u。
     *      1. 如果插入的节点中，元素和其中某个元素相等，则插入失败。
     *      2. 否则，将元素插入当前节点，并挪移后于元素的所有元素和子树的位置。
     * 如果插入完后，此节点元素个数>m，则需要将此节点进行分裂。
     * 分裂
     *      需要将此节点u中间节点右侧所有元素和子树，构造一个新的节点u1。
     *      将此节点u的中间节点，上升到父节点中，并且对应子树指向新节点u1。
     *      如果父节点的元素个数>m，则继续递归分裂父节点。
     * @param element
     * @return
     */
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

    /**
     * 删除元素
     * 从根节点往下遍历
     * 如果此节点u不包含要删除的元素k，且是内部节点，确定包含k的子树u1。
     *      1. 如果子树u1中关键字>=t,则继续往下遍历。
     *      2. 如果子树u1中关键字<t，则：
     *          1. 如果子树u1的相邻兄弟节点中，有关键字>=t的，则从元素最多的兄弟中，上升一个关键字到此节点u中，并将此节点u中一个关键字下降到子树u1中；
     *          2. 如果子树u1的相邻兄弟节点中，关键字都<t，则将子树u1和其中一个兄弟进行合并，再将此节点中一个关键字下降到此新节点中。保证此节点中元素和子树数量都减1。
     * 如果此节点u包含要删除的元素k，
     *      1. 如果此节点是叶子节点，则直接删去元素k
     *      2. 如果此节点是内部节点，则
     *          1. 此节点u中，前于k的子树u1，关键字>=t，则从子树u1中找到前驱元素k1，将k1从子树u1中删除，放到u中替代k。
     *          2. 此节点u中，前于k的子树u1，关键字<t，则看后于k的子树u2。如果u2的关键字 >=t，则从子树u2中找到后继元素k2，将K2从子树u2中删除，放到u中替代k。
     *          3. 此节点u中，前子树u1和后子树u2，关键字都<t，则将元素K下降到u2子树中，再和u1子树合并。保证此节点中元素和子树数量都减1。此时元素k从节点u中已删除，继续递归删除子树中的k。
     *
     * @param element
     */
    public void remove(E element) {
        if (root == null) {
            return ;
        }


        BNode<E> cur = root;
        remove(element, cur);

    }

    private BNode<E> handleIfRightChildTree(BNode<E> cur, int i, int t){
        // 要删除的元素不在此节点中，但在对应子树中
        BNode<E> cNode = cur.sub.get(i);
        if (cNode == null) {
            // 说明此元素不存在
            return null;
        }
        if (cNode.usedSize >= t) {
            // 说明子树删除一个元素，不会引起树结构变化，继续往下遍历
            cur = cNode;
            return cur;
        }else{
            // 说明子树元素不足，删除会引起树结构变化
            // 找到兄弟节点
            BNode<E> leftBrother = null;
            BNode<E> rightBrother = cur.sub.get(i+1);
            if (i >= 1) {
                leftBrother = cur.sub.get(i-1);
            }
            if (leftBrother == null && rightBrother == null) {
                return null;
            }
            // 如果兄弟节点中关键字有>=t，找关键字数量最多的那个兄弟节点中，上升一个元素到父节点中，父节点中的一个元素下降到子树中
            int leftSize = leftBrother==null?0:leftBrother.usedSize;
            int rightSize = rightBrother==null?0:rightBrother.usedSize;
            BNode<E> deleteElementNode = null;
            if (leftSize >= t && rightSize >= t) {
                if (leftSize > rightSize) {
                    deleteElementNode = leftBrother;
                    // 左兄弟节点中上升元素
                    E maxElement = findMaxElement(deleteElementNode);
                    remove(maxElement, deleteElementNode);
                    E replaceElement = cur.elements.get(i);
                    // 替换
                    cur.elements.set(i, maxElement);
                    // 当前元素下降到子树中
                    cNode.elements.set(cNode.usedSize, replaceElement);
                    cNode.usedSize++;
                    // 从子树中继续遍历删除
                    cur = cNode;
                    return cur;
                }else{
                    deleteElementNode = rightBrother;
                    // 右兄弟节点中上升元素
                    E minElement = findMinElement(deleteElementNode);
                    remove(minElement, deleteElementNode);
                    E replaceElement = cur.elements.get(i);
                    cur.elements.set(i, minElement);
                    // 当前元素下降到子树中
                    cNode.elements.set(cNode.usedSize, replaceElement);
                    cNode.usedSize++;
                    // 从子树中继续遍历删除
                    cur = cNode;
                    return cur;
                }
            }else if(leftSize < t && rightSize >= t){
                deleteElementNode = rightBrother;
                // 右兄弟节点中上升元素
                E minElement = findMinElement(deleteElementNode);
                remove(minElement, deleteElementNode);
                E replaceElement = cur.elements.get(i);
                cur.elements.set(i, minElement);
                // 当前元素下降到子树中
                cNode.elements.set(cNode.usedSize, replaceElement);
                cNode.usedSize++;
                // 从子树中继续遍历删除
                cur = cNode;
                return cur;
            }else if(leftSize >= t && rightSize < t){
                deleteElementNode = leftBrother;
                // 左兄弟节点中上升元素
                E maxElement = findMaxElement(deleteElementNode);
                remove(maxElement, deleteElementNode);
                E replaceElement = cur.elements.get(i-1);
                // 替换
                cur.elements.set(i-1, maxElement);
                // 当前元素下降到子树中
                for (int j = cNode.usedSize-1; j >= 0; j--) {
                    cNode.elements.set(j+1, cNode.elements.get(j));
                }
                cNode.elements.set(0, replaceElement);
                cNode.usedSize++;
                // 从子树中继续遍历删除
                cur = cNode;
                return cur;
            }else if(rightBrother != null){
                // 两个兄弟节点的数量都不足，则需要将此子树合并右兄弟节点。
                BNode<E> newNode = new BNode<>(m);
                E curElement = cur.elements.get(i);
                int j = 0;
                for (; j < cNode.usedSize; j++) {
                    newNode.elements.set(j, cNode.elements.get(j));
                    newNode.sub.set(j, cNode.sub.get(j));
                }
                newNode.sub.set(j, cNode.sub.get(j));
                newNode.elements.set(j, curElement);
                j++;
                for (int k = 0; k < rightBrother.usedSize; k++) {
                    newNode.elements.set(j, rightBrother.elements.get(k));
                    newNode.sub.set(j, rightBrother.sub.get(k));
                    j++;
                }
                newNode.parents = cur;
                newNode.usedSize = cNode.usedSize + rightSize + 1;
                cur.sub.set(i+1, newNode);
                // 删除cur节点中的元素，更改信息
                for (int k = i; k < cur.usedSize; k++) {
                    cur.elements.set(k, cur.elements.get(k + 1));
                    cur.sub.set(k, cur.sub.get(k + 1));
                }
                cur.sub.set(cur.usedSize, null);
                cur.usedSize--;
                if (root == cur && cur.usedSize == 0) {
                    root = cur.sub.get(0);
                }
                cur = newNode;
                return cur;
            }else {
                // 两个兄弟节点的数量都不足，但没有右兄弟，则需要将此子树合并左兄弟节点。
                BNode<E> newNode = new BNode<>(m);
                int j = 0;
                for (int k = 0; k < leftBrother.usedSize; k++) {
                    newNode.elements.set(j, leftBrother.elements.get(k));
                    newNode.sub.set(j, leftBrother.sub.get(k));
                    j++;
                }
                newNode.sub.set(j, leftBrother.sub.get(j));
                E curElement = cur.elements.get(i-1);
                newNode.elements.set(j, curElement);
                j++;
                for (int k = 0; k < cNode.usedSize; k++) {
                    newNode.elements.set(k+j, cNode.elements.get(k));
                    newNode.sub.set(k+j, cNode.sub.get(k));
                }
                newNode.parents = cur;
                newNode.usedSize = cNode.usedSize + leftSize + 1;
                newNode.sub.set(newNode.usedSize, cNode.sub.get(cNode.usedSize));
                // 删除cur节点中的元素，更改信息
                for (int k = i-1; k < cur.usedSize; k++) {
                    cur.elements.set(k, cur.elements.get(k + 1));
                    cur.sub.set(k, cur.sub.get(k + 1));
                }
                cur.sub.set(i-1, newNode);
                cur.sub.set(cur.usedSize, null);
                cur.usedSize--;
                if (root == cur && cur.usedSize == 0) {
                    root = cur.sub.get(0);
                }
                cur = newNode;
                return cur;
            }
        }
    }

    private void remove(E element, BNode<E> cur) {
        int t = m/2;
        while(cur != null){
            int i = 0;
            while (i < cur.usedSize) {
                if (compare(cur.elements.get(i), element) < 0) {
                    i++;
                    if (i == cur.usedSize) {
                        // todo 要删除的元素在最右边子树中
                        BNode<E> b = handleIfRightChildTree(cur, i, t);
                        if (b != null) {
                            cur = b;
                            break;
                        }else{
                            return;
                        }
                    }
                }else if(compare(cur.elements.get(i), element) > 0){
                    // 要删除的元素不在此节点中，但在对应子树中
                    BNode<E> cNode = cur.sub.get(i);
                    if (cNode == null) {
                        // 说明此元素不存在
                        return;
                    }
                    if (cNode.usedSize >= t) {
                        // 说明子树删除一个元素，不会引起树结构变化，继续往下遍历
                        cur = cNode;
                        break;
                    }else{
                        // 说明子树元素不足，删除会引起树结构变化
                        // 找到兄弟节点
                        BNode<E> leftBrother = null;
                        BNode<E> rightBrother = cur.sub.get(i+1);
                        if (i > 1) {
                            leftBrother = cur.sub.get(i-1);
                        }
                        if (leftBrother == null && rightBrother == null) {
                            return;
                        }
                        // 如果兄弟节点中关键字有>=t，找关键字数量最多的那个兄弟节点中，上升一个元素到父节点中，父节点中的一个元素下降到子树中
                        int leftSize = leftBrother==null?0:leftBrother.usedSize;
                        int rightSize = rightBrother==null?0:rightBrother.usedSize;
                        BNode<E> deleteElementNode = null;
                        if (leftSize >= t && rightSize >= t) {
                            if (leftSize > rightSize) {
                                deleteElementNode = leftBrother;
                                // 左兄弟节点中上升元素
                                E maxElement = findMaxElement(deleteElementNode);
                                remove(maxElement, deleteElementNode);
                                E replaceElement = cur.elements.get(i);
                                // 替换
                                cur.elements.set(i, maxElement);
                                // 当前元素下降到子树中
                                cNode.elements.set(cNode.usedSize, replaceElement);
                                cNode.usedSize++;
                                // 从子树中继续遍历删除
                                cur = cNode;
                                break;
                            }else{
                                deleteElementNode = rightBrother;
                                // 右兄弟节点中上升元素
                                E minElement = findMinElement(deleteElementNode);
                                remove(minElement, deleteElementNode);
                                E replaceElement = cur.elements.get(i);
                                cur.elements.set(i, minElement);
                                // 当前元素下降到子树中
                                cNode.elements.set(cNode.usedSize, replaceElement);
                                cNode.usedSize++;
                                // 从子树中继续遍历删除
                                cur = cNode;
                                break;
                            }
                        }else if(leftSize < t && rightSize >= t){
                            deleteElementNode = rightBrother;
                            // 右兄弟节点中上升元素
                            E minElement = findMinElement(deleteElementNode);
                            remove(minElement, deleteElementNode);
                            E replaceElement = cur.elements.get(i);
                            cur.elements.set(i, minElement);
                            // 当前元素下降到子树中
                            cNode.elements.set(cNode.usedSize, replaceElement);
                            cNode.usedSize++;
                            // 从子树中继续遍历删除
                            cur = cNode;
                            break;
                        }else if(leftSize >= t && rightSize < t){
                            deleteElementNode = leftBrother;
                            // 左兄弟节点中上升元素
                            E maxElement = findMaxElement(deleteElementNode);
                            remove(maxElement, deleteElementNode);
                            E replaceElement = cur.elements.get(i);
                            // 替换
                            cur.elements.set(i, maxElement);
                            // 当前元素下降到子树中
                            cNode.elements.set(cNode.usedSize, replaceElement);
                            cNode.usedSize++;
                            // 从子树中继续遍历删除
                            cur = cNode;
                            break;
                        }else{
                            // 两个兄弟节点的数量都不足，则需要将此子树合并兄弟节点。
                            BNode<E> newNode = new BNode<>(m);
                            E curElement = cur.elements.get(i);
                            int j = 0;
                            for (; j < cNode.usedSize; j++) {
                                newNode.elements.set(j, cNode.elements.get(j));
                                newNode.sub.set(j, cNode.sub.get(j));
                            }
                            newNode.sub.set(j, cNode.sub.get(j));
                            newNode.elements.set(j, curElement);
                            j++;
                            if (rightBrother != null) {
                                for (int k = 0; k < rightBrother.usedSize; k++) {
                                    newNode.elements.set(j, rightBrother.elements.get(k));
                                    newNode.sub.set(j, rightBrother.sub.get(k));
                                    j++;
                                }
                            }
                            newNode.parents = cur;
                            newNode.usedSize = cNode.usedSize + rightBrother.usedSize + 1;
                            cur.sub.set(i+1, newNode);
                            // 删除cur节点中的元素，更改信息
                            for (int k = i; k < cur.usedSize; k++) {
                                cur.elements.set(k, cur.elements.get(k + 1));
                                cur.sub.set(k, cur.sub.get(k + 1));
                            }
                            cur.sub.set(cur.usedSize, null);
                            cur.usedSize--;
                            if (root == cur && cur.usedSize == 0) {
                                root = cur.sub.get(0);
                            }
                            cur = newNode;
                            break;
                        }

                    }

                }else{
                    // 当前元素在此节点中
                    if (isLeafNode(cur)) {
                        // 叶子节点，则直接删除此元素
                        for (int j = 0; j < cur.usedSize; j++) {
                            if (compare(cur.elements.get(j), element) == 0) {
                                for (int k = j; k < cur.usedSize-1; k++) {
                                    cur.elements.set(k, cur.elements.get(k + 1));
                                }
                                cur.elements.set(cur.usedSize-1, null);
                                cur.usedSize--;
                                if (root == cur && cur.usedSize == 0) {
                                    root = cur.sub.get(0);
                                }
                                return;
                            }
                        }
                    }else{
//                        如果此节点是内部节点，则
//                              *          1. 此节点u中，前于k的子树u1，关键字>=t，则从子树u1中找到前驱元素k1，将k1从子树u1中删除，放到u中替代k。
//                              *          2. 此节点u中，前于k的子树u1，关键字<t，则看后于k的子树u2。如果u2的关键字 >=t，则从子树u2中找到后继元素k2，将K2从子树u2中删除，放到u中替代k。
//                              *          3. 此节点u中，前子树u1和后子树u2，关键字都<t，则将元素K下降到u2子树中，再和u1子树合并。保证此节点中元素和子树数量都减1。此时元素k从节点u中已删除，继续递归删除子树中的k。
                        int pos = 0;
                        for (int j = 0; j < cur.usedSize; j++) {
                            if (compare(cur.elements.get(j), element) == 0) {
                                pos = j;
                                break;
                            }
                        }
                        BNode<E> u1 = cur.sub.get(pos);
                        BNode<E> u2 = cur.sub.get(pos + 1);
                        if (u1 != null && u1.usedSize > t) {
                            // 找到前驱元素，替换此元素
                            E maxElement = findMaxElement(u1);
                            cur.elements.set(pos, maxElement);
                            remove(maxElement, u1);
                            return;
                        } else if (u2 != null && u2.usedSize > t) {
                            // 找到后继元素，替换此元素
                            E minElement = findMinElement(u2);
                            cur.elements.set(pos, minElement);
                            remove(minElement, u2);
                            return;
                        } else{
                            BNode<E> newNode = new BNode<>(m);
                            newNode.parents = cur;
                            // 将u1, u2, 要删除的元素都放到新节点中，再从当前节点中删除元素，然后递归删除新节点
                            int ePos = 0;
                            if (u1 != null) {
                                for (int j = 0; j < u1.usedSize; j++) {
                                    newNode.elements.set(j, u1.elements.get(j));
                                    newNode.sub.set(j, u1.sub.get(j));
                                }
                                newNode.sub.set(u1.usedSize, u1.sub.get(u1.usedSize));
                                ePos = u1.usedSize;
                            }
                            newNode.elements.set(ePos, element);
                            ePos++;
                            if (u2 != null) {
                                for (int j = 0; j < u2.usedSize; j++) {
                                    newNode.elements.set(j + ePos, u2.elements.get(j));
                                    newNode.sub.set(j + ePos, u2.sub.get(j));
                                }
                                newNode.sub.set(u2.usedSize + ePos, u2.sub.get(u2.usedSize));
                            }
                            newNode.usedSize = u1.usedSize+u2.usedSize+1;

                            // 删除element
                            for (int j = pos; j < cur.usedSize-1; j++) {
                                cur.elements.set(j, cur.elements.get(j + 1));
                                cur.sub.set(j, cur.sub.get(j+1));
                            }
                            cur.elements.set(cur.usedSize-1, null);
                            cur.sub.set(cur.usedSize - 1, cur.sub.get(cur.usedSize));
                            cur.sub.set(cur.usedSize, null);
                            cur.sub.set(pos, newNode);
                            cur.usedSize--;
                            if (root == cur && cur.usedSize == 0) {
                                root = cur.sub.get(0);
                            }

                            // 递归删除element
                            cur = newNode;
                            break;
                        }

//                        else if (u1 != null && u1.usedSize < t && u2 != null && u2.usedSize < t) {
//                            // 要删除的元素，放到U1，再u2合并到U1，从当前节点删除元素，然后递归删除u2
//                            u1.elements.set(u1.usedSize, element);
//                            u1.usedSize++;
//                            for (int j = 0; j < u2.usedSize; j++) {
//                                u1.elements.set(u1.usedSize + j, u2.elements.get(j));
//                                u1.sub.set(u1.usedSize+j, u2.sub.get(j));
//                            }
//                            u1.sub.set(u1.usedSize+u2.usedSize, u2.sub.get(u2.usedSize));
//
//                            for (int j = pos; j < cur.usedSize-1; j++) {
//                                cur.elements.set(j, cur.elements.get(j + 1));
//                                cur.sub.set(j, cur.sub.get(j+1));
//                            }
//                            cur.elements.set(cur.usedSize-1, null);
//                            cur.sub.set(cur.usedSize - 1, cur.sub.get(cur.usedSize));
//                            cur.sub.set(cur.usedSize, null);
//                            cur.usedSize--;
//                            cur.sub.set(pos, u1);
//                            remove(element, u1);
//                        }
                    }
                }

            }
        }
    }

    /**
     * @param node
     * @return
     */
    private E findMaxElement(BNode<E> node){
        if (node == null) {
            return null;
        }
        BNode<E> ebNode = node.sub.get(node.usedSize);
        if (ebNode == null) {
            return node.elements.get(node.usedSize -1);
        }
        while (ebNode.sub.get(ebNode.usedSize) != null) {
            ebNode = ebNode.sub.get(ebNode.usedSize);
        }
        return ebNode.elements.get(ebNode.usedSize);
    }

    private E findMinElement(BNode<E> node){
        if (node == null) {
            return null;
        }
        BNode<E> ebNode = node.sub.get(0);
        if (ebNode == null) {
            return node.elements.get(0);
        }
        while (ebNode.sub.get(0) != null) {
            ebNode = ebNode.sub.get(0);
        }
        return ebNode.elements.get(0);
    }

    private boolean isLeafNode(BNode<E> node){
        if (node == null || node.usedSize == 0) {
            return false;
        }
        for (int i = 0; i < node.usedSize; i++) {
            if (node.sub.get(i) != null) {
                return false;
            }
        }
        return true;
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

package com.ff.tree.binarytree.redblack;

/**
 * 红黑树
 */
public class RedBlackTree<E extends Comparable<E>> {

    int size = 0;

    RBNode<E> root;

    public RedBlackTree(){

    }

    /**
     * 添加新元素
     * @param element
     * @return
     */
    public boolean add(E element){
        if (root == null) {
            root = new RBNode<>(element, null);
            size++;
            return true;
        }

        RBNode<E> node = new RBNode<>(element, null);

        RBNode<E> cur = null;
        RBNode<E> x = this.root;
        while (x != null) {
            cur = x;
            int cmp = node.element.compareTo(x.element);
            if (cmp < 0) {
                cur = x.left;
            }else{
                cur = x.right;
            }
        }

        node.parents = cur;

        if (cur == null) {
            root = node;
        }else{
            int cmp = node.element.compareTo(cur.element);
            if (cmp < 0) {
                cur.left = node;
            }else{
                cur.right = node;
            }
        }

        balanced(node);

        return false;
    }

    /**
     * 删除
     * 1. 找到要删除的节点
     *      1. 如果要删除的节点，有两个子节点，则用后继节点替代，并删除后继节点。
     *      2. 如果要删除的节点，是叶子节点，直接删除；只有一个子节点，则直接删除并更新节点关系。
     * 2. 修复红黑树平衡
     *      1. 如果要删除的节点是红色，则直接删除，将后继节点替换过来，后继节点涂为当前节点颜色。然后删除后继节点。
     *      2. 如果要删除的节点是黑色（u:当前删除节点，x：u的后继节点，y：x的后继节点）
     *          1. uxy: 黑红黑
     *              1. 删除u，x替代，x改成u的颜色。然后删除x。
     *          2. uxy: 黑黑红
     *              1. 删除u，x替代，y的颜色改成黑色。然后删除x。
     *          3. uxy: 黑黑红
     *              1.
     *          4. uxy: 黑红红(不满足红黑树性质，不存在）
     *          1. 如果兄弟节点是红色，（父节点必定是黑色，不会有两个连续的红色） -> 兄弟节点变黑，父节点变黑，对父节点左旋或者右旋，继续修复父节点
     *          2. 如果兄弟节点是黑色，且兄弟节点的两个子节点也是黑色 -> 兄弟节点变红，将父节点变为当前节点，继续修复
     *          3.
     * @param node
     */
    public void delete(RBNode<E> node) {
        // 两个子节点
        if (node.left != null && node.right != null) {
            // 左右子节点都存在，则找到后继节点，替换值，然后删除后继节点
            RBNode<E> successor = successor(node);
            node.element = successor.element;
            node = successor;
        }

        // todo
        RBNode<E> replace = node.left != null? node.left: node.right;
        if(replace != null){
            // 只有一个子节点，则将子节点连接到父节点
            replace.parents = node.parents;
            if(node.parents == null){
                root = replace;
            } else if (node == node.parents.left) {
                node.parents.left = replace;
            }else{
                node.parents.right = replace;
            }

            node.left = node.right = node.parents = null;

            if (node.black == true) {
                fixAfterDelete(replace);
            }
        } else if (node.parents == null) {
            // 没有子节点，也没有父节点，只有root节点，直接删除
            root = null;
        }else {
            // 没有子节点

        }


    }

    /**
     * 找到后继节点
     * @param node
     * @return
     */
    private RBNode<E> successor(RBNode<E> node) {
        if (node == null) {
            return null;
        }
        RBNode<E> cur = node.right;
        if(cur != null) {
            // 如果存在右子树，则后继节点在右子树上
            while (cur != null) {
                cur = cur.left;
            }
            return cur;
        }else{
            // 如果不存在右子树，则后继节点那在父节点上（比当前节点大，那必须当前节点是父节点的左子节点）
            while (cur.parents != null && cur == cur.parents.right) {
                // 如果当前节点存在父节点，且当前节点是父节点的右子节点，那说明父节点小于当前节点，不符合，需要继续往上寻找
                // 直到找到 当前节点是父节点的左子节点。
                cur = cur.parents;
            }
            // 父节点就是寻找的后继接地那，但可能找到root节点都没找到
            return cur.parents;
        }
    }

    /**
     * 删除后，修复树平衡
     * @param node
     */
    private void fixAfterDelete(RBNode<E> node){

    }

    /**
     * 平衡红黑树
     * 1. 如果插入的是根节点，则根节点涂黑
     * 2. 如果插入的不是根节点，则
     *      0. 如果父节点是黑色，则不用处理。（当前为红色）
     *      1. 如果父节点是红色，且叔叔节点也是红色。 --> 父节点和叔叔节点涂黑，祖父节点涂红，当前节点指向祖父节点，则进入下面两种情况之一。
     *      2. 如果父节点是红色，叔叔节点是黑色，且当前节点是父节点的右子节点。 --> 以父节点为支点左旋，然后当前节点指向父节点。旋转后变成下面情况。
     *      3. 如果父节点是红色，叔叔节点是黑色，且当前节点是父节点的左子节点。 --> 将父节点涂黑，祖父节点涂红。以祖父节点为支点进行右旋转。
     * @param node
     */
    private void balanced(RBNode<E> node) {
        RBNode<E> parent, grand;

        while (((parent = node.parents) != null) && isRed(parent)){
            grand = parent.parents;

            if (parent == grand.left) {
                RBNode<E> uncle = grand.right;

                // case 2.1
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setRed(grand);
                    node = grand;
                    continue;
                }

                // case 2.2
                if (node == parent.right) {
                    leftRotate(parent);
                    RBNode<E> tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // case 2.3
                setBlack(parent);
                setRed(grand);
                rightRotate(grand);
            }else{
                RBNode<E> uncle = grand.left;

                // case 2.1
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setRed(grand);
                    node = grand;
                    continue;
                }

                // case 2.2
                if (node == parent.left) {
                    rightRotate(parent);
                    RBNode<E> tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // case 2.3
                setBlack(parent);
                setRed(grand);
                leftRotate(grand);
            }
        }
    }

    private boolean isRed(RBNode<E> node) {
        return !node.black;
    }

    private boolean isBlack(RBNode<E> node) {
        return node.black;
    }

    private void setBlack(RBNode<E> node) {
        node.black = true;
    }

    private void setRed(RBNode<E> node) {
        node.black = false;
    }

    private void leftRotate(RBNode<E> node) {
        RBNode<E> right = node.right;
        node.right = right.left;
        if (right.left != null) {
            right.left.parents = node;
        }

        right.parents = node.parents;
        if (node.parents == null) {
            root = right;
        } else if (node == node.parents.left) {
            node.parents.left = right;
        } else if (node == node.parents.right) {
            node.parents.right = right;
        }

        right.left = node;
        node.parents = right;
    }

    private void rightRotate(RBNode<E> node) {
        RBNode<E> left = node.left;
        node.left = left.right;
        if (left.right != null) {
            left.right.parents = node;
        }

        left.parents = node.parents;
        if (node.parents == null) {
            root = left;
        } else if (node == node.parents.left) {
            node.parents.left = left;
        } else if (node == node.parents.right) {
            node.parents.right = left;
        }

        left.right = node;
        node.parents = left;
    }

}

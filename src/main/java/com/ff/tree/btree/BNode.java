package com.ff.tree.btree;

import java.util.ArrayList;
import java.util.List;

public class BNode<E> {

    BNode<E> parents;

    List<BNode<E>> sub;

    List<E> elements;

    int usedSize = 0;

    public BNode(int m){
        elements = new ArrayList<E>(m);
        sub = new ArrayList<>(m + 1);
        for (int i = 0; i < m; i++) {
            elements.add(null);
        }
        for (int i = 0; i < m+1; i++) {
            sub.add(null);
        }
    }

    public BNode(int m, BNode<E> parents){
        this.parents = parents;
        elements = new ArrayList<E>(m);
        sub = new ArrayList<>(m + 1);
        for (int i = 0; i < m; i++) {
            elements.add(null);
        }
        for (int i = 0; i < m+1; i++) {
            sub.add(null);
        }
    }

}

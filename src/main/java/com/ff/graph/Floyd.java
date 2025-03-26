package com.ff.graph;

import java.util.Arrays;

/**
 * 弗洛伊德算法
 * 每一对顶点间的最短路径
 */
public class Floyd {

    int vNum;
    int[][] map;

    public static void main(String[] args) {
        Floyd floyd = new Floyd();
        floyd.vNum = 7;
        floyd.map = new int[][]{
                {0, 12, -1, -1, -1, 16, 14},
                {12, 0, 10, -1, -1, 7, -1},
                {-1, 10, 0, 3, 5, 6, -1},
                {-1, -1, 3, 0, 4, -1, -1},
                {-1, -1, 5, 4, 0, 2, 8},
                {16, 7, 6, -1, 2, 0, 9},
                {14, -1, -1, -1, 8, 9, 0}
        };
        floyd.floyd(new int[floyd.vNum][floyd.vNum]);
    }

    public void floyd(int[][] dist){
        // 初始化dist
        for (int i = 0; i < vNum; i++) {
            for (int j = 0; j < vNum; j++) {
                dist[i][j] = map[i][j] < 0?Integer.MAX_VALUE:map[i][j];
            }
        }

        // 计算 i到j的路径中，经过k顶点后的距离，并与i到j的距离进行比较
        for (int k = 0; k < vNum; k++) {
            for (int i = 0; i < vNum; i++) {
                for (int j = 0; j < vNum; j++) {
                    int newDist = (dist[i][k] == Integer.MAX_VALUE || dist[k][j] == Integer.MAX_VALUE) ? Integer.MAX_VALUE : dist[i][k] + dist[k][j];
                    if (dist[i][j] > newDist) {
                        dist[i][j] = newDist;
                    }
                }
            }
        }

        for (int i = 0; i < vNum; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < vNum; j++) {
                s.append(dist[i][j]).append(",");
            }
            System.out.println(s);
        }
    }
}

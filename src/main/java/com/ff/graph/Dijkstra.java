package com.ff.graph;

import java.util.Arrays;

/**
 * 迪杰斯特拉算法
 * 最短路径
 */
public class Dijkstra {

    int vNum ;

    int[][] map;


    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.vNum = 6;
        dijkstra.map = new int[][]{
                {0, -1, 10, -1, 30, 100},
                {-1, 0, 5, -1, -1, -1},
                {10, 5, 0, 50, -1, -1},
                {-1, -1, 50, 0, 20, 10},
                {30, -1, -1, 20, 0, 60},
                {100, -1, -1, 10, 60, 0}
        };
        dijkstra.dijkstra(0, new int[6], new int[6]);
    }


    /**
     *
     * @param vs 源点
     * @param prev 到第i个顶点的前置顶点
     * @param dist 到第i个顶点的最短路径
     */
    public void dijkstra(int vs, int[] prev, int[] dist){
        boolean[] flag = new boolean[vNum];
        for (int i = 0; i < vNum; i++) {
            flag[i] = false;
            prev[i] = 0;
            dist[i] = map[vs][i] < 0 ? Integer.MAX_VALUE: map[vs][i];
        }

        flag[vs] = true;
        dist[vs] = 0;

        // 每次循环找到一个顶点的最短路径
        for (int i = 0; i < vNum - 1; i++) {
            // 从没确定最短路径的顶点中，寻找距离当前节点的路径最短的顶点
            int minIdx = 0;
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < vNum; j++) {
                if (flag[j] == false && dist[j] < min) {
                    min = dist[j];
                    minIdx = j;
                }
            }

            flag[minIdx] = true;

            // 以minIdx这个顶点为中间点，比较源点经过这个顶点去其它顶点的距离 与 源点到其它顶点的距离 的大小，来修正dist
            for (int j = 0; j < vNum; j++) {
                int newDist = map[minIdx][j] < 0? Integer.MAX_VALUE : map[minIdx][j] + min;
                if (flag[j] == false && newDist < dist[j]) {
                    dist[j] = newDist;
                    prev[j] = minIdx;
                }
            }
        }

        System.out.println("vs:" + vs);
        System.out.println(Arrays.toString(dist));

    }
}

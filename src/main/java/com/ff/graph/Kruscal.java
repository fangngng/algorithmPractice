package com.ff.graph;

import com.ff.sort.FastSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Kruscal算法
 * 最小生成树
 */
public class Kruscal {

    public static void main(String[] args) {
        GraphMap<Integer> graphMap = new GraphMap<>();
        graphMap.vNum = 6;
        graphMap.aNum = 9;
        graphMap.nodes = new Integer[6];
        graphMap.map = new int[][]{
                {0,6,1,5,-1,-1},
                {6,0,5, -1, 3, -1},
                {1,5,0, 7, 5, 4},
                {5,-1,7, 0, -1, 2},
                {-1,3,5, -1, 0, -6},
                {-1,-1,4, 2, 6, 0}
        };

        Edge[] eList = new Edge[graphMap.aNum];
        int k = 0;
        for (int i = 0; i < graphMap.vNum; i++) {
            for (int j = i+1; j < graphMap.vNum; j++) {
                if (graphMap.map[i][j] > 0) {
                    Edge edge = new Edge();
                    edge.x = i;
                    edge.y = j;
                    edge.weight = graphMap.map[i][j];
                    eList[k++] = edge;
                }
            }
        }

        // 边増序排列
        FastSort.fastSort(eList, 0, k-1);

        Edge[] result = Kruscal.kruscal(eList, graphMap.vNum);
        System.out.println(Arrays.toString(result));
    }

    public static class Edge implements Comparable<Edge> {
        // 顶点序号
        int x = 0;
        int y = 0;
        // 权重
        int weight = 0;

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(o.weight, weight);
        }

        @Override
        public String toString() {
            return "x:" + x + ",y:" + y + ",weight:" + weight;
        }
    }

    public static Edge[] kruscal(Edge[] edges, int nodeCount) {
        int[] sets = new int[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            sets[i] = i;
        }

        Edge[] result = new Edge[nodeCount];
        int k = 0;
        int j = 0;
        while (k <= nodeCount - 1 && j < edges.length) {
            int s1 = edges[j].x;
            int s2 = edges[j].y;
            if (sets[s1] != sets[s2]) {
                result[k] = edges[j];
                k++;
                sets[s2] = sets[s1];
            }
            j++;
        }

        return result;
    }

}

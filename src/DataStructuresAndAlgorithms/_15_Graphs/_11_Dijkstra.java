package _15_Graphs;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.*;

public class _11_Dijkstra {
    public static class Edge {
        int v;
        int weight;

        Edge(int weight, int v) {
            this.weight = weight;
            this.v = v;
        }
    }

    public static class Graph {
        int size;
        ArrayList<ArrayList<Edge>> list;

        Graph(int n) {
            this.size = n;
            list = new ArrayList<>();
            ;
            for (int i = 0; i < n; i++) {
                ArrayList<Edge> l = new ArrayList<>();
                list.add(l);
            }
        }

        void addEdge(int u, int v, int weight) {
            list.get(u).add(new Edge(weight, v));
            list.get(v).add(new Edge(weight, u));
        }

        void addEdgeDirected(int u, int v, int weight) {
            list.get(u).add(new Edge(weight, v));
        }

        void print() {
            for (int i = 0; i < list.size(); i++) {
                System.out.print(i + "->");
                for (Edge e : list.get(i)) {
                    System.out.print(e.v + "," + e.weight + " ->");
                }
                System.out.println();
            }
        }

        ArrayList<Edge> get(int u) {
            return list.get(u);
        }
    }

    static class Pair {
        int key;
        int value;

        Pair(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    static class MinHeap {
        Pair arr[];
        int size;
        int capacity;
        int indexes[];

        MinHeap(int capacity) {
            this.arr = new Pair[capacity];
            this.size = 0;
            this.capacity = capacity;
        }

        MinHeap(Pair input[]) {
            this.arr = Arrays.copyOf(input, input.length);
            this.indexes = new int[input.length];
            for (int i = 0; i < input.length; i++)
                indexes[i] = i;
            this.size = arr.length;
            this.capacity = arr.length;
        }

        void build() {
            for (int i = size / 2; i >= 0; i--)
                heapify(i);
        }

        void heapify(int i) {
            int l = 2 * i + 1;
            int r = 2 * i + 2;
            int next = i;
            if (l < size && arr[next].value > arr[l].value) {
                next = l;
            }
            if (r < size && arr[next].value > arr[r].value) {
                next = r;
            }
            if (next != i) {
                swap(i, next);
                heapify(next);
            }

        }

        void swap(int a, int b) {
            int the_element_to_be_swapped_original_node = arr[a].key;
            int the_element_to_be_swapped_with_original_node = arr[b].key;

            indexes[the_element_to_be_swapped_original_node] = b;
            indexes[the_element_to_be_swapped_with_original_node] = a;

            Pair temp = arr[a];
            arr[a] = arr[b];
            arr[b] = temp;
        }

        Pair remove_element() {
            if (size == 0)
                return null;
            Pair top = arr[0];
            swap(0, size - 1);
            size--;
            heapify(0);
            return top;
        }

        void add_element(Pair element) {
            if (size == capacity) {
                System.out.println("Overflow");
                return;
            }
            arr[size] = element;
            size++;
            int i = size - 1;
            while (arr[i].value < arr[(i - 1) / 2].value) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }

        }

        void decrease_node(int node, int value) {
            Pair modified_node = new Pair(node, value);
            int the_current_index_of_the_node = indexes[node];
            decrease_index(the_current_index_of_the_node, modified_node);
        }

        void decrease_index(int index, Pair value)//instead of just index we could simply use hashmap values
        {
            arr[index] = value;
            int i = index;
            while (arr[i].value < arr[(i - 1) / 2].value) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
        }

        Pair peek() {
            return arr[0];
        }
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line[] = (br.readLine()).split(" ");
        int n = Integer.parseInt(line[0]);
        int e = Integer.parseInt(line[1]);
//        System.out.println(n+" "+e);
        Graph graph = new Graph(n);
        for (int i = 1; i <= e; i++) {
            line = (br.readLine()).split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            int weight = Integer.parseInt(line[2]);
//            System.out.println(u+" "+v+" "+weight);
            graph.addEdge(u, v, weight);
        }
        long ans[] = findShortestPath(graph);
        for (int i = 0; i < ans.length; i++)
            System.out.print(ans[i] + " ");

    }

    static long[] findShortestPath(Graph graph) {
            boolean visited[] = new boolean[graph.size];
        Pair pair[] = new Pair[graph.size];
        pair[0] = new Pair(0, 0);
        for (int i = 1; i < graph.size; i++)
            pair[i] = new Pair(i, (int) Math.pow(10, 9));
        MinHeap mh = new MinHeap(pair);
        mh.build();

        long distance[] = new long[graph.size];
        while (mh.size != 0) {
            Pair u = mh.remove_element();
            visited[u.key] = true;
            distance[u.key] = u.value;
            for (Edge e : graph.get(u.key)) {
                if (!visited[e.v]) {
                    if (mh.arr[mh.indexes[e.v]].value > (u.value + e.weight)) {
                        mh.decrease_node(e.v, (u.value + e.weight));
                    }
                }
            }
        }
        return distance;


    }
}
/*
4 5
0 1 5
0 2 10
1 2 3
1 3 20
2 3 2
 */
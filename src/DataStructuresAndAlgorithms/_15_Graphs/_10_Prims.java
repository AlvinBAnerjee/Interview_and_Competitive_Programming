package _15_Graphs;

import java.util.*;

public class _10_Prims {
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
                indexes[i] = i;//this means element i currently at i
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
            int the_element_to_be_swapped_original_node = arr[a].key;//absolutely the original position they were at
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

        void decrease_node(int node, int value) {//original node, the new value
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


    public static void main(String[] args) {
        System.out.println("Enter the number of vertices in the graph and the no of edges");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println("Enter the number of Edges");
        int e = sc.nextInt();
        System.out.println("Enter u v  and weight for the edges");
        Graph graph = new Graph(n);
        for (int i = 1; i <= e; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int weight = sc.nextInt();
            graph.addEdge(u, v, weight);
        }
        int parent[]=new int[graph.size];
        System.out.print(findMSTValue(graph,parent));

    }

    static int findMSTValue(Graph graph,int parent[]) {
        for (int i=0;i<parent.length;i++)
            parent[i]=-1;
        boolean visited[] = new boolean[graph.size];
        Pair pair[] = new Pair[graph.size];
        pair[0] = new Pair(0, 0);
        for (int i = 1; i < graph.size; i++)
            pair[i] = new Pair(i, Integer.MAX_VALUE);
        MinHeap mh = new MinHeap(pair);
        mh.build();

        int total_weight = 0;
        while (mh.size != 0) {
            Pair u = mh.remove_element();
            visited[u.key] = true;
            total_weight = total_weight + u.value;
            for (Edge e : graph.get(u.key)) {
                if (!visited[e.v]) {
                    if (e.weight < mh.arr[mh.indexes[e.v]].value) {

                        mh.decrease_node(e.v, e.weight);
                        parent[e.v]=u.key;
                    }
                }
            }
        }
        System.out.println("Parent array  is "+Arrays.toString(parent));
        return total_weight;


    }

    //Efficient Prims Algorithm code
    public static class mycomparator implements Comparator<Pair>
    {
        @Override
        public int compare(Pair p1,Pair p2)
        {
            return p1.value-p2.value;
        }
    }
    static int spanningTree(int V, ArrayList<ArrayList<ArrayList<Integer>>> adj)
    {
        int mst=0;
        boolean visited[]=new boolean[V];
        PriorityQueue<Pair> pq=new PriorityQueue<>(new mycomparator());
        pq.add(new Pair(0,0));
        for(int i=1;i<V;i++)
            pq.add(new Pair(i,Integer.MAX_VALUE));

        while(!pq.isEmpty())
        {
            Pair top=pq.remove();
            if(visited[top.key])
                continue;
            if(top.key==Integer.MAX_VALUE)
                break;
            visited[top.key]=true;
            int u=top.key;
            mst=mst+top.value;
            for(int i=0;i<adj.get(u).size();i++)
            {
                int v=adj.get(u).get(i).get(0);
                int weight=adj.get(u).get(i).get(1);
                pq.add(new Pair(v,weight));
            }
        }
        return mst;

    }
}
/*
7 9
0 1 28
1 2 16
2 3 12
3 4 22
4 5 25
5 0 10
4 6 24
6 1 14
6 3 18
 */

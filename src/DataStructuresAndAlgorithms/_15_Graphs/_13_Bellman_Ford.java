package _15_Graphs;
import javax.naming.InsufficientResourcesException;
import java.util.*;
public class _13_Bellman_Ford {
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

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int e=sc.nextInt();
        Graph graph=new Graph(n);
        Graph reverse=new Graph(n);
        for (int i=0; i<e;i++)
        {
            int u=sc.nextInt();
            int v=sc.nextInt();
            int weight=sc.nextInt();
            graph.addEdgeDirected(u,v,weight);

        }
        System.out.println(Arrays.toString(bellmanFord(graph,0)));

    }
    static int[] bellmanFord(Graph graph,int source) {
        int distance[] = new int[graph.size];
        for (int i = 0; i < graph.size; i++)
            distance[i] = Integer.MAX_VALUE;
        distance[source] = 0;
        for (int x = 1; x <= graph.size - 1; x++) {
            for (int i = 0; i < graph.size; i++) {
                if (distance[i] != Integer.MAX_VALUE) {
                    for (Edge edge : graph.get(i)) {
                        if (distance[edge.v]>distance[i]+ edge.weight)
                            distance[edge.v]=distance[i]+ edge.weight;
                    }
                }
            }
        }
        return distance;
    }
}
/*
4 5
0 1 1
0 2 4
1 2 -3
1 3 2
2 3 3
 */

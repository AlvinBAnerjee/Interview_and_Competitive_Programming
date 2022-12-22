package _21_Disjoint_sets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class _4_Krushkals {
    public static class Edge {
        int u;
        int v;
        int weight;


        Edge(int u, int v,int weight) {
            this.u =u;
            this.weight = weight;
            this.v = v;
        }
    }

    public static class Graph {
        int V;
        ArrayList<Edge> E;
        Graph(int V)
        {
            this.V=V;
            this.E=new ArrayList<>();

        }
        void addEdge(int u,int v, int weight)
        {
            E.add(new Edge(u,v,weight));
        }
    }
    static class DisjointSet{
        int parent[];
        int rank[];
        DisjointSet(int n)
        {
            parent=new int[n];
            rank=new int[n];
            for (int i=0;i<n;i++) {
                parent[i] = i;
                rank[i]=0;
            }
        }
        int find(int x)
        {
            if (parent[x]==x)
                return x;
            parent[x]= find(parent[x]);
            return parent[x];
        }
        void union(int x,int y)
        {
            int x_top=find(x);
            int y_top=find(y);
            if (rank[x_top]>rank[y_top])
            {
                parent[y_top]=x_top;
            }
            else if (rank[y_top]>rank[x_top])
            {
                parent[x_top]=y_top;
            }
            else
            {
                parent[x_top]=y_top;
                rank[y_top]++;

            }

        }
    }
    public static class sort_with_weight implements Comparator<Edge>
    {
        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.weight-o2.weight;
        }
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int V=sc.nextInt();
        int E=sc.nextInt();
        Graph graph=new Graph(V);
        for (int i=0;i<E;i++)
        {
            int u=sc.nextInt();
            int v=sc.nextInt();
            int weight=sc.nextInt();
            graph.addEdge(u,v,weight);
        }
        Graph tree=new Graph(V);
        System.out.println(findMST(graph,tree));

    }
    static  int findMST(Graph graph,Graph tree)
    {
        PriorityQueue<Edge> pq=new PriorityQueue<>(new sort_with_weight());
        DisjointSet ds=new DisjointSet(graph.V);
        pq.addAll(graph.E);
        int total_weight=0;
        while (!pq.isEmpty())
        {
            Edge e=pq.remove();
            int source=e.u;
            int destination=e.v;
            int weight=e.weight;
            int find_s=ds.find(source);
            int find_d=ds.find(destination);
            if (find_s!=find_d)
            {
                ds.union(source,destination);
                total_weight=total_weight+weight;
                tree.addEdge(source,destination,weight);
            }
        }
        return total_weight;

    }

}

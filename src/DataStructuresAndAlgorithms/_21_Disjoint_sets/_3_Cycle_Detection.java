package _21_Disjoint_sets;

import java.util.ArrayList;
import java.util.Scanner;

public class _3_Cycle_Detection {
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
        System.out.println(findCycle(graph));

    }
    static  boolean findCycle(Graph graph)
    {
        DisjointSet ds=new DisjointSet(graph.V);
        for (Edge e : graph.E)
        {
            int source=e.u;
            int destination=e.v;
            if (ds.find(source)==ds.find(destination))
                return true;
            else
                ds.union(source,destination);
        }
        return false;
    }
}

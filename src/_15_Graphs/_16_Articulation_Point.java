package _15_Graphs;
import java.util.*;
/*
A O(V+E) algorithm to find all Articulation Points (APs)
The idea is to use DFS (Depth First Search). In DFS, we follow vertices in tree form called DFS tree.
In DFS tree, a vertex u is parent of another vertex v, if v is discovered by u
(obviously v is an adjacent of u in graph). In DFS tree, a vertex u is articulation point if
one of the following two conditions is true.
1) u is root of DFS tree and it has at least two children.
2) u is not root of DFS tree and it has a child v such that no vertex in subtree rooted with v has a back edge to one of the ancestors (in DFS tree) of u.
 */
public class _16_Articulation_Point {
    public static class Edge{
        int v;
        int weight;

        Edge(int weight,int v)
        {
            this.weight=weight;
            this.v=v;
        }
    }
    public static class Graph{
        ArrayList<ArrayList<Edge>> list;
        int size;
        Graph(int n)
        {
            this.size=n;
            list=new ArrayList<>();
            ;
            for(int i=0;i<n;i++) {
                ArrayList<Edge> l = new ArrayList<>();
                list.add(l);
            }
        }
        void addEdge(int u,int v,int weight)
        {
            list.get(u).add(new Edge(weight,v));
            list.get(v).add(new Edge(weight,u));
        }
        void addEdgeDirected(int u,int v,int weight)
        {
            list.get(u).add(new Edge(weight,v));
        }
        void print()
        {
            for (int i=0;i<list.size();i++)
            {
                System.out.print(i+"->");
                for (Edge e : list.get(i))
                {
                    System.out.print(e.v+","+e.weight+" ->");
                }
                System.out.println();
            }
        }
        ArrayList<Edge> get(int u)
        {
            return list.get(u);
        }
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int v=sc.nextInt();
        int e=sc.nextInt();
        Graph graph=new Graph(v);
        for (int i=0;i<e;i++)
        {
            int a=sc.nextInt();
            int b=sc.nextInt();
            graph.addEdge(a,b,1);
        }
        articulationPoint(graph);

    }
    static int time=0;
    static void  articulationPoint(Graph graph)
    {
        boolean visited[]=new boolean[graph.size];
        int discovery[]=new int[graph.size];
        int low[]=new int[graph.size];
        boolean ap[]=new boolean[graph.size];
        int parent[]=new int[graph.size];
        for (int i=0;i< graph.size;i++)
        {
            parent[i]=-1;
        }
        time=0;
        APutil(graph,3,visited,discovery,low,ap,parent);

        for (int i=0;i<graph.size;i++)
            if (ap[i])
                System.out.print(i+" ");

    }
    static void APutil(Graph graph,int vertex,boolean visited[],int discovery[],int low[],boolean ap[],int parent[])
    {
        visited[vertex]=true;
        int child_count=0;
        low[vertex]=discovery[vertex]=++time;
        for (Edge e: graph.get(vertex))
        {
            if (!visited[e.v])
            {
                child_count++;
                parent[e.v]=vertex;
                APutil(graph,e.v,visited,discovery,low,ap,parent);
                low[vertex]=Math.min(low[vertex],low[e.v]);
                if (parent[vertex]==-1&& child_count>1)
                    ap[vertex]=true;
                 if(parent[vertex]!=-1&&low[e.v]>=discovery[vertex])
                {
                    ap[vertex]=true;
                }
            }
            else if (e.v!=parent[vertex])
                low[vertex]=Math.min(low[vertex],discovery[e.v]);
        }
    }
}
/*
5 5
0 1
1 2
2 0
2 3
3 4
 */
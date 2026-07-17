package _15_Graphs;
import java.util.*;
/*
a edge u-v is a bridge if low[v]>discovery[u]
 */
public class _17_Bridges {
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
        bridge(graph);

    }
    static int time=0;
    static void  bridge(Graph graph)
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
        Bridge_util(graph,0,visited,discovery,low,ap,parent);

        for (int i=0;i<graph.size;i++)
            if (ap[i]) {
                System.out.print("("+parent[i] + "->"+i+"),");
            }

    }
    static void Bridge_util(Graph graph,int vertex,boolean visited[],int discovery[],int low[],boolean ap[],int parent[])
    {
        visited[vertex]=true;

        low[vertex]=discovery[vertex]=++time;
        for (Edge e: graph.get(vertex))
        {
            if (!visited[e.v])
            {
                parent[e.v]=vertex;
                Bridge_util(graph,e.v,visited,discovery,low,ap,parent);
                low[vertex]=Math.min(low[vertex],low[e.v]);
                 if(low[e.v]>discovery[vertex])
                {
                    ap[e.v]=true;
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
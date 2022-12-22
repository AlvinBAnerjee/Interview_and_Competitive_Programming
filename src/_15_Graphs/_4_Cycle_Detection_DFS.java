package _15_Graphs;
import java.util.*;
/*
Input
4 5
0 1
1 2
2 3
0 2
1 3
 */
public class _4_Cycle_Detection_DFS {
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
        System.out.println("Enter the number of vertices in the graph and the no of edges");
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        System.out.println("Enter the number of Edges");
        int e=sc.nextInt();
        System.out.println("Enter u v for the edges");
        Graph graph=new Graph(n);
        for (int i=1;i<=e;i++)
        {
            int u=sc.nextInt();
            int v=sc.nextInt();
            graph.addEdge(u,v,1);
        }
        System.out.print(containsCycle(graph));

    }
    static boolean containsCycle(Graph graph)
    {
        boolean visited[]=new boolean[graph.size];
        for (int i=0;i<graph.size;i++)
        {
            if (visited[i]==false&&DFSutil(i,-1,visited,graph))
                return true;
        }
        return false;
    }
    static boolean DFSutil(int start,int parent,boolean visited[],Graph graph)
    {
        visited[start]=true;
        for (Edge e: graph.get(start))
        {
            if (!visited[e.v])
            {
                if (DFSutil(e.v,start,visited,graph))
                {
                    return true;
                }

            }
            else
            {
                if (e.v!=parent)
                    return true;
            }
        }
        return false;
    }
}

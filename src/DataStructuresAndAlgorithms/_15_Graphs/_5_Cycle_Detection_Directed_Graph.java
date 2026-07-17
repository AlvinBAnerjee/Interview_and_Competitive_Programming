package _15_Graphs;
/*
Input
6 7
0 1
1 2
2 3
3 4
4 5
5 2
5 3
 */
import java.util.*;
public class _5_Cycle_Detection_Directed_Graph {
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
            graph.addEdgeDirected(u,v,1);
        }
        System.out.println(containsCylce(graph));
    }
    static boolean containsCylce(Graph graph)
    {
        boolean visited[]=new boolean[graph.size];
        boolean stack[]=new boolean[graph.size];
        for (int i=0;i<graph.size;i++)
        {
            if (!visited[i])
            {
                if (DFSutil(graph,i,visited,stack)==true)
                    return true;
            }
        }
        return false;
    }
    static boolean DFSutil(Graph grah,int u,boolean visited[],boolean stack[])
    {
        visited[u]=true;
        stack[u]=true;
        for (Edge e : grah.get(u))
        {
            if (!visited[e.v])
            {
                if (DFSutil(grah,e.v,visited,stack))
                    return true;
            }
            else
            {
                if (stack[e.v]==true)
                    return true;
            }
        }
        stack[u]=false;
        return false;
    }
}

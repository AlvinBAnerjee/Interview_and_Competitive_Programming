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
public class _3_Single_Source_Shortest_Path {
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
        int arr[]=new int[n];
        findShortestPath(0,graph,arr);
        System.out.println(Arrays.toString(arr));
    }
    static void findShortestPath(int start,Graph graph,int dist[])
    {
        boolean visited[]=new boolean[graph.size];
        Queue<Integer> queue=new LinkedList<>();
        queue.add(start);
        dist[start]=0;
        visited[start]=true;
        while (!queue.isEmpty())
        {
            int top=queue.remove();
            for (Edge e: graph.get(top))
            {
                if (visited[e.v]==false)
                {
                    queue.add(e.v);
                    visited[e.v]=true;
                    dist[e.v]=dist[top]+1;
                }
            }
        }
    }
}

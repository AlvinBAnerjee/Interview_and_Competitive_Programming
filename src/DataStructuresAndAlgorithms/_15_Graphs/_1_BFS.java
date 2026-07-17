package _15_Graphs;
import java.util.*;
public class _1_BFS {
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
        findBFS(graph,0);



    }
    static void findBFS(Graph graph,int start)
    {
        boolean visited[]=new boolean[graph.size];
        BFSutil(start,graph,visited);

        for (int i=1;i<graph.size;i++) {
            BFSutil(i, graph, visited);
            System.out.println();
        }
    }
    static void BFSutil(int n,Graph graph,boolean visited[])
    {
        if(visited[n])
            return;
        Queue<Integer> queue=new LinkedList<>();
        queue.add(n);
        while (!queue.isEmpty()) {
            //System.out.println(queue);
            int top=queue.peek();
            visited[top]=true;
            System.out.print(top+" ");
            for (Edge e : graph.get(queue.remove())) {
                int v = e.v;
                if (!visited[v]) {
                    queue.add(v);
                    visited[v]=true;
                }
            }
        }


    }
}

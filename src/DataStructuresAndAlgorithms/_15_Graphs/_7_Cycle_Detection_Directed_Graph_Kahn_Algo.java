package _15_Graphs;
import  java.util.*;
public class _7_Cycle_Detection_Directed_Graph_Kahn_Algo {
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
        System.out.println(containsCycle(graph));
    }
    static boolean containsCycle(Graph graph)
    {
        int ans=topologicalSort(graph);
        return ans!=graph.size;
    }
    static  int topologicalSort(Graph graph)
    {
        int indegree[]=new int[graph.size];
        for (int i=0;i<graph.size;i++)
        {
            for (Edge e : graph.get(i))
            {
                indegree[e.v]++;
            }
        }

        Queue<Integer> queue=new LinkedList<>();
        int count=0;
        for (int i=0;i<graph.size;i++)
            if (indegree[i]==0)
                queue.add(i);
        while (!queue.isEmpty())
        {
            int u=queue.remove();
            count++;
            for (Edge e:graph.get(u))
            {
                indegree[e.v]--;
                if (indegree[e.v]==0)
                    queue.add(e.v);
            }
        }
        return count;
    }
}

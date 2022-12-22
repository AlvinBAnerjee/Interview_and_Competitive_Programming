package _15_Graphs;
import java.lang.reflect.Array;
import java.util.*;
/*
6 7
0 1 2
1 2 3
2 3 6
0 4 1
4 2 2
4 5 4
5 3 1
 */
public class _9_Shortest_Path_DAG {
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
            int weight=sc.nextInt();
            graph.addEdgeDirected(u,v,weight);
        }
        findShortestPathUsingtopologicalSort(graph,1);
    }
    static  void findShortestPathUsingtopologicalSort(Graph graph,int source) {
        Stack<Integer> stack=new Stack<>();
        boolean visited[]=new boolean[graph.size];
        for (int i=0;i<graph.size;i++)
        {
            if (!visited[i])
                DFSutil(graph,stack,i,visited);
        }
        int distance[]=new int[graph.size];
        for (int i=0;i<graph.size;i++)
            distance[i]=Integer.MAX_VALUE;
        distance[source]=0;
        while (!stack.isEmpty())
        {
            int top=stack.pop();
            if (distance[top]!=Integer.MAX_VALUE)
            {
                for (Edge e: graph.get(top))
                {
                    if (distance[e.v]>distance[top]+e.weight)
                        distance[e.v]=distance[top]+e.weight;
                }
            }
        }
        System.out.println(Arrays.toString(distance));
    }
    static  void DFSutil(Graph graph,Stack<Integer> stack,int current,boolean visited[])
    {
        visited[current]=true;
        for (Edge e : graph.get(current))
        {
            int v=e.v;
            if (!visited[v])
                DFSutil(graph,stack,v,visited);
        }
        stack.push(current);
    }

}


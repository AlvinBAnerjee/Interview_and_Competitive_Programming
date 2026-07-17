package _15_Graphs;
import java.util.*;

public class _12_kosarajus_algo {


    public static class Edge {
        int v;
        int weight;

        Edge(int weight, int v) {
            this.weight = weight;
            this.v = v;
        }
    }

    public static class Graph {
        int size;
        ArrayList<ArrayList<Edge>> list;

        Graph(int n) {
            this.size = n;
            list = new ArrayList<>();
            ;
            for (int i = 0; i < n; i++) {
                ArrayList<Edge> l = new ArrayList<>();
                list.add(l);
            }
        }

        void addEdge(int u, int v, int weight) {
            list.get(u).add(new Edge(weight, v));
            list.get(v).add(new Edge(weight, u));
        }

        void addEdgeDirected(int u, int v, int weight) {
            list.get(u).add(new Edge(weight, v));
        }

        void print() {
            for (int i = 0; i < list.size(); i++) {
                System.out.print(i + "->");
                for (Edge e : list.get(i)) {
                    System.out.print(e.v + "," + e.weight + " ->");
                }
                System.out.println();
            }
        }

        ArrayList<Edge> get(int u) {
            return list.get(u);
        }
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int e=sc.nextInt();
        Graph graph=new Graph(n);
        Graph reverse=new Graph(n);
        for (int i=0; i<e;i++)
        {
            int u=sc.nextInt();
            int v=sc.nextInt();
            graph.addEdgeDirected(u,v,1);
            reverse.addEdgeDirected(v,u,1);
        }
        findStronglyConnectedComponents(graph,reverse);

    }
     static  void findStronglyConnectedComponents(Graph graph,Graph reverse)
    {
        boolean visited[]=new boolean[graph.size];
        Stack<Integer> stack=new Stack<>();
        for (int i=0;i<graph.size;i++)
        {
            if (!visited[i])
                DFSutil(graph,i,visited,stack);
        }
        visited=new boolean[reverse.size];

        while (!stack.isEmpty())
        {
            int top=stack.pop();
            if (!visited[top])
            {
                Stack<Integer> temp=new Stack<>();
                DFSutil(reverse,top,visited,temp);
                System.out.println(temp);
            }
        }
    }
    static void DFSutil(Graph graph,int current,boolean visited[],Stack<Integer> stack)
    {
        //System.out.println(current);
        visited[current]=true;
        for (Edge e: graph.get(current))
        {
            if (!visited[e.v])
            DFSutil(graph,e.v,visited,stack);
        }
        stack.add(current);
    }
}
/*
Input 1
5 6
0 2
2 1
1 0
2 3
3 4
4 3
Input 2
6 7
0 1
1 2
2 1
1 3
3 4
4 5
5 4
 */

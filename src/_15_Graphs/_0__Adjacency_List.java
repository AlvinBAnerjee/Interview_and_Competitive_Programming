package _15_Graphs;

import java.util.ArrayList;

public class _0__Adjacency_List {

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
        Graph(int n)
        {
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
    }

    public static void main(String[] args) {
        Graph graph=new Graph(5);
        graph.addEdge(1,4,3);
        graph.addEdge(2,3,4);
        graph.addEdge(3,2,7);
        graph.print();


    }
}

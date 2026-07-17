package _15_Graphs;

public class _00_Implementing_Adjacency_Matrix {
    public static class Graph{
        int adjacency_Matrix[][];
        Graph(int n)
        {
            adjacency_Matrix=new int[n][n];
        }
        void addEdge(int u,int v,int weight)
        {
            adjacency_Matrix[u][v]=weight;

        }
        void addUndirectedEdge(int u,int v,int weight)
        {
            addEdge(u,v,weight);
            addEdge(v,u,weight);
        }
        void print()
        {
            for (int i=0;i<adjacency_Matrix.length;i++)
            {
                for (int j=0;j<adjacency_Matrix.length;j++)
                {
                    System.out.print(adjacency_Matrix[i][j]);
                }
                System.out.println();
            }
        }
    }
}

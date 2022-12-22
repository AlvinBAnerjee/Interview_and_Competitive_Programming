package X_CompanyQuestions.flipkart;

import java.util.Arrays;
import java.util.Scanner;

public class Footwear {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int k=sc.nextInt();
        int graph[][]=new int[n][n];
        for(int x[]: graph)
            Arrays.fill(x,Integer.MAX_VALUE);
        for(int i=0;i<n;i++)
        {
            graph[i][i]=0;
        }
        int w[]=new int[k];
        for(int i=0;i<k;i++)
        {
            w[i]=sc.nextInt();
        }
        int e=sc.nextInt();
        for(int i=0;i<e;i++)
        {
            int u=sc.nextInt();
            int v=sc.nextInt();
            graph[u][v]=sc.nextInt();
            graph[v][u]=graph[u][v];
        }

        for(int p=0;p<n;p++)
        {
            for(int i=0;i<n;i++)
            {
                for (int j=0;j<n;j++)
                {
                    if(graph[i][p]==Integer.MAX_VALUE || graph[p][j]==Integer.MAX_VALUE)
                    {
                        continue;
                    }
                    if(graph[i][p]+graph[p][j]<graph[i][j])
                    {
                        graph[i][j]=graph[i][p]+graph[p][j];
                    }
                }
            }
        }
        for(int x[]: graph)
            System.out.println(Arrays.toString(x));
        int min=Integer.MAX_VALUE;
        for(int i=0;i<w.length;i++)
        {
            for(int j=i+1;j<w.length;j++)
            {
                int u=w[i];
                int v=w[j];
                min=Math.min(min, graph[u][v]);
            }
        }
        System.out.println(min);
    }
}
/*
6 3
1 3 4
7
0 1 10
0 4 7
1 3 12
3 5 3
1 5 10
4 2 2
2 5 3
 */
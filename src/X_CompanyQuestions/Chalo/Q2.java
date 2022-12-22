package X_CompanyQuestions.Chalo;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
public class Q2 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int t=sc.nextInt();
        while(t-->0)
        {
            int n=sc.nextInt();
            int m=sc.nextInt();
            int graph[][]=new int[n][m];
            int q=sc.nextInt();
            HashSet<String> notAllowed=new HashSet<>();
            while(q-->0)
            {
                int a=sc.nextInt();
                int b=sc.nextInt();
                int c=sc.nextInt();
                int d=sc.nextInt();

                notAllowed.add(""+(a-1)+" "+(b-1)+" "+(c-1)+" "+(d-1));
                notAllowed.add(""+(c-1)+" "+(d-1)+" "+(a-1)+" "+(b-1));
            }

            int count=0;
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<m;j++)
                {
                    if(graph[i][j]==0)
                    {
                        rec(graph,i,j, notAllowed);
                        count++;
                    }
                }
            }
            System.out.print(count);
        }
    }
    public static void rec(int graph[][], int i, int j, HashSet<String> notAllowed)
    {
        int n=graph.length;
        int m=graph[0].length;
        graph[i][j]=1;
        int pos[][]={{-1,0},{1,0}, {0,-1},{0,1}};
        for(int k=0;k<4;k++)
        {
            int a=i;
            int b=j;
            int c=i+pos[k][0];
            int d=j+pos[k][1];
            if(c>=0 && c<=n-1 && d>=0 && d<=m-1 && graph[c][d]==0)
            {
                String key1=""+(a)+" "+(b)+" "+(c)+" "+(d);
                String key2=""+(c)+" "+(d)+" "+(a)+" "+(b);
                if(notAllowed.contains(key1) || notAllowed.contains(key2))
                    continue;
                rec(graph, c, d, notAllowed);
            }
        }
    }
}
/*
1
2
2
2
1 1 2 1
2 2 1 2
 */


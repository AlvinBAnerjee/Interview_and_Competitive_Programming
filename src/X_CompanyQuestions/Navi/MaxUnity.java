package X_CompanyQuestions.Navi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MaxUnity {
    static int parent[];
    static int size[];
    static int max=1;
    public static   int find(int x)
    {
        if(x==parent[x])
            return x;
        return parent[x]=find(parent[x]);
    }
    public static void union(int u, int v)
    {
        int x=find(u);
        int y=find(v);
        parent[y]=x;
        size[x]+=size[y];
        max=Math.max(max, size[x]);

    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        parent=new int[n];
        size=new int[n];
        for(int i=0;i<n;i++) {
            parent[i] = i;
            size[i]=1;
        }
        int m=n-1;
        int from[]=new int[m];
        int to[]=new int[m];
        boolean visited[]=new boolean[m];
        Arrays.fill(visited, true);


        for(int i=0;i<m;i++)
            from[i]=sc.nextInt()-1;
        for(int i=0;i<m;i++)
            to[i]=sc.nextInt()-1;
        int q=sc.nextInt();
        int query[]=new int[q];
        for(int i=0;i<q;i++)
        {
            query[i]=sc.nextInt()-1;
            visited[query[i]]=false;
        }
        for(int i=0;i<m;i++)
        {
            if(visited[i])
            {
                union(from[i],to[i]);
            }
        }
        for(int i=query.length-1;i>=0;i--)
        {
            System.out.println(max);
                int u=from[query[i]];
            int v=to[query[i]];
            union(u,v);
        }
    }
}

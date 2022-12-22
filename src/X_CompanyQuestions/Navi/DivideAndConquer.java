package X_CompanyQuestions.Navi;

import java.util.ArrayList;
import java.util.Scanner;

public class DivideAndConquer {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        ArrayList<ArrayList<Integer>> g=new ArrayList<>();
        for(int i=0;i<n;i++)
            g.add(new ArrayList<>());
        int m=sc.nextInt();
        for(int i=0;i<m;i++)
        {
            int u=sc.nextInt()-1;
            int v=sc.nextInt()-1;
            g.get(u).add(v);
            g.get(v).add(u);
        }
        int max=1;
        boolean visited[]=new boolean[n];
        for(int i=0;i<n;i++)
        {
            if(!visited[i])
            {
                int temp=rec(i,visited, g);
                max=Math.max(max, temp);
            }
        }
        System.out.println(max);
    }
    static  int rec(int u, boolean visited[], ArrayList<ArrayList<Integer>> g)
    {
        visited[u]=true;
        int count=1;
        for(int v: g.get(u))
        {
            if(!visited[v])
            {
                count+=rec(v, visited, g);
            }
        }
        return count;
    }
}
/*
5
3
2
4
1
3
1
3
5

5
3
2 1 4 3 1 5
 */
package X_CompanyQuestions.Amazon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class TripletQuery {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        sc.nextLine();
        ArrayList<ArrayList<Integer>> g=new ArrayList<>();
        for(int i=0;i<n;i++)
        {
            g.add(new ArrayList<>());
        }
        int cols[]=new int[n];
        String str=sc.nextLine();
        for(int i=0;i<n;i++)
        {
            cols[i]=str.charAt(i);
        }
        for(int i=0;i<m;i++)
        {
            int a=sc.nextInt();
            int b=sc.nextInt();
            g.get(a).add(b);
            g.get(b).add(a);
        }
        int q=sc.nextInt();
        sc.nextLine();
        while(q-->0)
        {
            String s=sc.nextLine();
            int a=(int)s.charAt(0);
            int b=(int)s.charAt(1);
            int c=(int)s.charAt(2);
            boolean visited[]=new boolean[n];
            boolean check=false;
            for(int i=0;i<n;i++)
            {
                HashSet<Integer> t=new HashSet<>();
                if(!visited[i])
                {
                    if((cols[i]==a || cols[i]==b|| cols[i]==c) &&dfs(g, a,b, c, i,cols, visited,t))
                        check=true;
                }
            }
            System.out.println(check);
        }
    }
    public static   boolean dfs(ArrayList<ArrayList<Integer>> g, int a, int b, int c, int u, int cols[], boolean visited[], HashSet<Integer> t)
    {
        visited[u]=true;
        t.add(cols[u]);
        if(t.size()==3)return true;
        for(int i: g.get(u))
        {
            if(!visited[i])
            {
                if((cols[i]==a || cols[i]==b|| cols[i]==c) &&dfs(g, a,b, c, i,cols, visited,t))
                    return true;
            }
        }
        return false;
    }
}

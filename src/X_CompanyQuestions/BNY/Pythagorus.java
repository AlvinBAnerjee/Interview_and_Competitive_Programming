package X_CompanyQuestions.BNY;

import java.util.*;

public class Pythagorus {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        ArrayList<ArrayList<Integer>> g=new ArrayList<>();
        for(int i=0;i<n;i++)
        {
            g.add(new ArrayList<>());
        }
        for(int i=0;i<n-1;i++)
        {
            int u=sc.nextInt();
            int v=sc.nextInt();
            g.get(u).add(v);
            g.get(v).add(u);
        }
        int x=sc.nextInt();
        int y=sc.nextInt();
        int z=sc.nextInt();
        int dist_x[]=BFS(x,g);
        int dist_y[]=BFS(y,g);
        int dist_z[]=BFS(z,g);
        System.out.println(Arrays.toString(dist_x));
        System.out.println(Arrays.toString(dist_y));
        System.out.println(Arrays.toString(dist_z));
        int count=0;
        for(int i=0;i<n;i++)
        {
            if(i==x || i==y || i==z)continue;
            int a=dist_x[i]*dist_x[i];
            int b=dist_y[i]*dist_y[i];
            int c=dist_z[i]*dist_z[i];
            if(a + b ==c || b+c == a || a+c==b)
            {
                count++;
                System.out.println(i);
            }
        }
        System.out.println(count);
    }
    public static int[] BFS(int u, ArrayList<ArrayList<Integer>> g)
    {
        int n=g.size();
        int dist[]=new int[n];
        int level=0;
        Queue<Integer> q=new LinkedList<>();
        boolean visited[]=new boolean[n];
        q.add(u);
        while (!q.isEmpty())
        {
            int size=q.size();
            while(size-->0)
            {
                int top=q.remove();
                if(visited[top])continue;
                visited[top]=true;
                dist[top]=level;
                for(int v: g.get(top))
                {
                    q.add(v);
                }
            }
            level++;
        }
        return dist;
    }
}
/*
10
0 4
0 1
1 2
1 3
3 5
3 7
5 6
7 8
8 9
4 6 9
op:1
 */
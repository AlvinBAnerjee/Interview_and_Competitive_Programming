package X_CompanyQuestions.Navi;

import java.util.*;

public class JackSparrow {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int k=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        ArrayList<ArrayList<Integer>> g=new ArrayList<>();
        for(int i=0;i<n;i++)
        {
            g.add(new ArrayList<>());
        }

        for(int i=0;i<n-1;i++)
        {
            int u=sc.nextInt()-1;
            int v=sc.nextInt()-1;
            if(arr[u]==arr[v])
            {
                g.get(u).add(v);
                g.get(v).add(u);
            }

        }
        //for(int i=0;i<n;i++)
            //System.out.println(g.get(i).toString());
        System.out.println(solve(g,n));

    }
    public static   int solve(ArrayList<ArrayList<Integer>> g, int n)
    {
        boolean visited[]=new boolean[n];
        int res=0;
        for(int i=0;i<n;i++)
        {
            if(!visited[i])
            {
                dfs(g, i, visited);
                int temp=findMax(g, n,i);
                res=Math.max(res, temp);
                //System.out.println(temp);
            }
        }
        return res;
    }
    public static   void  dfs(ArrayList<ArrayList<Integer>> g, int u, boolean visited[])
    {
        visited[u]=true;
        for(int v: g.get(u))
        {
            if(!visited[v])
            {
                dfs(g,v,visited);
            }
        }
    }
    public static int bfs(ArrayList<ArrayList<Integer>> g, int n, int u)
    {
        int dist[]=new int[n];
        Arrays.fill(dist,-1);
        Queue<Integer> q=new LinkedList<>();
        q.add(u);
        dist[u]=0;
        boolean visited[]=new boolean[n];
        visited[u]=true;
        int level=0;
        while(!q.isEmpty())
        {
            int size=q.size();
            level++;
            while(size-->0)
            {
                int top=q.remove();
                //System.out.println("top: "+top);
                visited[top]=true;
                dist[top]=level;
                for(int v: g.get(top))
                {
                    if(!visited[v])
                    {
                        q.add(v);
                    }
                }
            }
        }
        //System.out.println(Arrays.toString(dist));
        int max=0;
        for(int i=0;i<n;i++)
        {
            if(dist[max]< dist[i])
                max=i;
        }
        return max;

    }
    public static int findMax(ArrayList<ArrayList<Integer>> g,int n, int u)
    {
        int x=bfs(g, n,u);
        //System.out.println(u +"  : " + x );
        int dist[]=new int[n];
        Arrays.fill(dist,-1);
        Queue<Integer> q=new LinkedList<>();
        q.add(x);
        dist[x]=0;
        boolean visited[]=new boolean[n];
        visited[x]=true;
        int level=0;
        while(!q.isEmpty())
        {
            int size= q.size();
            level++;
            while(size-->0)
            {
                int top=q.remove();
                visited[top]=true;
                dist[top]=level;
                for(int v: g.get(top))
                {
                    if(!visited[v])
                    {
                        q.add(v);
                    }
                }
            }
        }
        int max=0;
        for(int i=0;i<n;i++)
        {
            if(dist[max]< dist[i])
                max=i;
        }
        //System.out.println(Arrays.toString(dist));
        //System.out.println(max+"hmmmmm");
        return dist[max];
    }
}
/*
9 3
1 1 1 3 3 2 2 3 1
1 2
1 3
2 4
5 2
3 6
3 7
5 8
5 9




5 1
1 1 1 1 1
1 2
2 3
3 5
3 4

5 1
1 1 1 1 1
1 2
1 3
1 4
1 5
output - 3

9 2
2 2 2 2 2 1 1 1 1
1 2
2 3
3 5
4 3
4 6
6 7
9 6
6 8
output - 4

9 3
1 1 1 3 3 2 2 3 1
1 2
1 3
2 4
5 2
3 6
3 7
5 8
5 9
 */
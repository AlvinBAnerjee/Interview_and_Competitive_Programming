package X_CompanyQuestions.BNY;

import java.util.*;

public class Predessor {
    static class Edge
    {
        int u;
        int v;
        int w;
        Edge(int u, int v, int w)
        {
            this.u=u;
            this.v=v;
            this.w=w;
        }
    }
    static class Pair{
        int i;
        int dist;
        HashSet<Integer> set;
        Pair(int i, int dist, HashSet<Integer> x)
        {
            this.i=i;
            this.dist=dist;
            set=new HashSet<>();
            for(int y: x)
            {
                set.add(y);
            }
        }
    }
    static class myComp implements Comparator<Pair>
    {
        @Override
        public  int compare(Pair o1, Pair o2)
        {
            return  o1.dist-o2.dist;
        }
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        ArrayList<ArrayList<Edge>> g=new ArrayList<>();
        for(int i=0;i<n;i++)
        {
            g.add(new ArrayList<>());
        }
        for(int i=0;i<m;i++)
        {
            int u=sc.nextInt()-1;
            int v=sc.nextInt()-1;
            int w=sc.nextInt();
            g.get(u).add(new Edge(u,v,w));
            g.get(v).add(new Edge(v,u,w));
        }
        int ans[]=djk(g);
    }
    public static int[] djk(ArrayList<ArrayList<Edge>> g)
    {
        int n=g.size();
        int dist[]=new int[n];
        ArrayList<HashSet<Integer>> p=new ArrayList<>();
        boolean visited[]=new boolean[n];
        for(int i=0;i<n;i++)
        {
            p.add(new HashSet<>());
        }
        Arrays.fill(dist,Integer.MAX_VALUE);
        PriorityQueue<Pair> pq=new PriorityQueue<>(new myComp());
        HashSet<Integer> temp=new HashSet<>();
        temp.add(0);
        p.get(0).add(0);
        pq.add(new Pair(0,0, temp));
        dist[0]=0;
        while(!pq.isEmpty())
        {
            Pair top=pq.remove();
            int u=top.i;
            int d=top.dist;
            if(visited[u])
            {
                if(dist[u]==d)
                {
                    p.get(u).addAll(top.set);
                    for(Edge e: g.get(u))
                    {
                        if(!visited[e.v])
                        {
                            temp=new HashSet<>();
                            temp.add(e.v);;
                            temp.addAll(top.set);

                            pq.add(new Pair(e.v, d+e.w, temp));
                        }
                    }
                }
                continue;
            }
            p.get(u).addAll(top.set);

            visited[u]=true;
            dist[u]=d;
            for(Edge e: g.get(u))
            {
                if(!visited[e.v])
                {
                    temp=new HashSet<>();
                    temp.add(e.v);;
                    temp.addAll(top.set);

                    pq.add(new Pair(e.v, d+e.w, temp));
                }
            }
        }
        for(int i=0;i<n;i++)
        {
            System.out.println(i+" : "+p.get(i).toString());
        }
        System.out.println(Arrays.toString(dist));
        return dist;
/*
6 8
3 6 2
1 3 2
4 6 1
2 4 1
5 3 1
2 3 2
2 1 2
5 4 1



 */
    }
}

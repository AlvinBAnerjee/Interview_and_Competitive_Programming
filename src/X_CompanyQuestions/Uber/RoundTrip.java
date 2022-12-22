package X_CompanyQuestions.Uber;

import java.util.*;

public class RoundTrip {
    static class Edge{
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
        int w;
        Pair(int i, int w)
        {
            this.i=i;
            this.w=w;
        }
    }
    static class  myComp implements Comparator<Pair>
    {
        @Override
        public  int compare(Pair o1, Pair o2)
        {
            return o1.w-o2.w;
        }
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int roll[]=new int[n];
        int uid[]=new int[n];
        for(int i=0;i<n;i++)
            roll[i]=sc.nextInt();
        for(int i=0;i<n;i++)
            uid[i]=sc.nextInt();
        ArrayList<ArrayList<Edge>> g=new ArrayList<>();
        for(int i=0;i<n;i++)
            g.add(new ArrayList<>());
        for(int i=0;i<n;i++)
        {
            for(int j=i+1;j<n;j++)
            {
                int u=i;
                int v=j;
                int w=Math.min(roll[u]*uid[v] , uid[u]*roll[v]);
                Edge e1=new Edge(u,v,w);
                Edge e2=new Edge(v,u,w);
                g.get(u).add(e1);
                g.get(v).add(e2);
            }
        }
        System.out.println(mst(g,n));

    }
    public  static  int  mst(ArrayList<ArrayList<Edge>> g, int n)
    {
        boolean visited[]=new boolean[n];
        PriorityQueue<Pair> pq=new PriorityQueue<>(new myComp());
        pq.add(new Pair(0,0));
        int mst=0;
        while(!pq.isEmpty())
        {
            Pair top=pq.remove();
            int i=top.i;
            if(visited[i])continue;
            visited[i]=true;
            mst=mst+top.w;
            for(Edge e: g.get(i))
            {
                int v=e.v;
                int w=e.w;
                pq.add(new Pair(v,w));
            }
        }
        return mst;
    }
}

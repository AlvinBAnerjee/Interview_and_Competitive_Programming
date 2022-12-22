package X_CompanyQuestions.UI;

import _15_Graphs._10_Prims;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class _11_maximum_xor {
    /*
    1. pair of all the xor
    mst;
     */
    static class Pair {
        int key;
        int value;

        Pair(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    public static class mycomparator implements Comparator<Pair>
    {
        @Override
        public int compare(Pair p1, Pair p2)
        {
            return p2.value-p1.value;
        }
    }
    static int spanningTree(int V, ArrayList<ArrayList<ArrayList<Integer>>> adj)
    {
        int mst=0;
        boolean visited[]=new boolean[V];
        PriorityQueue<Pair> pq=new PriorityQueue<>(new mycomparator());
        pq.add(new Pair(0,0));
        while(!pq.isEmpty())
        {
            Pair top=pq.remove();
            if(visited[top.key])
                continue;
            visited[top.key]=true;
            int u=top.key;
            mst=mst+top.value;
            for(int i=0;i<adj.get(u).size();i++)
            {
                int v=adj.get(u).get(i).get(0);
                int weight=adj.get(u).get(i).get(1);
                pq.add(new Pair(v,weight));
            }
        }
        return mst;

    }
    public static void main(String[] args) {
        int arr[]={1,2,3};
        int n=3;
        ArrayList<ArrayList<ArrayList<Integer>>> g=new ArrayList<>();
        for(int i=0;i<n;i++)
        {
            g.add(new ArrayList<>());
        }
        for(int i=0;i<n;i++)
        {
            for(int j=i+1;j<n;j++)
            {
                int u=i;
                int v=j;
                int w=arr[i]^arr[j];
                ArrayList<Integer> e=new ArrayList<>();
                e.add(v);
                e.add(w);
                g.get(u).add(e);
                e=new ArrayList<>();
                e.add(u);
                e.add(w);
                g.get(v).add(e);


            }
        }
        System.out.println(spanningTree(n, g));

    }
}

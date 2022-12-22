package X_CompanyQuestions.UI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

public class _1_Maximum_common_companies {
    static class  Edge
    {
        int v;
        int weight;
        Edge(int v,int weight)
        {
            this.v=v;
            this.weight=weight;
        }
    }
    static PriorityQueue<Integer> pq;
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int e=sc.nextInt();
        ArrayList<ArrayList<Edge >> g=new ArrayList<>();
        for(int i=0;i<n;i++)
        {
            g.add(new ArrayList<>());
        }
        for(int i=0;i<e;i++)
        {
            int u=sc.nextInt();
            int v=sc.nextInt();
            int w=sc.nextInt();
            g.get(u-1).add(new Edge(v-1, w));
            g.get(v-1).add(new Edge(u-1, w));
        }
        for(int i=1;i<=100;i++)
        {
            pq=new PriorityQueue<>(Collections.reverseOrder());
            boolean visited[]=new boolean[n];


        }
    }
}

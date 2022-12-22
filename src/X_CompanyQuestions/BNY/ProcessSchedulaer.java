package X_CompanyQuestions.BNY;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ProcessSchedulaer {
    static class Pair{
        int start;
        int end;
        Pair(int start, int end)
        {this.start=start;
        this.end=end;}
    }
    static class myComp implements Comparator<Pair> {
        @Override
        public int compare(Pair p1, Pair p2)
        {
            return p1.start-p2.start;
        }
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        PriorityQueue<Pair> pq1=new PriorityQueue<>(new myComp());
        int n=sc.nextInt();
        for(int i=0;i<n;i++)
        {
            pq1.add(new Pair(sc.nextInt(), sc.nextInt()));
        }
        PriorityQueue<Integer> pq2=new PriorityQueue<>();
        while(!pq1.isEmpty())
        {
            Pair top=pq1.remove();
            if(pq2.isEmpty() )
            {
                pq2.add(top.end);
            }
            else
            {
                if(pq2.peek()<top.start)
                {
                    pq2.remove();
                }
                pq2.add(top.end);
            }
        }
        System.out.println(pq2.size());
    }
}

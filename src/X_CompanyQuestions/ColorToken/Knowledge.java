package X_CompanyQuestions.ColorToken;
import  java.util.*;
class Knowledge {
    static class Triplet{
        int start; int end; int weight;
        Triplet(int start, int end, int weight)
        {
            this.start=start;
            this.end=end;
            this.weight=weight;
        }
    }
    static class Pair{
        int end; int weight;
        Pair(int end, int weight)
        {
            this.end=end;
            this.weight=weight;
        }
    }
    static class comp1 implements Comparator<Triplet>
    {
        @Override
        public int compare(Triplet o1,Triplet o2)
        {
            return o1.start-o2.start;
        }
    }
    static class comp2 implements Comparator<Pair>
    {
        @Override
        public int compare(Pair o1, Pair o2)
        {
            return o1.end-o2.end;
        }
    }
    public int maxTwoEvents(int[][] events) {
        PriorityQueue<Triplet> pq1=new PriorityQueue<>(new comp1());
        PriorityQueue<Pair> pq2=new PriorityQueue<>(new comp2());

        for(int i=0;i<events.length;i++)
        {
            pq1.add(new Triplet(events[i][0], events[i][1], events[i][2]));
        }
        int max=0;
        int ans=0;
        while(!pq1.isEmpty())
        {
            Triplet top=pq1.remove();
            while(!pq2.isEmpty())
            {
                if(pq2.peek().end >= top.start)
                    break;

                Pair temp=pq2.remove();
                max=Math.max(max, temp.weight);

            }
            ans=Math.max(ans, max+top.weight);
            pq2.add(new Pair(top.end, top.weight));
        }
        return ans;

    }
}
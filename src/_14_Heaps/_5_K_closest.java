package _14_Heaps;

import java.util.Comparator;
import java.util.PriorityQueue;

public class _5_K_closest {
    //similar concept to k smallest. this time we create a hashmap
    static class Pair {
        int value;
        int index;
        Pair(int value,int index)
        {
            this.value=value;
            this.index=index;
        }

    }
    static class SortOrder implements Comparator<Pair>
    {
        @Override
        public int compare(Pair o1, Pair o2) {
            if (o1.value!=o2.value)
            return -(o1.value-o2.value);
            return -(o1.index- o2.index);
        }
    }
    public static void main(String[] args) {
        int arr[]={30,40,32,33,36,37};
        int x=38;
        int k=2;
        findClosest(arr,x,k);

    }
    public static void findClosest(int arr[],int x,int k)
    {
        PriorityQueue<Pair> pq=new PriorityQueue<>(k,new SortOrder());
        if (arr.length<k)
        {
            System.out.println("Not possible");
            return;
        }
        for (int i=0;i<k;i++)
        {
            pq.add(new Pair(Math.abs(arr[i]-x),i));
        }
        for (int i=k;i<arr.length;i++)
        {
            int diff=Math.abs(arr[i]-x);
            if (diff<(pq.peek().value))
            {
                pq.poll();
                pq.add(new Pair(Math.abs(arr[i]-x),i));
            }

        }
        while (!pq.isEmpty())
        {
            System.out.print(arr[pq.poll().index]+" , ");
        }
        System.out.println();
    }
}

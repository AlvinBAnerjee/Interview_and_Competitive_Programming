package _14_Heaps;

import java.util.*;

/*
Use both min heap and max heap
O(nlogn)
 */
public class _7_Median_of_a_stream {
    public static void main(String[] args) {
        int arr[]={25,7,10,15,20};
        System.out.println(Arrays.toString(getMedianStream(arr)));
    }
    public static Object[] getMedianStream(int arr[])
    {
        List<Double> list=new ArrayList<>();
        PriorityQueue<Integer> max_heap=new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> min_heap=new PriorityQueue<>();
        max_heap.add(arr[0]);
        list.add((double)arr[0]);
        for (int i=1;i<arr.length;i++)
        {
            int x=arr[i];
            if (max_heap.size()>min_heap.size())
            {
                if (x>max_heap.peek())
                    min_heap.add(x);
                else
                {
                    min_heap.add(max_heap.remove());
                    max_heap.add(x);
                }
            }
            else
            {
                if (x<=min_heap.peek())
                    max_heap.add(x);
                else
                {
                    max_heap.add(min_heap.remove());
                    min_heap.add(x);
                }

            }
            int size1=max_heap.size();
            int size2=min_heap.size();
            if (size1>size2)
                list.add((double)max_heap.peek());
            else
                list.add(((double)max_heap.peek()+min_heap.peek())/2);

        }
        return list.toArray();
    }
}

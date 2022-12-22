package _14_Heaps;

import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
/*
Given an array of n elements, where each element is at most k away from its target position,
devise an algorithm that sorts in O(n log k) time. For example, let us consider k is 2,
an element at index 7 in the sorted array, can be at indexes 5, 6, 7, 8, 9 in the given array.
Better than naive sorting as this is 0(nlogk) is k is small almost O(n)
 */


public class _2_sort_k_sorted_Array {
    public static void main(String[] args) {
        int arr[]={6, 5, 3, 2, 8, 10, 9};
        int k=3;
        sort(arr,k);
        System.out.println(Arrays.toString(arr));

    }
    static void sort(int arr[],int k)
    {
        int n=arr.length;
        PriorityQueue<Integer> pq=new PriorityQueue<>();
        for (int i=0;i<=k;i++)//adding first k+1 elements to pq
            pq.add(arr[i]);
        int index=0;
        for (int i=k+1;i<n;i++)
        {
            arr[index++]=pq.remove();
            pq.add(arr[i]);
        }
        Iterator<Integer> iter= pq.iterator();
        while (iter.hasNext())
        {
            arr[index++]=iter.next();
        }

    }
}

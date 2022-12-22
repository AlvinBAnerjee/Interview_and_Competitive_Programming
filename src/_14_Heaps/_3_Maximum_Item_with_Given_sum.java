package _14_Heaps;

import java.util.Arrays;
import java.util.PriorityQueue;

/*
Input: arr[] = {1,12,5,111,200}
sum=10
Output: 3
TC=o(n)+o(output*logn)
 */
public class _3_Maximum_Item_with_Given_sum {
    static class MinHeap {
        int arr[];
        int size;
        int capacity;
        MinHeap(int capacity)
        {
            this.arr=new int[capacity];
            this.size=0;
            this.capacity=capacity;
        }
        MinHeap(int input[])
        {
            this.arr= Arrays.copyOf(input,input.length);
            this.size=arr.length;
            this.capacity=arr.length;
        }
        void build()
        {
            for(int i=size/2;i>=0;i--)
                heapify(i);
        }
        void heapify(int i)
        {
            int l=2*i+1;
            int r=2*i+2;
            int next=i;
            if (l<size&&arr[next]>arr[l])
            {
                next=l;
            }
            if (r<size&&arr[next]>arr[r])
            {
                next=r;
            }
            if(next!=i)
            {
                swap(i,next);
                heapify(next);
            }

        }
        void swap(int a,int b)
        {
            int temp=arr[a];
            arr[a]=arr[b];
            arr[b]=temp;
        }
        int remove_element()
        {
            if (size==0)
                return -1;
            int top=arr[0];
            swap(0,size-1);
            size--;
            heapify(0);
            return top;
        }
        void add_element(int element)
        {
            if(size==capacity)
            {
                System.out.println("Overflow");
                return;
            }
            arr[size]=element;
            size++;
            int i=size-1;
            while (arr[i]<arr[(i-1)/2])
            {
                swap(i,(i-1)/2);
                i=(i-1)/2;
            }

        }
        void decrease_index(int index,int value)//instead of just index we could simply use hashmap values
        {
            arr[index]=value;
            int i=index;
            while (arr[i]<arr[(i-1)/2])
            {
                swap(i,(i-1)/2);
                i=(i-1)/2;
            }
        }
    }
    public static void main(String[] args) {
        int arr[] = {20,10,5,30,100};
        int sum=35;
        MinHeap mh=new MinHeap(arr);
        mh.build();
        int count=0;
        for (int i=0;i<=arr.length;i++)
        {
            int top=mh.remove_element();
            if (top<=sum)
            {
                count++;
                sum=sum-top;
            }
            else
            {
                break;
            }
        }
        System.out.println(count);

    }
}

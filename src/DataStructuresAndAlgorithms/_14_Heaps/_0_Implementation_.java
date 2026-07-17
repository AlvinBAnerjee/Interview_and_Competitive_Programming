package _14_Heaps;

import java.util.Arrays;

public class _0_Implementation_ {
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
            this.arr=Arrays.copyOf(input,input.length);
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
        int peek()
        {
            return arr[0];
        }
    }

    public static void main(String[] args) {
        int arr[]={0,9,8,7,6,5,4,3,2,1,0};
        MinHeap minHeap=new MinHeap(arr);
        System.out.println(Arrays.toString(minHeap.arr));
        minHeap.heapify(3);
        System.out.println(Arrays.toString(minHeap.arr));
        minHeap.build();
        System.out.println(Arrays.toString(minHeap.arr));

        System.out.println(minHeap.remove_element());
        System.out.println(Arrays.toString(minHeap.arr));

        System.out.println(minHeap.remove_element());
        System.out.println(Arrays.toString(minHeap.arr));

        System.out.println(minHeap.remove_element());
        System.out.println(Arrays.toString(minHeap.arr));

        minHeap.add_element(-1);
        System.out.println(Arrays.toString(minHeap.arr));



    }
}

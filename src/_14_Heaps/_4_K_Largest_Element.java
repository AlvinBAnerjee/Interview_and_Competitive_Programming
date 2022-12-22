package _14_Heaps;
/*
Dude we can use Max_Heap that will give us o(n)+o(klogn) but dang we can do better as when k is large it becomes 0(n+nlogn)

How?
Use min heap
O(k+(n-k)*logn) when k is large we get O(n+logn)

 */


import java.util.Arrays;

public class _4_K_Largest_Element {
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
        int peek()
        {
            return arr[0];
        }
    }


    public static void main(String[] args) {
        int arr[] = { 3,2,1,5,6,4};
        int k=2;
        int ans[]=find_k_largest(arr,k);
        System.out.println(Arrays.toString(ans));

    }
    static int[] find_k_largest(int arr[],int k)
    {
        MinHeap mh=new MinHeap(k);
        mh.build();
        for (int i=0;i<k;i++)
        {
            mh.add_element(arr[i]);
        }
        for (int i=k;i<arr.length;i++)
        {

            if (mh.peek()<arr[i])
            {
                mh.remove_element();
                mh.add_element(arr[i]);
            }

        }
        int temp[]=new int[k];
        for(int i=0;i<k;i++)
        {
         temp[i]=mh.remove_element();
        }
        return temp;
    }
}

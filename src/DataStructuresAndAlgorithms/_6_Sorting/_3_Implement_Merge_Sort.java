package _6_Sorting;

import java.util.Arrays;
import java.util.Scanner;

public class _3_Implement_Merge_Sort {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
         arr[i]=sc.nextInt();
        }
        System.out.println("Before sorting: "+ Arrays.toString(arr));
        mergesort(arr,0,arr.length-1);
        System.out.println("After sorting: "+Arrays.toString(arr));
        //merge(arr,0,2,n-1);
        //System.out.println(Arrays.toString(arr));

    }
    static  void mergesort(int arr[],int start,int end)
    {
        if (start>=end)
            return;
        int mid=(start+end)/2;
        mergesort(arr,start,mid);
        mergesort(arr,mid+1,end);
        merge(arr,start,mid,end);
    }
    static void merge(int arr[],int start,int mid,int end)
    {
        int len=(end-start+1);
        int temp[]=new int[len];
        int counter=0;
        int i=start;
        int j=mid+1;
        while (i<=mid && j<=end)
        {
            if (arr[i]<arr[j])
                temp[counter++]=arr[i++];
            else
                temp[counter++]=arr[j++];
        }
        while (i<=mid)
        {
            temp[counter++]=arr[i++];
        }
        while (j<=end)
        {
            temp[counter++]=arr[j++];
        }
        counter=0;
        for (int x=start;x<=end;x++)
            arr[x]=temp[counter++];

    }

}

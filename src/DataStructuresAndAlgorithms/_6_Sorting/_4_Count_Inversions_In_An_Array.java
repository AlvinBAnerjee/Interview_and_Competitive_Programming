/*
A pair is said to be an inversion in an array if arr[i]>arr[j] and i<j


CODE:
import java.util.*;
import java.lang.*;
import java.io.*;

class Solution
{
    public static void main (String[] args)
    {
        int arr[] = new int[]{2,4,1,3,5};

        int n = arr.length;
	    System.out.print(countInv(arr,0,n-1));

    }

    static int countInv(int arr[], int l, int r)
    {
        int res = 0;
        if (l<r) {

            int m = (r + l) / 2;

            res += countInv(arr, l, m);
            res += countInv(arr, m + 1, r);
            res += countAndMerge(arr, l, m , r);
        }
        return res;
    }

    static int countAndMerge(int arr[], int l, int m, int r)
    {
        int n1=m-l+1, n2=r-m;
        int[] left=new int[n1];int[] right=new int[n2];
        for(int i=0;i<n1;i++)
            left[i]=arr[i+l];
        for(int j=0;j<n2;j++)
            right[j]=arr[m+1+j];
        int res=0,i=0,j=0,k=l;
        while(i<n1 && j<n2){
            if(left[i]<=right[j])
                {arr[k++]=left[i++];}
            else{
                arr[k++]=right[j++];
                res=res+(n1-i);
            }
        }
        while(i<n1)
            arr[k++]=left[i++];
        while(j<n2)
            arr[k++]=right[j++];
        return res;
    }
}

 */



package _6_Sorting;

import java.util.Arrays;
import java.util.Scanner;

public class _4_Count_Inversions_In_An_Array {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for( int i =0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        System.out.println("The no of inversions are: "+count(arr,0,n-1));
        System.out.println(Arrays.toString(arr));
    }
    static int count(int arr[],int l,int r)
    {
        if (l>r|| l==r)
            return 0;
        int res=0;
        int mid=(l+r)/2;
        res=res+count(arr,l,mid);
        res=res+count(arr,mid+1,r);
        res=res+count_merge(arr,l,r,mid);
        return  res;
    }
    static int count_merge(int arr[],int l,int r,int mid)
    {
        int res=0;
        int temp[]=new int[r-l+1];
        int k=0;
        int i=l;
        int j=mid+1;
        while (i<=mid&&j<=r)
        {

            if (arr[i]<=arr[j]) {
                temp[k++] = arr[i++];
            }
            else
            {
                temp[k++]=arr[j++];
                res=res+(mid  - i + 1);
            }
        }
        while (i<=mid)
        {
            temp[k++] = arr[i];
            i++;

        }
        while (j<=r)
        {
            temp[k++]=arr[j];
            j++;

        }
        i=l;
        for (k=0;k<temp.length;k++,i++)
        {
            arr[i]=temp[k];
        }
        return res;
    }
}

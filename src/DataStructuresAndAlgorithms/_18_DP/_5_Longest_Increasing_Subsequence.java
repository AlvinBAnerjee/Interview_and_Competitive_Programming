package _18_DP;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

/*
Now, let us discuss the Longest Increasing Subsequence (LIS) problem as an example problem that
can be solved using Dynamic Programming.

The Longest Increasing Subsequence (LIS) problem is to find the length of the longest
subsequence of a given sequence such that all elements of the subsequence are sorted in increasing order.
For example, the length of LIS for {10, 22, 9, 33, 21, 50, 41, 60, 80} is 6 and LIS is {10, 22, 33, 50, 60, 80}.

Let arr[0..n-1] be the input array and L(i) be the length of the LIS ending at index i such that arr[i] is the last element of the LIS.

Then, L(i) can be recursively written as:

L(i) = 1 + max( L(j) ) where 0 < j < i and arr[j] < arr[i]; or
L(i) = 1, if no such j exists.
 */
public class _5_Longest_Increasing_Subsequence {
    public static void main(String[] args) {

        ;
        }




    static int findLIS(int arr[])//This is not the most optimal, the most optimal solution uses binary search as this is O(n*n) but Binary search is O(nlogn)
    {
        int l[]=new int[arr.length];
        for (int i=0;i<l.length;i++)
        {
            l[i]=1;
            for (int j=0;j<i;j++)
            {
                if (arr[j]<arr[i])
                {
                    l[i]=Math.max(l[j]+1,l[i]);
                }
            }
        }
        int max=Integer.MIN_VALUE;
        for (int k=0;k<arr.length;k++)
        {
            if (l[k]>max)
                max=l[k];
        }
        return max;
    }
    static int ceilIdx(int tail[], int l, int r, int key)
    {
        while (r > l) {
            int m = l + (r - l) / 2;
            if (tail[m] >= key)
                r = m;
            else
                l = m+1;
        }

        return r;
    }

    static int findLISBinarySearch(int arr[])
    {

        int n=arr.length;
        int[] tail = new int[n];
        int len =1;

        tail[0] = arr[0];

        for (int i = 1; i < n; i++) {

            if(arr[i] > tail[len - 1])
            {
                tail[len] = arr[i];
                len++;
            }
            else{
                int c = ceilIdx(tail, 0, len - 1, arr[i]);
                System.out.println(Arrays.toString(tail)+"  "+len);
                System.out.println(arr[i]+" , "+c);
                tail[c] = arr[i];
            }
        }

        return len;
    }
}

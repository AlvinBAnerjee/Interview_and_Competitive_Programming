package _3_Arrays;

import java.util.Scanner;

/*
Kadane’s Algorithm:

Initialize:
    max_so_far = INT_MIN
    max_ending_here = 0

Loop for each element of the array
  (a) max_ending_here = max_ending_here + a[i]
  (b) if(max_so_far < max_ending_here)
            max_so_far = max_ending_here
  (c) if(max_ending_here < 0)
            max_ending_here = 0
return max_so_far
 */
public class _8_MaximumSubarray {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for (int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        findSubarray(arr);
    }
    static void findSubarray(int arr[])
    {
        int max_so_far=Integer.MIN_VALUE;
        int maxEnding=0;
        for (int i=0;i<arr.length;i++)
        {
            maxEnding=maxEnding+arr[i];
            if (maxEnding>max_so_far)
                max_so_far=maxEnding;
            if (maxEnding<0)
            {
                maxEnding=0;
            }
        }
        System.out.println(max_so_far);
    }
}

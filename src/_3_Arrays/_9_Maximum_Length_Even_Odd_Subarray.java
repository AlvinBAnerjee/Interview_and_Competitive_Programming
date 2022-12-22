package _3_Arrays;

import java.util.Scanner;
/*
Given an array a[] of N integers, the task is to find the length of the longest Alternating Even Odd subarray present in the array.
Examples:


Input: a[] = {1, 2, 3, 4, 5, 7, 9}
Output: 5
Explanation:
The subarray {1, 2, 3, 4, 5} has alternating even and odd elements.
 */
public class _9_Maximum_Length_Even_Odd_Subarray {
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
    static void findSubarray(int arr[]) {
        int max_so_far = Integer.MIN_VALUE;
        int last_ending = -1;
        int curr_max=0;
        for (int i=0;i<arr.length;i++)
        {
            int new_ending=arr[i]%2;
            if (last_ending!=new_ending)
            {
                curr_max++;
            }
            else {
                curr_max=1;
            }
            last_ending=new_ending;
            max_so_far=Math.max(max_so_far,curr_max);
        }
        System.out.println(curr_max);
    }
}

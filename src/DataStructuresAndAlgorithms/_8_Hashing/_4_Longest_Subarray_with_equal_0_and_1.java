package _8_Hashing;

import java.util.HashMap;
import java.util.Scanner;
/*
Given an array containing only 0s and 1s, find the largest subarray which contains equal no of 0s and 1s. The expected time complexity is O(n).

Examples:

Input: arr[] = {1, 0, 1, 1, 1, 0, 0}
Output: 1 to 6
(Starting and Ending indexes of output subarray)

Input: arr[] = {1, 1, 1, 1}
Output: No such subarray

Input: arr[] = {0, 0, 1, 1, 0}
Output: 0 to 3 Or 1 to 4


Efficient apprach is to replace every 0 with -1 so the problem translates to longest subarray with sum=0
damn Bitch!!!!!
 */

public class _4_Longest_Subarray_with_equal_0_and_1 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        System.out.println(find_length(arr,n));
    }
    static int find_length(int arr[],int n)
    {
        int sum=0;
        int pre=0;
        int res=0;
        HashMap<Integer,Integer> map=new HashMap<>();
        for (int i=0;i<n;i++)
        {
            pre=pre+2*arr[i]-1;//replacing 0 with -1 and keeping 1 as it is
            if (pre==sum)
            {
                res=i+1;
            }
            if (map.containsKey(pre)==false)
            {
                map.put(pre,i);
                //System.out.println(arr[i]+" "+pre+" "+i);
            }
            if (map.containsKey(pre-sum))
            {
                res=Math.max(res,i-map.get(pre-sum));
            }
        }
        return res;
    }
}

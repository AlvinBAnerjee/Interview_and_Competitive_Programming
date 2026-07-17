package _4_Searching;
/*
Given an array of integers numbers that is already sorted in ascending order,
find two numbers such that they add up to a specific target number.
Example 1:

Input: numbers = [2,7,11,15], target = 9
Output: [1,2]
Explanation: The sum of 2 and 7 is 9. Therefore index1 = 1, index2 = 2.
Example 2:

Input: numbers = [2,3,4], target = 6
Output: [1,3]
Example 3:

Input: numbers = [-1,0], target = -1
Output: [1,2]

 */

import java.util.Arrays;

public class _5_Pair_Sum_But_using_2_Pointer_Approach {
    public static void main(String[] args) {
        int arr[]={2,7,11,15};
        System.out.println(Arrays.toString(findPair(arr,9)));
    }
    static int[] findPair(int arr[],int sum)
    {
        int ans[]={-1,-1};
        int l=0;
        int r=arr.length-1;
        while (l<r)
        {
            if ((arr[l]+arr[r])==sum)
            {
                ans[0]=l;
                ans[1]=r;
                return ans;
            }
            else if ((arr[l]+arr[r])>sum)
                r--;
            else
                l++;
        }
        return ans;
    }
}

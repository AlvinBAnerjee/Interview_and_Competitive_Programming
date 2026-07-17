package _22_BackTracking;

import java.util.ArrayList;
import java.util.List;

public class _3_Permutation1 {
    /*
Given an array nums of distinct integers, return all the possible permutations.
You can return the answer in any order.

Example 1:

Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
Example 2:

Input: nums = [0,1]
Output: [[0,1],[1,0]]
Example 3:

Input: nums = [1]
Output: [[1]]
     */
    List<List<Integer>> ans;
    public List<List<Integer>> permute(int[] nums) {
        ans = new ArrayList<>();

        rec(nums, 0);
        return ans;
    }
    public void rec(int arr[], int i)
    {
        if(i==arr.length)
        {
            List<Integer> t = new ArrayList<>();
            for(int x : arr)
                t.add(x);
            ans.add(t);
            return;
        }
        for(int k=i;k<arr.length;k++)
        {
            int a = arr[i];
            int b = arr[k];
            arr[i]=b;
            arr[k]=a;
            rec(arr, i+1);
            arr[i]=a;
            arr[k]=b;
        }
    }
}
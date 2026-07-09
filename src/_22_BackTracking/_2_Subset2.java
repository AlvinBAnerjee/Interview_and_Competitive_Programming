package _22_BackTracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class _2_Subset2 {
    /*
Given an integer array nums that may contain duplicates, return all possible subsets (the power set).

The solution set must not contain duplicate subsets. Return the solution in any order.



Example 1:

Input: nums = [1,2,2]
Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]
Example 2:

Input: nums = [0]
Output: [[],[0]]


Constraints:

1 <= nums.length <= 10
-10 <= nums[i] <= 10
     */
    List<List<Integer>> ans;
    boolean used[];
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        ans=new ArrayList<>();
        used=new boolean[nums.length];
        Arrays.sort(nums);
        rec(nums, 0, new ArrayList<>());
        return ans;
    }
    public void rec(int arr[] ,int idx, List<Integer> t)
    {
        if(idx>=arr.length)
        {
            List<Integer> temp=new ArrayList<>();
            temp.addAll(t);
            ans.add(temp);
            return;
        }
        for(int i=idx;i<arr.length;i++)
        {
            if(i!=idx && arr[i]==arr[i-1])continue; //takes care of duplicates
            t.add(arr[i]);
            rec(arr, i+1,t);
            t.remove(t.size()-1);
        }
        rec(arr, arr.length, t);
    }
    public void rec2(int i, int arr[], ArrayList<Integer> temp)//easier to follow
    {
        if(i==arr.length)
        {
            ArrayList<Integer> t = new ArrayList<>();
            t.addAll(temp);
            ans.add(t);
            return;
        }

        rec2(i+1, arr, temp);
        if(i>0 && arr[i]==arr[i-1] && !used[i-1])return;
        used[i]=true;
        temp.add(arr[i]);
        rec2(i+1, arr, temp);
        temp.remove(temp.size()-1);
        used[i]=false;
    }
}
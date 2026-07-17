package _22_BackTracking;

import java.util.ArrayList;
import java.util.List;

public class _1_Subset1 {
    /*
 Given an integer array nums of unique elements, return all possible subsets (the power set).

The solution set must not contain duplicate subsets. Return the solution in any order.



Example 1:

Input: nums = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
Example 2:

Input: nums = [0]
Output: [[],[0]]


Constraints:

1 <= nums.length <= 10
-10 <= nums[i] <= 10
All the numbers of nums are unique.
     */
    List<List<Integer>> ans;
    public List<List<Integer>> subsets(int[] nums) {
        ans=new ArrayList<>();
        rec(nums, 0, new ArrayList<>());
        return ans;
    }
    public void rec(int arr[] ,int idx, List<Integer> t)
    {
        if(idx==arr.length)
        {
            List<Integer> temp=new ArrayList<>();
            temp.addAll(t);
            ans.add(temp);
            return;
        }
        rec(arr,idx+1,t);
        t.add(arr[idx]);
        rec(arr, idx+1, t);
        t.remove(t.size()-1);

    }
}
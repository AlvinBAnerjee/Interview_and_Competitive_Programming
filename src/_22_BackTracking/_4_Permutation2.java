package _22_BackTracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class _4_Permutation2 {
    /*
Given a collection of numbers, nums, that might contain duplicates, return all possible unique permutations in any order.



Example 1:

Input: nums = [1,1,2]
Output:
[[1,1,2],
 [1,2,1],
 [2,1,1]]
Example 2:

Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]


Constraints:

1 <= nums.length <= 8
-10 <= nums[i] <= 10

Use an extra boolean array " boolean[] used" to indicate whether the value is added to list.

Sort the array "int[] nums" to make sure we can skip the same value.

when a number has the same value with its previous, we can use this number only if his previous is used
     */
    boolean used[];
    List<List<Integer>> ans;
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        ans = new ArrayList<>();
        used = new boolean[nums.length];

        rec(nums, 0, new ArrayList<>());
        return ans;
    }
    public void rec(int arr[], int i, ArrayList<Integer> temp)
    {
        if(i==arr.length)
        {
            List<Integer> t = new ArrayList<>();
            t.addAll(temp);
            ans.add(t);
            return;
        }
        for(int k=0;k<arr.length;k++)
        {
            if(used[k])continue;
            if(k>0 && arr[k-1]==arr[k] && !used[k-1])continue; // if the left value is already put only then you can.
            // So only the starting of same value series only he first is always included
            used[k]=true;
            temp.add(arr[k]);
            rec(arr, i+1, temp);
            temp.remove(temp.size()-1);
            used[k]=false;
        }
    }
}
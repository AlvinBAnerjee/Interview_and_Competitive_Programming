package _22_BackTracking;

import java.util.ArrayList;
import java.util.List;

public class _5_CombinationSum1 {

    /*
Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.

The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the frequency of at least one of the chosen numbers is different.

The test cases are generated such that the number of unique combinations that sum up to target is less than 150 combinations for the given input.



Example 1:

Input: candidates = [2,3,6,7], target = 7
Output: [[2,2,3],[7]]
Explanation:
2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
7 is a candidate, and 7 = 7.
These are the only two combinations.
Example 2:

Input: candidates = [2,3,5], target = 8
Output: [[2,2,2,2],[2,3,3],[3,5]]
Example 3:

Input: candidates = [2], target = 1
Output: []


Constraints:

1 <= candidates.length <= 30
2 <= candidates[i] <= 40
All elements of candidates are distinct.
1 <= target <= 40
     */
    List<List<Integer>> ans;
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        ans=new ArrayList<>();
        rec(candidates, 0, target,0,  new ArrayList<>());
        return ans;
    }
    public void rec(int arr[], int i, int target,int sum, List<Integer> t)
    {
        if(sum>target || i>=arr.length)
        {
            return;
        }

        if(sum==target)
        {
            List<Integer> temp=new ArrayList<>();
            temp.addAll(t);
            ans.add(temp);
            return;
        }

        rec(arr, i+1,target,sum,t);
        t.add(arr[i]);
        rec(arr,i,target, sum+arr[i], t);
        t.remove(t.size()-1);
    }
}
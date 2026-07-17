package _22_BackTracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class _6_CombinationSum2 {
    /*

Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates where the candidate numbers sum to target.

Each number in candidates may only be used once in the combination.

Note: The solution set must not contain duplicate combinations.



Example 1:

Input: candidates = [10,1,2,7,6,1,5], target = 8
Output:
[
[1,1,6],
[1,2,5],
[1,7],
[2,6]
]
Example 2:

Input: candidates = [2,5,2,1,2], target = 5
Output:
[
[1,2,2],
[5]
]


Constraints:

1 <= candidates.length <= 100
1 <= candidates[i] <= 50
1 <= target <= 30
     */
    List<List<Integer>> ans;
    boolean used[];
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        ans=new ArrayList<>();
        used = new boolean[candidates.length];
        rec(candidates, 0, target,0,  new ArrayList<>());
        return ans;
    }
    public void rec(int arr[], int i, int target,int sum, List<Integer> t)
    {
        if(sum==target)
        {
            List<Integer> temp=new ArrayList<>();
            temp.addAll(t);
            ans.add(temp);
            return;
        }
        if(sum>target || i>=arr.length)
        {
            return;
        }

        rec(arr, i+1,target,sum,t);
        if(i>0 && arr[i]==arr[i-1] && !used[i-1])return;
        used[i]=true;
        t.add(arr[i]);
        rec(arr,i+1,target, sum+arr[i], t);
        t.remove(t.size()-1);
        used[i]=false;
    }
}

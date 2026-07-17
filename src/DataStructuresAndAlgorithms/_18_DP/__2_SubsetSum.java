package _18_DP;

import java.util.Arrays;

public class __2_SubsetSum {
    /*
    Given a set of non-negative integers, and a value sum, determine if there is a subset of the given set with sum equal to given sum.

Example:

Input: set[] = {3, 34, 4, 12, 5, 2}, sum = 9
Output: True
There is a subset (4, 5) with sum 9.

Input: set[] = {3, 34, 4, 12, 5, 2}, sum = 30
Output: False
There is no subset that add up to 30.

Approach :
Similar to KnapSack
     */
    boolean subsetSum(int sum,int item,int elements[])
    {
        int dp[][]=new int[item+1][sum+1];
        for (int arr[]:
             dp) {
            Arrays.fill(arr,-1);

        }

       int res= subsetSumUtil(dp,sum,item,elements);
        if (res==1)
            return true;
        else
            return false;
    }
    int subsetSumUtil(int dp[][],int sum,int item,int elements[])
    {
        if (dp[item][sum]!=-1)
            return dp[item][sum];
        if (sum==0)
        {
            dp[item][sum]=1;
            return dp[item][sum];
        }
        if (item==0)
        {
            dp[item][sum]=0;
            return dp[item][sum];
        }
        if (sum>=elements[item-1])
        {
            dp[item][sum]=subsetSumUtil(dp,sum-elements[item-1],item-1,elements) | subsetSumUtil( dp, sum,item -1, elements);
        }
        else
            dp[item][sum]=subsetSumUtil( dp, sum,item -1, elements);
        return dp[item][sum];
    }

}

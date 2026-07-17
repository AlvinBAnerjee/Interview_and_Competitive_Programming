package _18_DP;

import java.util.Arrays;
/*
Given an array arr[] of size N, check if it can be partitioned into two parts such that the sum of elements in both parts is the same.

Example 1:

Input: N = 4
arr = {1, 5, 11, 5}
Output: YES
Explaination:
The two parts are {1, 5, 5} and {11}.

convert the problem to subset sum of size sum_of_all_elements/2;
 */
public class __3_EqualSumPartition {
    boolean equalSumPartition(int elements[])
    {
        int sum=0;
        for(int i=0;i<elements.length;i++)
        {
            sum=sum+ elements[i];
        }
        if (sum%2==1)
            return false;
        return subsetSum(sum/2,elements.length,elements);
    }
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

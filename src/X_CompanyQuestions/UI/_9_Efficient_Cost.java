package X_CompanyQuestions.UI;

import java.util.Arrays;

public class _9_Efficient_Cost {
    public int maxSumAfterPartitioning(int[] arr, int k) {
        int dp[]=new int[arr.length];
        Arrays.fill(dp, -1);
        return rec(arr, 0,k,dp);
    }
    int rec(int arr[], int index, int k, int dp[])
    {
        if(index==arr.length)
            return 0;
        if(dp[index]!=-1)return dp[index];
        int max=Integer.MIN_VALUE;
        int res=Integer.MAX_VALUE;
        for(int i=index;i<Math.min(arr.length, index+k);i++)
        {
            max=Math.max(max,arr[i] );
            int temp=max + rec(arr, i+1, k, dp);
            res=Math.min(res, temp);
        }
        return dp[index]=res;
    }
}

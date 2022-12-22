package _18_DP;

import java.util.Arrays;

public class __15_Matrix_Chain {
    public static int mcmRecursive(int arr[],int i,int j)
    {
        if(i==j)
            return 0;
        if(i+1==j)
            return arr[i-1]*arr[i]*arr[j];
        int min=Integer.MAX_VALUE;
        for (int k=i+1;k<=j;k++)
        {
            int total=mcmRecursive(arr,i,k-1)+mcmRecursive(arr,k,j)+arr[i-1]*arr[k-1]*arr[j];
            min=Math.min(min,total);
        }
        return min;
    }
    public static int mcmTopDown(int arr[])
    {
        int i=1;
        int j=arr.length-1;
        int dp[][]=new int[arr.length+1][arr.length+1];
        for (int a[]: dp)
        {
            Arrays.fill(a,-1);
        }
        int ans=mcmTopDownUtil(dp,arr,i,j);
        return ans;
    }
    public static int mcmTopDownUtil(int dp[][],int arr[],int i,int j)
    {
        if (dp[i][j]!=-1)
            return dp[i][j];
        if(i==j) {
            dp[i][j]=0;
            return 0;
        };
        if(i+1==j) {
            dp[i][j]= arr[i - 1] * arr[i] * arr[j];
            return dp[i][j];
        }
        int min=Integer.MAX_VALUE;
        for (int k=i+1;k<=j;k++)
        {
            int total=mcmTopDownUtil(dp,arr,i,k-1)+mcmTopDownUtil(dp,arr,k,j)+arr[i-1]*arr[k-1]*arr[j];
            min=Math.min(min,total);
        }
        dp[i][j]=min;
        return dp[i][j];
    }

    public static int mcmBottomUp(int arr[])
    {
        int n=arr.length;
        int dp[][]=new int[n][n];
        for (int i=1;i<n;i++)
        {
            dp[i][i]=0;
        }
        for (int gap=1;gap<n;gap++)
        {
            for (int i=1;i+gap<n;i++)
            {
                int a=i;
                int b=i+gap;
                int min=Integer.MAX_VALUE;
                for (int k=a+1;k<=b;k++)
                {
                    int total=dp[a][k-1]+dp[k][b]+arr[i-1]*arr[k-1]*arr[b];
                    min=Math.min(min,total);
                }
                dp[a][b]=min;
            }
        }
        return dp[1][n-1];
    }


}

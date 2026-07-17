package _18_DP;

import java.util.Arrays;

public class __19_Egg_Dropping {
    public static int findEggDropp(int n, int k)
    {

        if (k==1|| k==0)
            return k;
        if (n==1)
            return k;
        int min=Integer.MAX_VALUE;
        for (int i=1;i<=k;i++)
        {
            int res=Math.max(findEggDropp(n,k-i),findEggDropp(n-1,i-1));
            if (res<min)
                min=res;
        }
        return min+1;
    }
    public static  int findEggDropTopDown(int n,int k)
    {
        int dp[][]=new int[n+1][k+1];
        for (int arr[]:dp)
        {
            Arrays.fill(arr,-1);
        }
        return findEggDropTopDownUtil(dp,n,k);

    }
    public static int findEggDropTopDownUtil(int dp[][],int n,int k)
    {
        if (dp[n][k]!=-1)
            return dp[n][k];
        if (k==1|| k==0) {
            dp[n][k]=k;
            return k;
        }
        if (n==1) {
            dp[n][k]=k;

            return k;
        }
        int min=Integer.MAX_VALUE;
        for (int i=1;i<=k;i++)
        {
            int res=Math.max(findEggDropTopDownUtil(dp,n,k-i),findEggDropTopDownUtil(dp,n-1,i-1));
            if (res<min)
                min=res;
        }
        dp[n][k]=min+1;
        return min+1;
    }
}

package _18_DP;

public class __8_RodCutting {
    int findMaxValue(int N, int prices[])
    {
        int dp[][]=new int[N+1][N+1];
        for (int i=1;i<=N;i++)
        {
            for (int j=1;j<=N;j++)
            {
                if (j>=i)
                {
                    dp[i][j]=Math.max(dp[i][j-i]+prices[i-1],dp[i-1][j]);
                }
                else
                {
                    dp[i][j]=dp[i-1][j];
                }
            }
        }
        return dp[N][N];
    }
}

package _18_DP;
import  java.util.*;
public class __10_CoinChangeMinNoOfCoins {
    public int coinChange(int[] coins, int amount) {
        int N=coins.length;
        int dp[][]=new int[coins.length + 1][amount + 1];
        Arrays.fill(dp[0],Integer.MAX_VALUE);
        for(int i=1;i<=N;i++)
        {
            for(int j=1;j<=amount;j++)
            {
                dp[i][j]=dp[i-1][j];
                if(j>=coins[i-1] && dp[i][j-coins[i-1]]!=Integer.MAX_VALUE)
                {
                    dp[i][j]=Math.min(dp[i][j],dp[i][j-coins[i-1]]+1);
                }
            }
        }
        if(dp[N][amount]==Integer.MAX_VALUE)
        {
            return -1;
        }
        else
            return dp[N][amount];
    }
}

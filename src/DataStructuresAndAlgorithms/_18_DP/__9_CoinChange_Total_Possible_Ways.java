package _18_DP;

public class __9_CoinChange_Total_Possible_Ways {
    public int countWays(int coins[],  int amount)
    {
        int m=coins.length;
        //code here
        int dp[][]=new int[m+1][amount+1];
        for(int i=0;i<=m;i++)
        {
            dp[i][0]=1;
        }
        for(int i=1;i<=m;i++)
        {
            for(int j=1;j<=amount;j++)
            {
                if(j>=coins[i-1])
                {
                    dp[i][j]=(dp[i][j-coins[i-1]]+dp[i-1][j]);
                }
                else
                {
                    dp[i][j]=dp[i-1][j];
                }
            }
        }
        return dp[m][amount];
    }
}

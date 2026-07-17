package _18_DP;

import java.util.Scanner;
//Time complexity is 0(no_of_coins*total_length)
/*
Given a value N, if we want to make change for N cents,
and we have infinite supply of each of S = { S1, S2, .. , Sm}
valued coins, how many ways can we make the change? The order of coins doesn’t matter.
For example, for N = 4 and S = {1,2,3},
there are four solutions: {1,1,1,1},{1,1,2},{2,2},{1,3}.
So output should be 4. For N = 10 and S = {2, 5, 3, 6},
there are five solutions: {2,2,2,2,2}, {2,2,3,3}, {2,2,6}, {2,3,5} and {5,5}.
So the output should be 5.
 */
public class _3_CoinsChange {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int no_of_coins=3;
        int changes[]= {1,5,6};
        int total_amount=7;
        System.out.println(findChangeNoob(total_amount,no_of_coins,changes));
    }
    static int findChangeNoob(int total_amount,int no_of_coins,int changes[])
    {
        int dp[][]=new int[total_amount+1][no_of_coins+1];
        for (int i=0;i<=total_amount;i++)
        {
            dp[i][0]=Integer.MAX_VALUE;
        }
        for (int j=0;j<=no_of_coins;j++)
        {
            dp[0][j]=0;
        }
        for(int i=1;i<=total_amount;i++)
        {
            for (int j=1;j<=no_of_coins;j++)
            {
                dp[i][j]=dp[i][j-1];
                if (i>=changes[j-1] && dp[i-changes[j-1]][j]!=Integer.MAX_VALUE)
                {
                    dp[i][j]=Math.min(dp[i][j],dp[i-changes[j-1]][j]+1);
                }
            }
        }
        return dp[total_amount][no_of_coins];
    }



}

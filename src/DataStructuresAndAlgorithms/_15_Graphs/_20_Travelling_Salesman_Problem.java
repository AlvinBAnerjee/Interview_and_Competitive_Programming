package _15_Graphs;

import java.util.Arrays;

public class _20_Travelling_Salesman_Problem {
    static int visited_all;
    public int total_cost(int[][] cost)
    {
        // Code here

        int n=cost.length;
        visited_all=(1<<n)-1;
        int dp[][]=new int[2048][n];// n<=11
        for(int x[]:dp)
        {
            Arrays.fill(x,-1);
        }
        return tsp(1,0,dp,cost);
    }
    public int tsp(int mask, int city,int [][]dp,int [][] cost)
    {
        if(dp[mask][city]!=-1)
            return dp[mask][city];
        if(mask==visited_all)
        {
            dp[mask][city]=cost[city][0];
            return dp[mask][city];
        }
        int min=Integer.MAX_VALUE;
        for(int i=0;i<cost.length;i++)
        {
            if((mask & 1<<i)==0)
            {
                int temp=cost[city][i]+ tsp(mask | (1<<i),i,dp,cost);
                min=Math.min(min,temp);
            }
        }
        dp[mask][city]=min;
        return min;

    }
}

package _18_DP;

import java.util.Arrays;

public class __1_Knapsack {
    public static void main(String[] args) {

    }
    public int knapsackRecursive(int capacity, int item, int weights[], int profits[]){
        if (capacity==0 || item==0)
            return 0;
        if (capacity>=weights[item-1])
        {
            return Math.max(knapsackRecursive(capacity-weights[item-1],item-1,weights,profits)+profits[item-1]//including item
                    ,knapsackRecursive(capacity,item-1,weights,profits));//not including item
        }
        return knapsackRecursive(capacity,item-1,weights,profits);
    }

    public int knapsackTopDown(int capacity, int item, int weights[], int profits[]){
        int dp[][]=new int[item+1][capacity+1];
        for (int arr[]: dp) {
            Arrays.fill(arr,-1);

        }
        return knapsackTopDownUtil(dp,capacity,item-1,weights,profits);
    }

    public int knapsackTopDownUtil(int dp[][],int capacity, int item, int weights[], int profits[]){
        if (dp[item][capacity]!=-1)
            return dp[item][capacity];
        if (capacity==0 || item==0) {
            dp[item][capacity]=0;
            return dp[item][capacity];
        }
        if (capacity>=weights[item-1])
        {
           dp[item][capacity]= Math.max(knapsackTopDownUtil(dp,capacity-weights[item-1],item-1,weights,profits)+profits[item-1]//including item
                    ,knapsackTopDownUtil(dp,capacity,item-1,weights,profits));//not including item
        }
        else
         dp[item][capacity]=knapsackTopDownUtil(dp,capacity,item-1,weights,profits);

        return dp[item][capacity];
    }

    public int knapsackBottomUp(int capacity, int item,int weights[], int profits[])
    {
        int dp[][]=new int[item+1][capacity+1];
        for (int i=1;i<=item;i++)
        {
            for (int j=1;j<=capacity;j++)
            {
                dp[i][j]=dp[i-1][j];
                if (j>=weights[i-1])
                    dp[i][j]=Math.max(dp[i][j],dp[i-1][j-weights[i-1]]+profits[i-1]);
            }
        }
        return dp[item][capacity];
    }
}

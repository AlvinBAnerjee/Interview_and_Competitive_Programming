package X_CompanyQuestions.Navi;

import java.util.Arrays;
import java.util.Scanner;

public class StockBuySell {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int t=sc.nextInt();
        while(t-->0){
            int k=sc.nextInt();
            int n=sc.nextInt();
            int price[]=new int[n];
            for(int i=0;i<n;i++){
                price[i]=sc.nextInt();
            }
            System.out.println(solve(price, n ,k));
        }
    }
    public static int solve(int price[], int n, int k)
    {
        int dp[][][]=new int[n][2][k+1];
        for(int x[][]: dp)
        {
            for(int y[]:x)
            {
                Arrays.fill(y,-1);
            }
        }

        return rec(0, 1, k, price, dp);
    }
    public static  int rec(int day, int canBuy, int cap, int price[], int dp[][][])
    {
        if(day==price.length)return  0;
        if(cap==0)return 0;
        if(dp[day][canBuy][cap]!=-1)return dp[day][canBuy][cap];
        if(canBuy==1){
            return dp[day][canBuy][cap]=Math.max( -price[day] + rec(day+1, 0, cap, price,dp),
                    0 + rec(day+1, 1, cap, price,dp));
        }
        else
        {
            return dp[day][canBuy][cap]=Math.max( price[day] + rec(day+1, 1, cap-1, price,dp),
                    0 + rec(day+1, 0, cap, price,dp));
        }
    }
}

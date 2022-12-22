package X_CompanyQuestions.Navi;

import java.util.Arrays;
import java.util.Scanner;

public class CruelBoss {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int q=sc.nextInt();
        int dp[]=new int[200];
        Arrays.fill(dp,-1);
        rec(150, dp);
        while(q-->0)
        {
            int n=sc.nextInt();
            System.out.println(dp[n]);
        }
    }
    public static  int rec(int n, int dp[])
    {
        if(n<=0)return 0;
        if(dp[n]!=-1)return dp[n];
        if(n<=2)return 1;
        if(n==3)return  2;
        return  dp[n]=rec(n-1,dp)+rec(n-3,dp);
    }
}

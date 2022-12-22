package _18_DP;

import java.util.Arrays;
import java.util.Scanner;

public class _2_01_Knapsack {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        ///lets do it using recusive dp
        int sum=10;
        int n=3;
        int p[] = {7, 8, 4};
        int w[] = {3, 8, 6};
        int ans[][]=new int[n+1][sum+1];
        for (int i=0;i<=n;i++)
            for (int j=0;j<=sum;j++)
                ans[i][j]=-1;

        System.out.println(findMax(sum,n,p,w,ans));
        for (int i=0;i<=n;i++) {
            for (int j = 0; j <= sum; j++) {
                System.out.print(ans[i][j]+" ");
            }
            System.out.println();
        }
        findMaxSpaceOptimised(sum,n,p,w);

    }
    static int findMax(int sum,int n,int p[],int w[],int ans[][])
    {
       if (n==0||sum==0) {
           return 0;
       }
       if (ans[n][sum]!=-1)
           return ans[n][sum];
        if (w[n-1]>sum)
            return findMax(sum,n-1,p,w,ans);
       else
       {
           ans[n][sum]=Math.max(findMax(sum,n-1,p,w,ans),findMax(sum-w[n-1],n-1,p,w,ans)+p[n-1]);
       }
       return ans[n][sum];
    }
    static void findMaxSpaceOptimised(int sum, int n, int p[],int w[])
    {
        int dp[][]=new int[sum+1][n+1];
        int prev[]=new int[sum+1];
        Arrays.fill(prev,0);
        int now[]=new int[sum+1];
        for (int i=1;i<=n;i++)
        {
            for (int j=0;j<=sum;j++)
            {
                now[j]=0;
                if (j==0)
                    now[j]=0;
                else
                {
                    now[j]=prev[j];
                    if (j>=w[i-1])
                    {
                        now[j]=Math.max(now[j],prev[j-w[i-1]]+p[i-1]);
                    }

                }
            }
            for (int j=0;j<=sum;j++)
            {
                prev[j]=now[j];
            }
        }
        System.out.println(now[sum]);
    }
}

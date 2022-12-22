package X_CompanyQuestions.Oracle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class DistinctItems {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        int k=sc.nextInt();
        int arr[]=new int[m];
        HashSet<Integer> set=new HashSet<>();
        for(int i=0;i<m;i++)
        {
            arr[i]=sc.nextInt();
            set.add(arr[i]);
        }
        int dp[][]=new int[n+1][k+1];
        for(int i=1;i<=n;i++)
        {
            for(int j=1;j<=k;j++)
            {
                if(set.contains(i)){
                    dp[i][j]=dp[i-1][j];
                    continue;
                }
                if(j>=i)
                {
                    dp[i][j]=Math.max(dp[i-1][j], dp[i-1][j-i]+1);
                }
                else
                {
                    dp[i][j]=dp[i-1][j];
                }
            }
        }
        System.out.println(dp[n][k]+m);
    }
}

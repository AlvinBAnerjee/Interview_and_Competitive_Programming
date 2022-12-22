package X_CompanyQuestions.Navi;

import java.util.Arrays;
import java.util.Scanner;

public class BuyingParts {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int p=sc.nextInt();
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
            arr[i]=sc.nextInt();
        Arrays.sort(arr);
        int dp[][]=new int[n+2][p+2];
        for(int x[]:dp)
        {
            Arrays.fill(x,-1);
        }
        System.out.println(rec(0,p,arr,dp));
    }
    public  static  int rec(int idx, int p,int arr[], int dp[][])
    {
        if(idx>=arr.length && p>0)return 1000000000;
        if(p==0)return 0;
        if(dp[idx][p]!=-1)return dp[idx][p];

        int take=Integer.MAX_VALUE;
        if(idx<arr.length-1)
        {
            take=Math.max(arr[idx+1]-arr[idx], rec(idx+2, p-1, arr,dp));
        }
        int dont=rec(idx+1, p, arr,dp );
        return dp[idx][p]=Math.min(take, dont);
    }

}

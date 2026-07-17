package _18_DP;

import java.util.Arrays;

public class _14_Max_Sum_With_No_two_Consecutives {
    public static void main(String[] args) {
        int arr[]={8,7,6,10};
        System.out.println(findMaxsubarrayRec(arr,arr.length-1));
        System.out.println(findMaxsubarrayDP(arr));

    }
    static int findMaxsubarrayRec(int arr[],int n)
    {
        if (n==0)
        return arr[0];
        if (n==1)
            return Math.max(arr[0],arr[1]);

        return Math.max(findMaxsubarrayRec(arr,n-1),findMaxsubarrayRec(arr,n-2)+arr[n]);
    }
    static int findMaxsubarrayDP(int arr[])
    {
        int n=arr.length;
        if (n==1)
            return arr[0];
        else if (n==2)
            return Math.max(arr[0],arr[1]);
        int dp[]=new int[n];
        dp[0]=arr[0];
        dp[1]=Math.max(arr[0],arr[1]);
        for (int i=2;i<n;i++)
        {
            dp[i]=Math.max(dp[i-1],dp[i-2]+arr[i]);
        }
        return dp[n-1];
    }
}

package _18_DP;

public class    _11_Subset_Sum_Problems {
    public static void main(String[] args) {
        int arr[]={10,20};
        int sum=40;
        System.out.println(findMaxSubsets(arr,sum));

    }
    static int findMaxSubsets(int arr[],int sum)//This problem is similar to Coins change this is bounded but that one is not
    {
        int dp[][]=new int[sum+1][arr.length+1];
        for (int i=0;i<=sum;i++)
            dp[i][0]=0;
        for (int j=0;j<= arr.length;j++)
            dp[0][j]=1;
        for (int i=1;i<=sum;i++)
        {
            for (int j=1;j<=arr.length;j++)
            {
                dp[i][j]=dp[i][j-1];
                if (arr[j-1]<=i)
                    dp[i][j]=dp[i][j]+dp[i-arr[j-1]][j-1];
            }
        }
        return dp[sum][arr.length];
    }
}

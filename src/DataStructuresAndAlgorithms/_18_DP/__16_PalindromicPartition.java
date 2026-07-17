package _18_DP;

import java.util.Arrays;

public class __16_PalindromicPartition {
    public static boolean isPalindromic(String str,int i,int j)
    {
        while(i<j)
        {
            if (str.charAt(i)==str.charAt(j))
            {
                i++;
                j--;
            }
            else
                return false;
        }
        return true;
    }
    public static int PPRec(String str,int i,int j)
    {
        if (i==j)
        {
            return 0;
        }
        if (isPalindromic(str,i,j))
        {
            return 0;
        }
        int min=Integer.MAX_VALUE;
        for (int k=i;k<j;k++)
        {
            int total=PPRec(str,i,k)+PPRec(str,k+1,j)+1;
            min=Math.min(min,total);
        }
        return min;
    }

    public int PPTopDown(String str)
    {
        int dp[][]=new int[str.length()][str.length()];
        for (int arr[]:dp)
        {
            Arrays.fill(arr,-1);
        }
        return PPTopDownUtil(dp,str,0,str.length()-1);
    }
    public  int PPTopDownUtil(int dp[][],String str,int i,int j)
    {
        if (dp[i][j]!=-1)
            return dp[i][j];
        if (i==j)
        {
            dp[i][j]=0;
            return 0;
        }
        if (isPalindromic(str,i,j))
        {
            dp[i][j]=0;
            return 0;
        }
        int min=Integer.MAX_VALUE;
        for (int k=i;k<j;k++)
        {
            int total=PPTopDownUtil(dp,str,i,k)+PPTopDownUtil(dp,str,k+1,j)+1;
            min=Math.min(min,total);
        }
        dp[i][j]=min;
        return min;
    }
}

package _18_DP;

public class __13_EditDistance {
    static int FindRec(String str1,String str2,int m,int n)
    {
        if (m==0)
            return n;
        if (n==0)
            return m;
        if (str1.charAt(m-1)==str2.charAt(n-1))
            return FindRec(str1,str2,m-1,n-1);
        int min=Math.min(FindRec(str1,str2,m-1,n),FindRec(str1,str2,m,n-1));
        return 1+Math.min(min,FindRec(str1,str2,m-1,n-1));
    }
    static int FindDP(String str1,String str2,int m,int n)
    {
        int dp[][]=new int[m+1][n+1];
        for (int i=0;i<=m;i++)
            dp[i][0]=i;
        for (int j=0;j<=n;j++)
            dp[0][j]=j;
        for (int i=1;i<=m;i++)
        {
            for (int j=1;j<=n;j++)
            {
                if (str1.charAt(i-1)==str2.charAt(j-1))
                    dp[i][j]=dp[i-1][j-1];
                else
                {
                    int min=Math.min(dp[i-1][j],dp[i][j-1]);
                    dp[i][j]=1+Math.min(min,dp[i-1][j-1]);
                }
            }
        }
        return dp[m][n];
    }
}

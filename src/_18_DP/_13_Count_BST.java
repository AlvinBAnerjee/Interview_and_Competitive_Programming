package _18_DP;

public class _13_Count_BST {
    public static void main(String[] args) {
        int n=3;
        System.out.println(findPossibleBSTCatalan(n));
        System.out.println(findPossibleBSTDP(n));
        System.out.println(findPossibleBSTRec(n));

    }
    static int findPossibleBSTRec(int n)//exponential
    {
        if (n==0)
            return 1;
        if (n==1)
            return 1;
        if (n==2)
            return 2;
        else
        {
            int sum=0;
            for (int i=1;i<=n;i++)
            {
                sum=sum+findPossibleBSTRec(i-1)*findPossibleBSTRec(n-i);
            }
            return sum;
        }
    }
    static int findPossibleBSTDP(int n)//this is O(n^2)
    {
        int dp[]=new int[n+1];
        dp[0]=1;
        dp[1]=1;
        dp[2]=2;
        for (int i=3;i<=n;i++)
        {
            int sum=0;
            for (int j=1;j<=i;j++)
            {
                sum=sum+dp[j-1]*dp[i-j];
            }
            dp[i]=sum;
        }
        return dp[n];
    }
    static long findPossibleBSTCatalan(int n)//this is O(n)
    {
        return binomialCoeff(2*n,n)/(n+1);
    }
    static long binomialCoeff(int n, int k)
    {
        long res = 1;

        // Since C(n, k) = C(n, n-k)
        if (k > n - k) {
            k = n - k;
        }

        // Calculate value of [n*(n-1)*---*(n-k+1)] /
        // [k*(k-1)*---*1]
        for (int i = 0; i < k; ++i) {
            res *= (n - i);
            res /= (i + 1);
        }

        return res;
    }
}

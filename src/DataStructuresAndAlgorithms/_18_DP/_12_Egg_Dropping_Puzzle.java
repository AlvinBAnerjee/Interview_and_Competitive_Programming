package _18_DP;
public class _12_Egg_Dropping_Puzzle {
    static int dp2[][] = new int[101][10001];

    public static void main(String[] args) {
        int n = 10;
        int f = 1000;
        System.out.println(findMinTrailsDPTopDown(n,f));
        System.out.println(findMinTrialsDP(n, f));
        System.out.println(findMinTrialsRec(n, f));
        System.out.println(findMinTrialsDP(n, f));


    }

    static int findMinTrialsRec(int n, int k) {
        if (k == 1 || k == 0)
            return k;
        if (n == 1)
            return k;
        int min = Integer.MAX_VALUE;
        for (int i = 1; i <= k; i++) {
            min = Math.min(min,//the previous value
                    Math.max(findMinTrialsRec(n - 1, i - 1), findMinTrialsRec(n, k - i))//the new calculated value
            );
        }
        return min + 1;
    }

    static int findMinTrialsDP(int n, int k) {
        int dp[][] = new int[n + 1][k + 1];
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 0;
            dp[i][1] = 1;
        }
        for (int j = 1; j <= k; j++)
            dp[1][j] = j;

        for (int i = 2; i <= n; i++) {
            for (int j = 2; j <= k; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int x = 1; x <= j; x++) {
                    dp[i][j] = Math.min(dp[i][j],   Math.max( dp[i - 1][x - 1], dp[i][j - x] )     + 1);
                }
            }
        }
        return dp[n][k];
    }

    static int findMinTrailsDPTopDown(int n, int k) {
        if (dp2[n][k] != 0)
            return dp2[n][k];

        if (k == 1 || k == 0) {
            return k;
        }
        if (n == 1)
            return k;

        int min = Integer.MAX_VALUE;
        for (int i = 1; i <= k; i++) {
            min = Math.min(min,//the previous value
                    Math.max(findMinTrailsDPTopDown(n - 1, i - 1), findMinTrailsDPTopDown(n, k - i))//the new calculated value
            );
        }
        min++;
        dp2[n][k] = min;
        return dp2[n][k];

    }
}

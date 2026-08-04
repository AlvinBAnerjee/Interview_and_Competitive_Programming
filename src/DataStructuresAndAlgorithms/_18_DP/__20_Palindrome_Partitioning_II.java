package DataStructuresAndAlgorithms._18_DP;

import java.util.Arrays;

/**
 * LeetCode 132 - Palindrome Partitioning II.
 * Finds the minimum number of cuts needed to partition a string so that
 * every substring of the partition is a palindrome, using memoized
 * recursion over the cut point (dp) plus a memoized palindrome check (p).
 */
public class __20_Palindrome_Partitioning_II {
    int p[][];
    int dp[][];

    public int minCut(String s) {
        int n = s.length();
        p = new int[n][n];
        for (int arr[] : p) {
            Arrays.fill(arr, -1);
        }
        dp = new int[n][n];
        for (int arr[] : dp) {
            Arrays.fill(arr, -1);
        }

        return rec(s, 0, n - 1);
    }

    public int rec(String s, int i, int j) {
        if (i > j) return 0;
        if (dp[i][j] != -1) return dp[i][j];
        if (isPalindrome(s, i, j) == 1) return dp[i][j] = 0;

        int min = Integer.MAX_VALUE;
        for (int k = i; k <= j; k++) {
            if (isPalindrome(s, i, k) == 1)
                min = Math.min(min, 1 + rec(s, k + 1, j));
        }
        return dp[i][j] = min;
    }

    public int isPalindrome(String s, int i, int j) {
        if (i > j) return 1;
        if (p[i][j] != -1) return p[i][j];

        if (s.charAt(i) != s.charAt(j))
            p[i][j] = 0;
        else
            p[i][j] = isPalindrome(s, i + 1, j - 1);
        return p[i][j];
    }
}

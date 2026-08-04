package DataStructuresAndAlgorithms._18_DP;

import java.util.Arrays;

/**
 * LeetCode 887 - Super Egg Drop.
 * Same recurrence as findEggDrop in __19_Egg_Dropping, but instead of
 * trying every floor 1..f for the first drop, binary searches for the
 * floor where the two outcomes balance out (see comments in rec).
 */
public class __21_Super_Egg_Drop_Binary_Search {
    int dp[][];

    public int superEggDrop(int k, int n) {
        dp = new int[k + 1][n + 1];

        for (int arr[] : dp)
            Arrays.fill(arr, -1);

        return rec(k, n);
    }

    public int rec(int e, int f) {
        if (e <= 1) return f;
        if (f <= 1) return f;
        if (dp[e][f] != -1) return dp[e][f];

        int min = Integer.MAX_VALUE;
        int low = 1;
        int high = f;
        // breaks(mid) = rec(e-1, mid-1) rises as mid increases (an egg break
        // leaves more floors above to search with one fewer egg).
        // doesnt(mid) = rec(e, f-mid) falls as mid increases (surviving
        // leaves fewer floors left above to search).
        // The worst case for a given mid is max(breaks, doesnt), which is
        // minimized right where the two curves cross, so instead of scanning
        // every floor we binary search for that crossover point.
        while (low <= high) {
            int mid = (low + high) / 2;
            int breaks = rec(e - 1, mid - 1);
            int doesnt = rec(e, f - mid);
            min = Math.min(Math.max(doesnt, breaks) + 1, min);

            if (breaks > doesnt) {
                // "Egg breaks" already dominates the worst case here, and it
                // only gets larger as mid grows, so move left: shrink breaks
                // (and let doesnt grow) to head toward the crossover.
                high = mid - 1;
            } else {
                // "Egg survives" dominates (or ties) instead, and it only
                // gets larger as mid shrinks, so move right: shrink doesnt
                // (and let breaks grow) to head toward the crossover.
                low = mid + 1;
            }
        }
        return dp[e][f] = min;
    }
}

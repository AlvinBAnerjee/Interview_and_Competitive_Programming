package DataStructuresAndAlgorithms._18_DP;
/*
The applications/variations are :
1. No of deletions to make an array sorted
2. Maximum sum increasing sub sequence
3. Maximum Length Bitonic sub sequence (a sequence that is first increasing and then decreasing)
4. Number of Longest Increasing Subsequences  (LeetCode 673)

5.Consider a 2-D map with a horizontal river passing through its center.
There are n cities on the southern bank with x-coordinates a(1) … a(n) and n cities
on the northern bank with x-coordinates b(1) … b(n). You want to connect as many north-south pairs
of cities as possible with bridges such that no two bridges cross. When connecting cities, you can
only connect city a(i) on the northern bank to city b(i) on the southern bank. Maximum number of bridges
that can be built to connect north-south pairs with the aforementioned constraints.


null

The values in the upper bank can be considered as the northern x-coordinates of the cities and the values in the
bottom bank can be considered as the corresponding southern x-coordinates of the cities to
which the northern x-coordinate city can be connected.
Examples:


Input : 6 4 2 1
        2 3 6 5
Output : Maximum number of bridges = 2
Explanation: Let the north-south x-coordinates
be written in increasing order.

1  2  3  4  5  6
  \  \
   \  \        For the north-south pairs
    \  \       (2, 6) and (1, 5)
     \  \      the bridges can be built.
      \  \     We can consider other pairs also,
       \  \    but then only one bridge can be built
        \  \   because more than one bridge built will
         \  \  then cross each other.
          \  \
1  2  3  4  5  6

Input : 8 1 4 3 5 2 6 7
        1 2 3 4 5 6 7 8
Output : Maximum number of bridges = 5


Recommended: Please try your approach on {IDE} first, before moving on to the solution.
Approach: It is a variation of LIS problem. The following are the steps to solve the problem.

1. Sort on the basis of first value
2. Apply LIS on second value

*/
public class _6_LIS_Applications {
    static int maxSumIS(int arr[], int n)//maximum sum increasing sub sequence
    {
        int i, j, max = 0;
        int msis[] = new int[n];

        for (i = 0; i < n; i++)
            msis[i] = arr[i];

        for (i = 1; i < n; i++)
            for (j = 0; j < i; j++)
                if (arr[i] > arr[j] &&
                        msis[i] < msis[j] + arr[i])
                    msis[i] = msis[j] + arr[i];

        for (i = 0; i < n; i++)
            if (max < msis[i])
                max = msis[i];

        return max;
    }
    static int lbs( int arr[], int n )//bitonic
    {
        int i, j;

        /* Allocate memory for LIS[] and initialize LIS values as 1 for
            all indexes */
        int[] lis = new int[n];
        for (i = 0; i < n; i++)
            lis[i] = 1;

        /* Compute LIS values from left to right */
        for (i = 1; i < n; i++)
            for (j = 0; j < i; j++)
                if (arr[i] > arr[j] && lis[i] < lis[j] + 1)
                    lis[i] = lis[j] + 1;

        /* Allocate memory for lds and initialize LDS values for
            all indexes */
        int[] lds = new int [n];
        for (i = 0; i < n; i++)
            lds[i] = 1;

        /* Compute LDS values from right to left */
        for (i = n-2; i >= 0; i--)
            for (j = n-1; j > i; j--)
                if (arr[i] > arr[j] && lds[i] < lds[j] + 1)
                    lds[i] = lds[j] + 1;


        /* Return the maximum value of lis[i] + lds[i] - 1*/
        int max = lis[0] + lds[0] - 1;
        for (i = 1; i < n; i++)
            if (lis[i] + lds[i] - 1 > max)
                max = lis[i] + lds[i] - 1;

        return max;
    }

    /*
    NUMBER OF LIS — how many distinct longest increasing subsequences exist.

    Plain LIS keeps one number per index: len[i] = length of the LIS ending at i.
    To count them we carry a second number alongside it:

        len[i] = length of the longest increasing subsequence ENDING EXACTLY at i
        cnt[i] = how many such subsequences of that length end at i

    Walking j < i with arr[j] < arr[i], there are exactly two cases:

        len[j] + 1 >  len[i]  ->  j gives a strictly LONGER chain than anything seen
                                  so far, so the old tally is obsolete: RESET
                                  len[i] = len[j]+1 and cnt[i] = cnt[j]
        len[j] + 1 == len[i]  ->  j is another route to the SAME best length,
                                  so ACCUMULATE: cnt[i] += cnt[j]

    (`>` resets, `==` accumulates. Getting these two confused is the whole bug
    surface of this problem.) Note we add cnt[j], never 1 — j itself may already
    be reachable in several ways, and every one of those extends through i.

    The answer is the sum of cnt[i] over every i whose len[i] equals the maximum,
    because an LIS can end at any such index. O(n^2) time, O(n) space.
    */
    static int countLIS(int arr[], int n) {
        return lisLengthAndCount(arr, n)[1];
    }

    // returns {length of the LIS, number of distinct LIS}
    static int[] lisLengthAndCount(int arr[], int n) {
        if (n == 0) return new int[]{0, 0};

        int len[] = new int[n];
        int cnt[] = new int[n];

        for (int i = 0; i < n; i++) {
            len[i] = 1;                       // arr[i] alone is always a valid LIS
            cnt[i] = 1;                       // and there is exactly one such way
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {        // strictly increasing
                    if (len[j] + 1 > len[i]) {
                        len[i] = len[j] + 1;  // longer chain found -> restart tally
                        cnt[i] = cnt[j];
                    } else if (len[j] + 1 == len[i]) {
                        cnt[i] += cnt[j];     // same length, another way -> add
                    }
                }
            }
        }

        int max = 0;
        for (int i = 0; i < n; i++)
            max = Math.max(max, len[i]);

        int total = 0;
        for (int i = 0; i < n; i++)
            if (len[i] == max)                // an LIS may end at ANY index
                total += cnt[i];              // achieving the maximum length

        return new int[]{max, total};
    }

    public static void main(String[] args) {
        // the same array used in the LIS notes in _5_Longest_Increasing_Subsequence.java
        int a[] = {10, 22, 9, 33, 21, 50, 41, 60, 80};
        int r[] = lisLengthAndCount(a, a.length);
        System.out.println("LIS length = " + r[0] + ", number of LIS = " + r[1]);
        // length 6, and TWO of them: {10,22,33,50,60,80} and {10,22,33,41,60,80}

        int b[] = {1, 3, 5, 4, 7};
        System.out.println("countLIS([1,3,5,4,7])   = " + countLIS(b, b.length));
        // 2  -> {1,3,5,7} and {1,3,4,7}

        int c[] = {2, 2, 2, 2, 2};
        System.out.println("countLIS([2,2,2,2,2])   = " + countLIS(c, c.length));
        // 5  -> nothing is strictly increasing, so every single element is its own LIS

        int d[] = {1, 101, 2, 3, 100, 4, 5};
        System.out.println("maxSumIS               = " + maxSumIS(d, d.length));   // 106

        int e[] = {1, 11, 2, 10, 4, 5, 2, 1};
        System.out.println("lbs (bitonic)          = " + lbs(e, e.length));        // 6
    }
}


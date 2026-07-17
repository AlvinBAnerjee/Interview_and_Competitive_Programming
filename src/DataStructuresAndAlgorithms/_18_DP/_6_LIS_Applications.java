package _18_DP;
/*
The applications/variations are :
1. No of deletions to make an array sorted
2. Maximum sum increasing sub sequence
3. Maximum Length Bitonic sub sequence (a sequence that is first increasing and then decreasing)
Consider a 2-D map with a horizontal river passing through its center.
There are n cities on the southern bank with x-coordinates a(1) … a(n) and n cities
on the northern bank with x-coordinates b(1) … b(n). You want to connect as many north-south pairs
of cities as possible with bridges such that no two bridges cross. When connecting cities, you can
only connect city a(i) on the northern bank to city b(i) on the southern bank. Maximum number of bridges
that can be built to connect north-south pairs with the aforementioned constraints.


null

The values in the upper bank can be considered as the northern x-coordinates of the cities and the values in the bottom bank can be considered as the corresponding southern x-coordinates of the cities to which the northern x-coordinate city can be connected.
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

}


package DataStructuresAndAlgorithms._18_DP;

import java.util.Scanner;

/*
LONGEST COMMON SUBSEQUENCE — O(m*n) table.

    dp[i][j] = length of the LCS of the first i chars of str1 and first j chars of str2

    chars match     ->  dp[i][j] = 1 + dp[i-1][j-1]     (both strings can consume it)
    chars differ    ->  dp[i][j] = max(dp[i-1][j], dp[i][j-1])
                                                        (drop the last char of one or the other)

Row 0 / column 0 stay 0: an empty string shares nothing. Because dp[i][j] depends only on
the cell above-left, above, and left, one pass filling top-to-bottom, left-to-right works.

Reading the string back out: start at dp[m][n] and walk BACKWARDS. On a match, that char is
in the LCS — take it and move diagonally. On a mismatch, step toward whichever neighbour
holds the larger value (that is the direction the length came from). Building the answer by
prepending means it comes out in the right order.

--------------------------------------------------------------------------------
WORKED EXAMPLE      str1 = "AGGTAB"      str2 = "GXTXAYB"

           ""   G   X   T   X   A   Y   B
      ""    0   0   0   0   0   0   0   0
      A     0   0   0   0   0   1   1   1
      G     0   1   1   1   1   1   1   1
      G     0   1   1   1   1   1   1   1
      T     0   1   1   2   2   2   2   2
      A     0   1   1   2   2   3   3   3
      B     0   1   1   2   2   3   3   4

    dp[6][7] = 4   ->   ANSWER: LCS = "GTAB", length 4

    Spot-checks against the two rules:
      row A, col A  : 'A' == 'A'  ->  1 + dp[""][X] = 1 + 0 = 1
      row T, col T  : 'T' == 'T'  ->  1 + dp[G][X]  = 1 + 1 = 2
      row B, col B  : 'B' == 'B'  ->  1 + dp[A][Y]  = 1 + 3 = 4
      row G, col X  : 'G' != 'X'  ->  max(dp[A][X]=0, dp[G][G]=1) = 1

    Traceback from dp[6][7], collecting on each match:  B <- A <- T <- G  =>  "GTAB"

Note "GTAB" is one of possibly several LCSs of the same length — the traceback's tie-break
(`if (lengths[i-1][j] > lengths[i][j-1]) i--; else j--;`) just picks one of them. The
LENGTH is unique; the string need not be.
--------------------------------------------------------------------------------
*/
public class _1_LCS {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str1=sc.nextLine();
        String str2=sc.nextLine();
        int m=str1.length();
        int n=str2.length();
        int lengths[][]=new int[m+1][n+1];
        for (int i=0;i<=m;i++)
        {
            for (int j=0;j<=n;j++)
            {
                if (i==0||j==0) {
                    lengths[i][j] = 0;
                }
                else if (str1.charAt(i-1)==str2.charAt(j-1))
                {
                    lengths[i][j]=1+lengths[i-1][j-1];
                }
                else
                {
                    lengths[i][j]=Math.max(lengths[i-1][j],lengths[i][j-1]);//exclude the last element from string 1 or string 2
                }
            }
        }

        for (int i=0;i<=m;i++)
        {
            for (int j=0;j<=n;j++)
            {
                System.out.print(lengths[i][j]+  "  ");
            }
            System.out.println();
        }
        System.out.println("The longest LCS is:");
        int i=m;
        int j=n;
        String ans="";
        while (i!=0&&j!=0)
        {
            if (str1.charAt(i-1)==str2.charAt(j-1))
            {
                ans=str1.charAt(i-1)+ans;
                i--;
                j--;
            }
            else
            {
                if (lengths[i-1][j]>lengths[i][j-1])
                {
                    i--;
                }
                else
                {
                    j--;
                }
            }
        }
        System.out.println(ans);

    }
}
/*
Sample input (two lines on stdin):

AGGTAB
GXTXAYB

Output:

0  0  0  0  0  0  0  0
0  0  0  0  0  1  1  1
0  1  1  1  1  1  1  1
0  1  1  1  1  1  1  1
0  1  1  2  2  2  2  2
0  1  1  2  2  3  3  3
0  1  1  2  2  3  3  4
The longest LCS is:
GTAB

A couple more to try:
    ABCBDAB / BDCABA   -> length 4  (traceback prints BDAB; BCBA and BCAB are equally long)
    ABC    / ABC       -> length 3, "ABC"     (identical strings)
    ABC    / DEF       -> length 0, ""        (nothing in common)
 */

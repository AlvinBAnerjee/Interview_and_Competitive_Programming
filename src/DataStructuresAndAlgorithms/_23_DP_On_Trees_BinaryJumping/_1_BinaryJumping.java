package _23_DP_On_Trees_BinaryJumping;

import java.util.Arrays;

/*
Binary Jumping (a.k.a Sparse Table on ancestors)

Problem it solves: given a rooted tree (or just a "parent" array), answer
"who is the k-th ancestor of node x?" queries fast, for many queries.

Naive approach: walk up one parent at a time -> O(k) per query, O(n) worst case.

Idea: precompute, for every node, its 1st, 2nd, 4th, 8th, 16th ... ancestor
(powers of 2). This is exactly like a sparse table for range queries, but
instead of ranges we jump over ancestors.

up[i][j] = the (2^j)-th ancestor of node i
         = up[ up[i][j-1] ][j-1]   -> jump 2^(j-1) steps, then another 2^(j-1) steps

Base case: up[i][0] = parent[i]  (the 1st ancestor, i.e. 2^0 = 1 step up)

Building the table costs O(n log n) time/space.

To answer "k-th ancestor of x": write k in binary. Each set bit j means
"jump 2^j steps". Process bits from the highest to keep jumping node up.
Example: k = 13 = 1101 in binary = 8 + 4 + 1
  jump 8 steps, then 4 steps, then 1 step (using precomputed tables),
  each jump is O(1), so total query is O(log k).

Example tree (0 is root):
        0
       / \
      1   2
     /     \
    3       4
   /
  5

parent[] = [-1, 0, 0, 1, 2, 3]   (parent[0] = -1 means root has no parent)

up[i][0] (1st ancestor):
  up[0][0] = -1
  up[1][0] = 0
  up[2][0] = 0
  up[3][0] = 1
  up[4][0] = 2
  up[5][0] = 3

up[i][1] (2nd ancestor = jump 1 + 1):
  up[5][1] = up[ up[5][0] ][0] = up[3][0] = 1   -> 2nd ancestor of 5 is 1
  up[3][1] = up[ up[3][0] ][0] = up[1][0] = 0   -> 2nd ancestor of 3 is 0

up[i][2] (4th ancestor = jump 2 + 2), and so on...

So the 3rd ancestor of node 5 (k=3=011 binary -> jump 2, then jump 1):
  jump 2: node = up[5][1] = 1
  jump 1: node = up[1][0] = 0
  answer = 0 (the root) - matches path 5 -> 3 -> 1 -> 0 (3 steps up).
*/
public class _1_BinaryJumping {

    static final int LOG = 4; // enough since 2^4 = 16 > number of nodes (6) in the demo

    public static void main(String[] args) {
        int[] parent = {-1, 0, 0, 1, 2, 3};
        int n = parent.length;

        int[][] up = build(n, parent);

        System.out.println("3rd ancestor of node 5 = " + kthAncestor(up, 5, 3)); // expect 0
        System.out.println("2nd ancestor of node 5 = " + kthAncestor(up, 5, 2)); // expect 1
        System.out.println("1st ancestor of node 4 = " + kthAncestor(up, 4, 1)); // expect 2
        System.out.println("5th ancestor of node 5 = " + kthAncestor(up, 5, 5)); // expect -1 (out of range)

        for (int[] row : up) System.out.println(Arrays.toString(row));
    }

    static int[][] build(int n, int[] parent) {
        int[][] up = new int[n][LOG];
        for (int[] row : up) Arrays.fill(row, -1);

        for (int i = 0; i < n; i++) up[i][0] = parent[i];

        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                if (up[i][j - 1] != -1) {
                    up[i][j] = up[up[i][j - 1]][j - 1];
                }
            }
        }
        return up;
    }

    static int kthAncestor(int[][] up, int node, int k) {
        for (int j = LOG - 1; j >= 0 && node != -1; j--) {
            if ((k & (1 << j)) != 0) {
                node = up[node][j];
            }
        }
        return node;
    }
}
